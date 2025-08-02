import { StyleSheet, SafeAreaView, Text, Image, View } from 'react-native';
import useSpriteSheet from './rnsprite/spritesheets/useSpriteSheet';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: "green",
    borderColor: "red",
    borderStyle: "solid",
    borderWidth: 2,
  },
});


const App = () => {
  const TwemojiSprite = useSpriteSheet('twemoji');
  
  return (
    <SafeAreaView style={styles.container}>
      <TwemojiSprite icon="1f60a.png"/>
      <View
        style={{
          backgroundColor: "blue",
          width: "100%",
          height: 2,
          position: "absolute",
          top: "50%",
          marginTop: -1,
        }}
      />   
      <View
        style={{
          backgroundColor: "blue",
          height: "100%",
          width: 2,
          position: "absolute",
          left: "50%",
          marginLeft: -1,
        }}
      />   
  </SafeAreaView>
  );
}

export default App;