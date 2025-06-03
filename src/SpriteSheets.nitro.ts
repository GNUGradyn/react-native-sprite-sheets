import type { HybridViewProps, HybridView } from 'react-native-nitro-modules';

export interface SpriteProps extends HybridViewProps {
  pack: string
  icon: string
  width?: number
  height?: number 
}

export type Sprite = HybridView<SpriteProps>;