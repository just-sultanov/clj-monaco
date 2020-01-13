const postcssImport = require("postcss-import");
const postcssUrl = require("postcss-url");
const autoprefixer = require("autoprefixer");
const tailwindcss = require("tailwindcss");

const assetsPath = "dev/resources/public/assets/js/";

module.exports = {
  plugins: [
    postcssImport(),
    postcssUrl({
      filter: "**/*.ttf",
      url: "copy",
      basePath: ".",
      assetsPath: assetsPath,
      useHash: true
    }),
    postcssUrl({
      filter: "**/*.ttf",
      url: asset => `${asset.url.replace(assetsPath, "")}`
    }),
    tailwindcss(),
    autoprefixer()
  ]
};
