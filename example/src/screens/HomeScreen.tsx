import { SafeAreaView } from "react-native";
import useSpriteSheet from "../rnsprite/spritesheets/useSpriteSheet";

const HomeScreen = () => {
    const TwemojiSprite = useSpriteSheet('twemoji');

    return (
        <SafeAreaView>
            <TwemojiSprite icon="1f60a.png"/>
        </SafeAreaView>
    );
}

export default HomeScreen;