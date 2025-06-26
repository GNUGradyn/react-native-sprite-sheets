# react-native-sprite-sheets

Blazing‐fast, memory‐efficient icon rendering for React Native apps using pre-packed sprite sheets

## Installation
First, install the library
```sh
yarn install react-native-sprite-sheets
```
For iOS run cocoapods
```sh
pod install
```
Next, you need to decide where your sprites and output files will be stored. You will have an input and an output directory. How the input files are formatted will be described in the usage section. In this example I will use `src/rnsprite/sprites` as the input and `src/rnsprite/spritesheets` as the output but you can do it however you like. 

In your package.json under the `scripts` section, add the following entry
```json
"rnsprite:pack": "rnsprite src/rnsprite/sprites src/rnsprite/spritesheets"
```
Replacing the paths with your chosen input and output directory paths
## Usage
First, you need to add some sprites. The directory structure for your inputs is as follows
```
(input root)/
├─ animals/
│  ├─ dog.png
│  ├─ cat.png
│  ├─ mouse.png
├─ items/
│  ├─ keyboard.png
│  ├─ lamp.png
│  ├─ screwdriver.png
├─ food/
│  ├─ bread.png
│  ├─ rice.png
│  ├─ beans.png
```
Whenever one icon from a spritesheet is loaded, all icons in that spritesheet will be loaded in memory. So you want to sort your icons into folders based on where they are likely to be seen together in the application. For example if you have icons for your login screen and separate icons for the home screen, those could be separate spritesheets so the login screen icons arent in memory when logged in and vise versa. The file names can be anything but will be the names you use to reference the icons in your code.

When you have modified the input icons, you need to inform the library so it can recompile your the output spritesheets. You can do this with the package script you added earlier
```sh
yarn rnsprite:pack
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
