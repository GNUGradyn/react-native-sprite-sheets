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
      <Text>js side test</Text>
      <TwemojiSprite icon="1f0cf.png" style={{ width: 100, height: 100 }} />
    </SafeAreaView>
  );
}

export default App;