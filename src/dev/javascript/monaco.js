import * as monaco from "monaco-editor";

self.MonacoEnvironment = {
  getWorkerUrl: function (moduleId, label) {
    if (label === "json") {
      return "./assets/json.worker.js";
    }
    if (label === "css") {
      return "./assets/css.worker.js";
    }
    if (label === "html") {
      return "./assets/html.worker.js";
    }
    if (label === "typescript" || label === "javascript") {
      return "./assets/ts.worker.js";
    }
    return "./assets/editor.worker.js";
  },
};
