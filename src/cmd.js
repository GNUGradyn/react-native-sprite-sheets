const Spritesmith = require('spritesmith');
const fs = require('fs');
const path = require('path');

if (process.argv.length != 4) {
    console.error(
`FAIL: Your react-native-sprite-sheets script line in package.json is invalid. It should be:
node ./node_modules/react-native-sprite-sheets/cmd.js (source path) (output path) [output width] [output height]
where () is mandatory, [] is optional, paths are relative to project root, and output dimensions are the maximum size before the sprite sheet is split`);
    process.exit(1);
}

const src = process.argv[3];

if (!fs.existsSync(src)) {
    console.error("FAIL: source directory does not exist. Check your react-native-sprite-sheets script line in package.json");
}

if (fs.readdirSync(src.length = 0)) {
    console.error("FAIL: no icons to compile");
}

const spritesmith = new Spritesmith();

let jobs = {};

// Validate everything and split into jobs
for(let file of fs.readdirSync(src)) {
    let entry = path.join(src, file);
    if (!fs.statSync().isDirectory) {
        console.error(`FAIL: sprite sheet ${entry} is not a directory`);
        process.exit(1);
    }


}