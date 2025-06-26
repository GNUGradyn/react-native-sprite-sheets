// This will be copied into the sprite sheet build output and modified by ts-morph

import React from "react";
import type { NativeSpriteMethods, NativeSpriteProps } from "./SpriteSheets.nitro";
import { getHostComponent } from "react-native-nitro-modules";
import NativeSpriteConfig from "../nitrogen/generated/shared/json/NativeSpriteConfig.json"

export const spriteSheetAssets: Record<string, SpriteSheetAsset> = {

}

type SpritesmithMap = Record<string, { x: number; y: number; width: number; height: number }>;

type SheetName = keyof typeof spriteSheetAssets;

interface SpriteSheetAsset {
    image: number
    map: SpritesmithMap
}

interface SpriteComponentProps {
    icon: string,
    width?: number
    height?: number
}

const NativeSprite = getHostComponent<NativeSpriteProps, NativeSpriteMethods>(
    "NativeSprite",
    () => NativeSpriteConfig
)

const useSpriteSheet = (sheetName: SheetName) => {
    if (sheetName.endsWith(".png")) sheetName = sheetName.slice(0, -4);

    const asset = spriteSheetAssets[sheetName];

    if (!asset) throw Error("Spritesheet " + sheetName + " could not be located. Try recompiling your spritesheets with `yarn rnsprite:pack`");

    const sprite: React.FC<SpriteComponentProps> = ({ icon, width, height }: SpriteComponentProps) => {
        const coordinates = asset.map[icon];

        if (!coordinates) throw new Error("Could not find icon " + icon + " in sheet " + sheetName + ". Try recompiling your spritesheets with `yarn rnsprite:pack`");

        return <NativeSprite assetID={asset.image} srcX={coordinates.x} srcY={coordinates.y} srcW={coordinates.width} srcH={coordinates.height} width={width} height={height} />
    }

    return React.memo(sprite);

}

export default useSpriteSheet;