(ns user
  (:require
    [figwheel.main.api :as figwheel]))

(defn start
  ([]
   (start "dev"))

  ([build-id]
   (figwheel/start {:mode :serve} build-id)
   (figwheel/cljs-repl build-id)))


(defn stop
  ([]
   (figwheel/stop-all))

  ([build-id]
   (figwheel/stop build-id)))
