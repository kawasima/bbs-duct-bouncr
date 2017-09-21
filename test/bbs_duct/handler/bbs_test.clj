(ns bbs-duct.handler.bbs-test
  (:require [clojure.test :refer :all]
            [kerodon.core :refer :all]
            [kerodon.test :refer :all]
            [integrant.core :as ig]
            [bbs-duct.handler.bbs :as bbs]))

(def handler
  (ig/init-key :bbs-duct.handler/bbs {}))

(deftest smoke-test
  (testing "bbs page exists"
    (-> (session handler)
        (visit "/bbs")
        (has (status? 200) "page exists"))))
