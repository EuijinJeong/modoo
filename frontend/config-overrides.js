const webpack = require('webpack');

module.exports = {
    webpack: (config, env) => {
        // process/browser 설정 추가 (폴리필)
        config.resolve.fallback = {
            ...config.resolve.fallback,
            "process": require.resolve("process/browser"),
        };

        // Webpack의 ProvidePlugin을 사용하여 전역적으로 process 변수를 제공
        config.plugins.push(
            new webpack.ProvidePlugin({
                process: 'process/browser',
            })
        );

        // fullySpecified 설정 추가 (확장자 명시 문제 해결)
        config.module.rules.unshift({
            test: /\.m?js$/,
            resolve: {
                fullySpecified: false, // 확장자 명시 안 해도 되도록 설정
            },
        });

        return config;
    },
};
