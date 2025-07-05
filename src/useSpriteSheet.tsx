// This will be copied into the sprite sheet build output and modified by ts-morph

import React from "react";
import type { NativeSpriteMethods, NativeSpriteProps } from "./SpriteSheets.nitro";
import { getHostComponent } from "react-native-nitro-modules";
import { _NativeSpriteConfig } from "react-native-sprite-sheets"
import { type ViewStyle } from "react-native";

export const spriteSheetAssets: Record<string, SpriteSheetAsset> = {

}

type SpritesmithMap = Record<string, { x: number; y: number; width: number; height: number }>;

type SheetName = keyof typeof spriteSheetAssets;

interface SpriteSheetAsset {
    image: number
    map: SpritesmithMap
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
)

const useSpriteSheet = (sheetName: SheetName) => {
    if (sheetName.endsWith(".png")) sheetName = sheetName.slice(0, -4);

    const asset = spriteSheetAssets[sheetName];

    if (!asset) throw Error("Spritesheet " + sheetName + " could not be located. Try recompiling your spritesheets with `yarn rnsprite:pack`");


    const sprite: React.FC<SpriteComponentProps> = (props: SpriteComponentProps) => {
        const coordinates = asset.map[props.icon];
        if (!coordinates) throw new Error("Could not find icon " + props.icon + " in sheet " + sheetName + ". Try recompiling your spritesheets with `yarn rnsprite:pack`");

        if (!props.style) props.style = {};
        if (props.style.height || props.style.width) console.warn("Height and width styles are ignored when using sprites. Use the `width` and `height` props instead.");
        const height = props.height || coordinates.height;
        const width = props.width || coordinates.width;

        return <NativeSprite assetID={asset.image} srcX={coordinates.x} srcY={coordinates.y} srcW={coordinates.width} srcH={coordinates.height} width={width} height={height} style={props.style} />
    }

    return React.memo(sprite);

}

export default useSpriteSheet;