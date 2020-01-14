import * as monaco from "monaco-editor";

self.MonacoEnvironment = {
  getWorkerUrl: function(moduleId, label) {
    if (label === "json") {
      return "/js/json.worker.js";
    }
    if (label === "css") {
      return "/js/css.worker.js";
    }
    if (label === "html") {
      return "/js/html.worker.js";
    }
    if (label === "typescript" || label === "javascript") {
      return "/js/ts.worker.js";
    }
    return "/js/editor.worker.js";
  }
};
