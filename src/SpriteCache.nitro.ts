import type { HybridObject } from "react-native-nitro-modules";

export interface SpriteCache extends HybridObject<{ ios: 'swift', android: 'kotlin' }> {
  private data class Entry(
    val tokens: MutableSet<String> = hashSetOf(),
    var img: CloseableReference<CloseableImage>? = null
  )
  private val map = ConcurrentHashMap<String, Entry>()

  override suspend fun pin(uri: String, token: String) {
    synchronized(this) {
      val e = map.getOrPut(uri) { Entry() }
      if (!e.tokens.add(token)) return           // duplicate pin by same consumer → no-op
      if (e.img != null) return                  // already pinned/decoded
    }

    // First unique token: decode & pin (suspend until done)
    return kotlinx.coroutines.suspendCancellableCoroutine { cont ->
      val req = ImageRequestBuilder.newBuilderWithSource(uri.toUri()).build()
      val ds = Fresco.getImagePipeline().fetchDecodedImage(req, null)
      ds.subscribe(object : BaseDataSubscriber<CloseableReference<CloseableImage>>() {
        override fun onNewResultImpl(source: DataSource<CloseableReference<CloseableImage>>?) {
          try {
            if (source != null && source.isFinished) {
              synchronized(this@SpriteCacheImpl) {
                val e = map[uri]
                e?.img = CloseableReference.cloneOrNull(source.result)
              }
              if (cont.isActive) cont.resume(Unit, onCancellation = null)
            }
          } finally { source?.close() }
        }
        override fun onFailureImpl(source: DataSource<CloseableReference<CloseableImage>>?) {
          synchronized(this@SpriteCacheImpl) {
            map[uri]?.tokens?.remove(token)
            if (map[uri]?.tokens?.isEmpty() == true && map[uri]?.img == null) map.remove(uri)
          }
          if (cont.isActive) cont.resumeWithException(RuntimeException("Failed to pin $uri"))
        }
      }, UiThreadImmediateExecutorService.getInstance())

      cont.invokeOnCancellation { ds.close() }
    }
  }

  override fun release(uri: String, token: String) {
    synchronized(this) {
      val e = map[uri] ?: return
      e.tokens.remove(token)
      if (e.tokens.isEmpty()) {
        CloseableReference.closeSafely(e.img)
        map.remove(uri)
      }
    }
  }
}
