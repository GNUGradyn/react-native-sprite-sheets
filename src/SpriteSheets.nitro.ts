import { requireNativeComponent } from 'react-native';
import type { HybridView, HybridViewProps } from 'react-native-nitro-modules';

export interface NativeSpriteProps extends HybridViewProps {
  pack: string;
  icon: string;
  width?: number;
  height?: number;
  srcX: number;
  srcY: number;
  srcW: number;
  srcH: number;
}

export type SpriteView = HybridView<NativeSpriteProps>;

export const NativeSprite = requireNativeComponent<NativeSpriteProps>('Sprite');

export default {} as SpriteView;