import { StyleSheet, View } from "react-native";

interface EmojiKeyboardProps {
    spritesheet: string
}

const style = StyleSheet.create({
    container: {
        width: "100%",
        height: "50%",
        position: "absolute",
        bottom: 0
    }
});

const EmojiKeyboard: React.FC<EmojiKeyboardProps> = (props: EmojiKeyboardProps) => {
    return (
        <View style={style.container}>
            
        </View>
    );
}

export default EmojiKeyboard;