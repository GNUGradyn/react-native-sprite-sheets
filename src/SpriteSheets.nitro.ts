import type { HybridView, HybridViewMethods, HybridViewProps } from 'react-native-nitro-modules';

export interface NativeSpriteProps extends HybridViewProps {
  assetID: number;
  width?: number;
  height?: number;
  srcX: number;
  srcY: number;
  srcW: number;
  srcH: number;
}

export interface NativeSpriteMethods extends HybridViewMethods { }

export type NativeSprite = HybridView<NativeSpriteProps, NativeSpriteMethods>;