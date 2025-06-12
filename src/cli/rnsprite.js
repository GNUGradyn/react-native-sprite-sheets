"use strict"

const Spritesmith = require('spritesmith');
const fs = require('fs');
const path = require('path');
const events = require('events');

const writeStreamSync = (istream, path, opts = {}) => {
    return new Promise(async (resolve, reject) => {
        const ostream = fs.createWriteStream(path, opts);
        istream.pipe(ostream);
        ostream.on('error', reject);
        istream.on('error', reject);
        await events.once(ostream, 'finish');
        resolve();
    });
};


const main = async () => {
    if (process.argv.length != 4) {
        console.error(
            `FAIL: Your react-native-sprite-sheets script line in package.json is invalid. It should be:
node ./node_modules/react-native-sprite-sheets/cmd.js (source path) (output path) [output width] [output height]
where () is mandatory, [] is optional, paths are relative to project root, and output dimensions are the maximum size before the sprite sheet is split`);
        process.exit(1);
    }

    const src = process.argv[2];
    const out = process.argv[3];

    if (!fs.existsSync(src)) {
        console.error("FAIL: source directory does not exist. Check your react-native-sprite-sheets script line in package.json");
        process.exit(1);
    }

    if (fs.readdirSync(src).length == 0) {
        console.error("FAIL: no icons to compile");
        process.exit(1);
    }

    const spritesmith = new Spritesmith();

    let jobs = {};

    // Validate everything and split into jobs
    for (let spriteSheetDirInputName of fs.readdirSync(src)) {
        let spriteSheetDirInputPath = path.join(src, spriteSheetDirInputName);
        if (!fs.statSync(spriteSheetDirInputPath).isDirectory) {
            console.error(`FAIL: sprite sheet ${spriteSheetDirInputPath} is not a directory`);
            process.exit(1);
        }

        let filesInSheetNames = fs.readdirSync(spriteSheetDirInputPath);
        let filesInSheetPaths = filesInSheetNames.map(fileInSheetName => path.join(spriteSheetDirInputPath, fileInSheetName));

        if (filesInSheetPaths.some(fileInSheetPath => fs.statSync(fileInSheetPath).isDirectory())) {
            console.error("FAIL: sprite sheet " + spriteSheetDirInputName + " contains directories");
            process.exit(1);
        }

        jobs[spriteSheetDirInputName + ".png"] = filesInSheetPaths;
    }

    const existingOutFiles = fs.readdirSync(out);
    if (existingOutFiles.length > 0) {
        if (existingOutFiles.some(file => !file.endsWith(".png") && !file.endsWith(".json"))) {
            console.error("FAIL: output directory contains files that are not images or JSON. Refusing to clean up automatically. Try deleting the output directory contents manually");
            process.exit(1);
        }
        console.log("Output directory not clean. Cleaning");
        existingOutFiles.forEach(existingOutFile => fs.rmSync(path.join(out, existingOutFile)));
    }

    for (let i = 0; i < Object.keys(jobs).length; i++) {
        let jobkey = Object.keys(jobs)[i];
        let job = jobs[jobkey];
        console.log(`Processing sprite sheet ${jobkey} (job ${i + 1} of ${Object.keys(jobs).length})`);

        let spriteSheetImgOutputPath = path.join(out, jobkey);

        spritesmith.createImages(job, async (err, images) => {
            if (err) {
                console.error(err);
                process.exit(2);
            }

            let result = spritesmith.processImages(images);

            await writeStreamSync(result.image, spriteSheetImgOutputPath);
            fs.writeFileSync(spriteSheetImgOutputPath + ".json", JSON.stringify(result.coordinates), 'utf8');
        });
    }
}

main();