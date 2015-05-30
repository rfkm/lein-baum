(ns lein-baum.plugin
  (:require [baum.core :as baum]
            [clojure.walk :as walk]
            [environ.core :refer [env] :as environ]))

(defn reader->reducer [reader]
  (fn [m v opts]
    (baum/reduction (reader v) opts)))

(def reader-reducers (->> (baum/default-readers)
                          (map (fn [[k v]] [k (reader->reducer v)]))
                          (into {})))

(def reader-option (baum/update-options {:reducers reader-reducers}))

(defn baum-path [project]
  (:baum-path project))

(defn replace? [x]
  (:baum/get-in (meta x)))

(defn try-replace [conf x]
  (if (replace? x)
    (get-in conf x)
    x))

(defn read-env [project]
  (into {} (map (fn [[k v]] [(#'environ/sanitize k) v]) (:env project))))

(defn middleware [project]
  (let [env' (read-env project)
        project (with-redefs [env env']
                  (baum/reduction project reader-option))]
    (if-let [path (baum-path project)]
      (let [conf (with-redefs [env env'] (baum/read-config path))]
        (walk/prewalk (partial try-replace conf)
                      project))
      project)))
