import { StyleSheet, SafeAreaView, Text } from 'react-native';
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
      <TwemojiSprite icon="1f0cf.png"/>
    </SafeAreaView>
  );
}

export default App;