var webpack = require('webpack')
var path = require('path')
var ExtractTextPlugin = require('extract-text-webpack-plugin')

module.exports = {
  entry: [
    './app/assets/js/main.js',
    './app/assets/css/main.sass'
  ],

  output: {
    libraryTarget: 'umd',
    library: 'Croissify',
    umdNamedDefine: true,
    path: path.resolve(__dirname, 'public'),
    filename: 'javascripts/croissify.js'
  },

  resolve: {
    extensions: ['', '.js', '.sass']
  },

  module: {
    loaders: [
      { test: /\.js(x?)$/, loader: 'babel', exclude: /node_modules/ },
      { test: /\.sass$/, loader: ExtractTextPlugin.extract("css?sourceMap!sass?sourceMap") }
    ]
  },

  plugins: [
    new ExtractTextPlugin('stylesheets/croissify.css')
  ]
};
