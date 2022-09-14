const { defineConfig } = require('@vue/cli-service')
const Dotenv = require('dotenv-webpack');
const webpack = require('webpack')
module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack: {
    plugins: [
      new Dotenv(),
      new webpack.ProvidePlugin({
        Buffer: ['buffer', 'Buffer'],
      })
    ],
    resolve: {
      extensions: ['.ts', '.js'],
      fallback: {
        "buffer": require.resolve("buffer")
      }
    },
    // devServer: {
    //   proxy: 'http://localhost:8080/api'
    // }
  }
})
