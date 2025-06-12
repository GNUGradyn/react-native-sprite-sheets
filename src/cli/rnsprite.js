"use strict"

const Spritesmith = require('spritesmith');
const fs = require('fs');
const path = require('path');
const util = require('util');
const { exec } = require('child_process');
const runSpritesmith = util.promisify(Spritesmith.run);

const executeJob = async (job, jobkey) => {
    const result = await runSpritesmith({ src: job });

    if (result.err) {
        console.error(err);
        process.exit(2);
    }

    targets.forEach(target => {
        fs.writeFileSync(path.join(target, "rnsprite", jobkey), result.image);
        fs.writeFileSync(path.join(target, "rnsprite", jobkey) + ".json", JSON.stringify(result.coordinates));
    });
}

if (process.argv.length != 3) {
    console.error(
        `FAIL: Your react-native-sprite-sheets script line in package.json is invalid. It should be:
node ./node_modules/react-native-sprite-sheets/cmd.js path-to-sprites-directory`);
    process.exit(1);
}

const src = process.argv[2];

if (!fs.existsSync(src)) {
    console.error("FAIL: source directory does not exist. Check your react-native-sprite-sheets script line in package.json");
    process.exit(1);
}

if (fs.readdirSync(src).length == 0) {
    console.error("FAIL: no icons to compile");
    process.exit(1);
}

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

const targets = [];

if (fs.existsSync("android")) {
    const androidPath = path.join("android", "app", "src", "main", "assets");
    if (!fs.existsSync(androidPath)) fs.mkdirSync(androidPath);
    targets.push(androidPath);
}
if (fs.existsSync("ios")) targets.push("ios");
if (fs.existsSync("web")) console.error("WEB IS NOT YET SUPPORTED! It will be skipped as a build target");
if (fs.length == 0) {
    console.error("FAIL: No supported targets");
    process.exit(1);
}

for (let target of targets) {
    let targetWorkDir = path.join(target, "rnsprite");
    if (fs.existsSync(targetWorkDir)) fs.rmSync(targetWorkDir, { recursive: true });
    fs.mkdirSync(targetWorkDir);
    fs.writeFileSync(path.join(targetWorkDir, "README.txt"),
        `This directory is generated automatically by react-native-sprite-sheets.
Any modifications made to this directory will be overriden next time the sprite sheets are compiled.
Instead, modify the sprite inputs and rerun your rnsprite:pack script as documented at https://github.com/GNUGradyn/react-native-sprite-sheets`);
}

const promises = [];

for (let i = 0; i < Object.keys(jobs).length; i++) {
    let jobkey = Object.keys(jobs)[i];
    let job = jobs[jobkey];
    console.log(`Queueing job ${jobkey} (job ${i + 1} of ${Object.keys(jobs).length})`);

    promises.push(executeJob(job, jobkey));
}

// IIFE so we can await Promise.all so we can console.log after
(async () => {
    await Promise.all(promises);
    console.log("Done. Remember to rebuild your native clients");
})();