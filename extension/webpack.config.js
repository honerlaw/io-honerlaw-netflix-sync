var webpack = require("webpack");

module.exports = {
    entry: './src/App.jsx',
    output: {
        filename: './dist/js/bundle.js'
    },
    resolve: {
        extensions: ['', '.webpack.js', '.web.js', '.js', '.jsx'],
        modulesDirectories: ["node_modules"]
    },
    module: {
        loaders: [
            {
                test: /\.jsx?$/,
                loader: 'babel-loader',
                query: {
                    presets: ['es2015', 'react']
                }
            }
        ]
    },
    plugins: [
        new webpack.DefinePlugin({
            'process.env.NODE_ENV': '"production"'
        }),
        new webpack.optimize.UglifyJsPlugin({ minimize: true, compile: { warnings: false } })
    ]
}
