(defproject lein-baum-example "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :plugins [[rkworks/lein-baum "0.1.0"]]

  :baum-path "example.edn"
  :port ^:baum/get-in [:webserver :port]

  $let [foo "bar"]
  :foo {- foo}                          ; => "bar"

  :env {:context "dev"}
  :dev {env :context}                   ; => "dev"
  :dev* ^:baum/get-in [:context]        ; => "dev"
  )
