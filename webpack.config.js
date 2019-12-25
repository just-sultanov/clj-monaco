const path = require('path');

module.exports = {
  mode: 'development',
  entry: './src/index.js',
  output: {
    filename: 'index.min.js',
    path: path.resolve(__dirname, 'dev/resources/public/assets/js')
  },
  module: {
    rules: [
      {
        test: /(\.woff|\.woff2)$/,
        loader: 'ignore-loader'
      },
      {
        test: /\.ttf$/,
        loader: 'ignore-loader'
      },
      {
        test: /\.eot$/,
        loader: 'ignore-loader'
      },
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader']
      }]
  }
};
