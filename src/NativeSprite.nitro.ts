import type { HybridView, HybridViewMethods, HybridViewProps } from 'react-native-nitro-modules';

export interface NativeSpriteProps extends HybridViewProps {
  assetUri: string;
  // Width and height required because react native does not get a size from the native side and the we have the dimensions from the map anyway
  srcX: number;
  srcY: number;
  srcW: number;
  srcH: number;
}

export interface NativeSpriteMethods extends HybridViewMethods { }

export type NativeSprite = HybridView<NativeSpriteProps, NativeSpriteMethods>;