const webpack = require('webpack');

module.exports = {
    resolve: {
        fallback: {
            process: require.resolve('process/browser'), // process 모듈을 브라우저에서 사용할 수 있도록 polyfill
        },
    },
    plugins: [
        new webpack.ProvidePlugin({
            process: 'process/browser', // Webpack에서 process 모듈을 브라우저 환경에 제공
        }),
    ],
};

