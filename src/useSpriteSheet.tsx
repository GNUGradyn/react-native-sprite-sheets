// This will be copied into the sprite sheet build output and modified by ts-morph

import React, { useEffect, useMemo } from "react";
import type { NativeSpriteMethods, NativeSpriteProps } from "./NativeSprite.nitro";
import { getHostComponent, NitroModules } from "react-native-nitro-modules";
import { _NativeSpriteConfig } from "react-native-sprite-sheets";
import { Image, PixelRatio, StyleSheet, type ViewStyle } from "react-native";
import type { SpriteCache } from "./SpriteCache.nitro";

export const spriteSheetAssets: Record<string, SpriteSheetAsset> = {};

type SpritesheetMap = Record<string, { x: number; y: number; width: number; height: number }>;
type SpritesheetMetadata = {
    coordinates: SpritesheetMap;
    width: number;
    height: number;
};

type SheetName = keyof typeof spriteSheetAssets;

interface SpriteSheetAsset {
    image: number
    metadata: SpritesheetMetadata
}

interface SpriteComponentProps {
    icon: string
    width?: number
    height?: number
    style?: ViewStyle
}

export const NativeSprite = getHostComponent<NativeSpriteProps, NativeSpriteMethods>(
  "NativeSprite",
  () => _NativeSpriteConfig
);

const Cache = NitroModules.createHybridObject<SpriteCache>("SpriteCache");

const toDp = (px: number) => px / PixelRatio.get();
const makeToken = () => Math.random().toString(36).slice(2);

const useSpriteSheet = (sheetName: SheetName) => {
  if (sheetName.endsWith(".png")) sheetName = sheetName.slice(0, -4);
  const asset = spriteSheetAssets[sheetName];
  if (!asset) {
    throw Error(
      "Spritesheet " + sheetName + " could not be located. Try recompiling your spritesheets with `yarn rnsprite:pack`"
    );
  }

  const sheetUri = Image.resolveAssetSource(asset.image).uri;

  const Sprite: React.FC<SpriteComponentProps> = (props) => {
    const coords = asset.metadata.coordinates[props.icon];
    if (!coords) {
      throw new Error(
        "Could not find icon " + props.icon + " in sheet " + sheetName + ". Try recompiling your spritesheets with `yarn rnsprite:pack`"
      );
    }

    // Each instance gets a stable token
    const token = useMemo(makeToken, []);

    // Pin on mount; release on unmount
    useEffect(() => {
      // fire-and-forget; native is idempotent per token
      Cache.pin(sheetUri, token).catch(() => {});
      return () => {
        Cache.release(sheetUri, token);
      };
    }, [sheetUri, token]);

    const base = StyleSheet.flatten(props.style) || {};
    if ((base as any).width || (base as any).height) {
      console.warn(
        "Height and width styles are ignored when using sprites. Use the `width` and `height` props instead."
      );
    }
    const widthDp = props.width ?? toDp(coords.width);
    const heightDp = props.height ?? toDp(coords.height);

    const style: ViewStyle = { ...base, width: widthDp, height: heightDp };

    return (
      <NativeSprite
        assetUri={sheetUri}
        srcX={coords.x}
        srcY={coords.y}
        srcW={coords.width}
        srcH={coords.height}
        style={style}
      />
    );
  };

  return React.memo(Sprite);
};

export default useSpriteSheet;