import { StyleSheet, SafeAreaView } from 'react-native';
import { createSpritesheet } from 'react-native-sprite-sheets';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});


const App = () => {
  const TwemojiSprite = createSpritesheet('twemoji');
  
  return (
    <SafeAreaView style={styles.container}>
      <TwemojiSprite icon="1f0cf.png"/>
    </SafeAreaView>
  );
}

export default App;