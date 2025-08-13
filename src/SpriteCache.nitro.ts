import type { HybridObject } from "react-native-nitro-modules";

export interface SpriteCache extends HybridObject<{ ios: 'swift', android: 'kotlin' }> {
    pin (uri: string, token: string, height: number, width: number): Promise<void>
    release (uri: string, token: string): void
}
