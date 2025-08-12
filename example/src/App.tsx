import { StyleSheet, SafeAreaView } from 'react-native';
import useSpriteSheet from './rnsprite/spritesheets/useSpriteSheet';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});


const App = () => {
  const TwemojiSprite = useSpriteSheet('twemoji');
  
  return (
    <SafeAreaView style={styles.container}>
      <TwemojiSprite icon="1f60a.png"/>
  </SafeAreaView>
  );
}

export default App;