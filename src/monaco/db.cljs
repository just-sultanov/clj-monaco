(ns monaco.db
  (:require
    [re-frame.core :as rf]))

(rf/reg-event-db
  ::init
  (fn [_ _]
    {}))
