import { useSpritesheetMeta } from "./spritesheetMeta";
import { NativeSprite } from "./SpriteSheets.nitro";

export interface SpriteProps  {
  pack: string
  icon: string
  width?: number
  height?: number 
}
/**
 * Factory that gives you a ready-to-go <Sprite /> component
 * bound to a single pack.
 * @param pack name of pack to bind
 * @param options autoDispose: rather or not the sprite sheet should be removed from memory when it is no longer in use. defaults to true
 * @returns 
 */
export const createSpritesheet = (
  pack: string,
  options: { autoDispose?: boolean } = {}
) => {
  return function SpriteSheet({
    icon,
    width,
    height,
  }: {
    icon: string;
    width?: number;
    height?: number;
  }) {
    const meta = useSpritesheetMeta(pack, options);
    if (!meta) return null;

    const frame = meta[icon];
    if (!frame) {
      console.warn(`Icon ${icon} not found in pack ${pack}`);
      return null;
    }

    const w = width  ?? frame.width;
    const h = height ?? frame.height;

    return (
      <NativeSprite
        pack={pack}
        icon={icon}
        srcX={frame.x}
        srcY={frame.y}
        srcW={frame.width}
        srcH={frame.height}
        width={w}
        height={h}
      />
    );
  };
}