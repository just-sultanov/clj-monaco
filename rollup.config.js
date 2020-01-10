import postcss from "rollup-plugin-postcss";
import resolve from "@rollup/plugin-node-resolve";
import typescript from "rollup-plugin-typescript2";
import { terser } from "rollup-plugin-terser";

export default [
  {
    input: "./dev/src/index.js",
    output: {
      dir: "./dev/resources/public/assets/",
      format: "esm"
    },
    plugins: [
      resolve(),
      postcss({ extract: true }),
      typescript({ objectHashIgnoreUnknownHack: true })
    ]
  },
  {
    input: "./dev/src/index.js",
    output: {
      dir: "./dev/resources/public/assets/min",
      format: "esm"
    },
    plugins: [
      resolve(),
      postcss({ extract: true, minimize: true }),
      typescript({ objectHashIgnoreUnknownHack: true }),
      terser()
    ]
  }
];
