import { NitroModules } from 'react-native-nitro-modules';
import type { SpriteSheets } from './SpriteSheets.nitro';

const SpriteSheetsHybridObject =
  NitroModules.createHybridObject<SpriteSheets>('SpriteSheets');

export function multiply(a: number, b: number): number {
  return SpriteSheetsHybridObject.multiply(a, b);
}
