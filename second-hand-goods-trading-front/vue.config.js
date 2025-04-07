const webpack = require('webpack')

module.exports = {
    lintOnSave: false, // 关闭 ESLint 检查
    publicPath: './',
    // publicPath: process.env.NODE_ENV === 'production' ? '/' : '/',
    // assetsDir: 'static',
    productionSourceMap: false,
    configureWebpack: {
        plugins: [
            new webpack.ProvidePlugin({
                $: "jquery",
                jQuery: "jquery",
                "windows.jQuery": "jquery"
            })
        ]
    },
    devServer: {
        port: 8080,
    },
    devServer: {
        proxy: {
            '/api':{
                target:'http://localhost:8080',
                changeOrigin:true,
                pathRewrite:{
                    '/api':'http://localhost:8080'
                }
            }
        }
    }
};