module.exports = {
  devServer: {
    port: 8009,
    proxy: {
      '/api': {
        target: 'http://localhost:8009/v1',
        changeOrigin: true,
        pathRewrite: {
          '^/api': '',
        },
      },
    },
  },
  transpileDependencies: ['vuetify'],
  chainWebpack: (config) => {
    config.plugin('html').tap((args) => {
      args[0].title = 'Book a Seat!';
      return args;
    });
  },
};
