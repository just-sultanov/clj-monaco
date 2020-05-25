const fs = require("fs");
const path = require("path");
const tailwindcss = require("tailwindcss");
const autoprefixer = require("autoprefixer");
const postcssUrl = require("postcss-url");
const postcssImport = require("postcss-import");

const fontsRelativePath = `fonts`;
const fontsPath = path.resolve("public/assets", fontsRelativePath);
const nodeModulesPath = path.resolve("node_modules");

const overrides = {};

const fixUrl = (asset) => {
  const assetAbsolutePath = asset.absolutePath;
  let x;

  Object.entries(overrides).map(([module, opts]) => {
    if (assetAbsolutePath && assetAbsolutePath.toLowerCase().includes(module)) {
      const { fixPaths } = opts;
      x = fixPaths(asset);
    }
  });

  const { absolutePath, relativePath } = x ? x : asset;
  const copyFrom = absolutePath;
  const copyTo = path.resolve(fontsPath, relativePath);
  fs.mkdirSync(path.dirname(copyTo), { recursive: true });
  fs.copyFileSync(copyFrom, copyTo);

  return `${fontsRelativePath}/${relativePath}`;
};

module.exports = {
  plugins: [
    postcssImport(),
    postcssUrl({
      filter: "**/*.+(eot|ttf|woff|woff2)",
      basePath: nodeModulesPath,
      assetsPath: fontsPath,
      url: fixUrl,
    }),
    tailwindcss(),
    autoprefixer(),
  ],
};
