
const path = require("path");

const createExtensionWebpackConfig = require('../../MapStore2/build/createExtensionWebpackConfig');
const FileManagerPlugin = require('filemanager-webpack-plugin');
const { name } = require('../../config');
const commons = require('./commons');

// the build configuration for production allow to create the final zip file, compressed accordingly
const plugins = [
    new FileManagerPlugin({
        events: {
            onEnd: {
                copy: [
                    { source: path.resolve(__dirname, "..", "..", "assets", "translations"), destination: 'dist/translations' },
                    { source: path.resolve(__dirname, "..", "..", "assets", "index.json"), destination: 'dist/index.json' },
                ],
                archive: [
                    { 
                        source: 'dist', destination: `dist/${name}.zip`,

                    },
                ],
            },
        },
    })
];
module.exports = createExtensionWebpackConfig({ prod: true, name, ...commons, plugins });
