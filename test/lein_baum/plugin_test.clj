(ns lein-baum.plugin-test
  (:require [baum.core :as baum]
            [clojure.test :refer :all]
            [lein-baum.plugin :refer :all]
            [leiningen.core.project :as lein]))

(def fixture {:baum-path "foo/bar.edn"
              :simple ^:baum/get-in [:foo :bar]
              :whole ^:baum/get-in []
              :nil ^:baum/get-in [:foo :bar :baz]})

(def env-fixture {:baum-path "foo/bar.edn"
                  :env {:port 1000}
                  :get-env ^:baum/get-in [:foo]})

(def reduction-fixture '{$let [foo "foo"]
                         :foo {baum/ref foo}})

(deftest simple-get-in-test
  (with-redefs [baum/read-config (constantly {:foo {:bar :baz}})]
    (is (= (middleware fixture)
           (assoc fixture
                  :simple :baz
                  :whole {:foo {:bar :baz}}
                  :nil nil)))))

(deftest read-env-test
  (is (= (read-env {:env {:foo :bar :o_0 :baz "qux" :quux}})
         {:foo :bar :o-0 :baz :qux :quux})))

(deftest environ-test
  (with-redefs [slurp (constantly "{:foo #env :port}")]
    (is (= (middleware env-fixture)
           (assoc env-fixture
                  :get-env 1000)))))

(deftest project-tree-reduction
  (is (= (middleware reduction-fixture)
         {:foo "foo"})))

(deftest integration
  (is (= (-> (lein/read "example/project.clj")
             (select-keys [:port :foo :dev :dev*]))
         {:port 3000
          :foo "bar"
          :dev "dev"
          :dev* "dev"})))
