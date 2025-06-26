import { requireNativeComponent } from 'react-native';
import type { HybridView, HybridViewProps } from 'react-native-nitro-modules';

export interface NativeSpriteProps extends HybridViewProps {
  assetID: number;
  width?: number;
  height?: number;
  srcX: number;
  srcY: number;
  srcW: number;
  srcH: number;
}

export type SpriteView = HybridView<NativeSpriteProps>;

/**
 * DO NOT USE! This is an internal component used for sprite rendering
 */
export const NativeSprite = requireNativeComponent<NativeSpriteProps>('NativeSprite');

export default {} as SpriteView;