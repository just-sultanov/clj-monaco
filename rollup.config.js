const postcss = require("rollup-plugin-postcss");
const resolve = require("@rollup/plugin-node-resolve");
const typescript = require("rollup-plugin-typescript2");
const { terser } = require("rollup-plugin-terser");

const production = !process.env.ROLLUP_WATCH;

export default {
  input: "src/dev/index.js",
  output: {
    dir: "public/js",
    format: "esm"
  },
  plugins: [
    resolve(),
    postcss({
      extract: true,
      minimize: production
    }),
    typescript({ objectHashIgnoreUnknownHack: true }),
    production && terser()
  ]
};
