import { useEffect, useState } from 'react';
import { NativeModules } from 'react-native';

type SpriteMeta = { x: number; y: number; width: number; height: number };
type MetaMap = Record<string, SpriteMeta>;

// module-level caches
const metaCache = new Map<string, MetaMap>();
const refCount  = new Map<string, number>();

/**
 * Internal hook to fetch metadata over the bridge
 * @param pack Name of sprite sheet to use
 * @param options autoDispose: rather or not the sprite sheet should be removed from memory when it is no longer in use. defaults to true
 * @returns 
 */
export const useSpritesheetMeta = (
  pack: string,
  { autoDispose = true }: { autoDispose?: boolean } = {}
): MetaMap | null => {
  const [meta, setMeta] = useState<MetaMap | null>(() => metaCache.get(pack) ?? null);

  useEffect(() => {
    let alive = true;

    if (autoDispose) {
      refCount.set(pack, (refCount.get(pack) ?? 0) + 1);
    }

    NativeModules.SpriteModule.preloadSheet(pack);

    if (metaCache.has(pack)) {
      setMeta(metaCache.get(pack)!);
    } else {
      NativeModules.SpriteModule.loadMetadata(pack)
        .then((m: MetaMap) => {
          metaCache.set(pack, m);
          if (alive) setMeta(m);
        })
        .catch(() => {
          if (alive) setMeta(null);
        });
    }

    return () => {
      alive = false;

      if (autoDispose) {
        // JS: drop metadata when last unmounts
        const newCount = (refCount.get(pack) ?? 1) - 1;
        if (newCount <= 0) {
          metaCache.delete(pack);
          refCount.delete(pack);
        } else {
          refCount.set(pack, newCount);
        }
        // Native: release the sheet (and recycle when count hits zero)
        NativeModules.SpriteModule.releaseSheet(pack);
      }
    };
  }, [pack, autoDispose]);

  return meta;
}