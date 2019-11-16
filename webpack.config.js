const MonacoWebpackPlugin = require('monaco-editor-webpack-plugin');

module.exports = {
  entry: './src/index.js',
  output: {
    filename: 'index.min.js',
    path: __dirname + '/dev/resources/public/assets/js'
  },
  plugins: [
    new MonacoWebpackPlugin()
  ]
};
