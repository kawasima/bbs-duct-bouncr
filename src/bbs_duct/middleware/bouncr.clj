(ns bbs-duct.middleware.bouncr
  (:require [integrant.core :as ig]
            [ring.util.response :refer [redirect]])
  (:import [java.net URLEncoder]))

(defn wrap-bouncr
  [handler]
  (fn
    ([request]
     (if-let [bouncr-id (get-in request [:headers "x-bouncr-id"])]
       (handler (assoc request
                       :identity
                       {:id bouncr-id
                        :permissions (some-> (get-in request [:headers "x-bouncr-permissions"])
                                             (clojure.string/split #",")
                                             (vec)) }))
       (redirect (str "/my/signIn?url=" (URLEncoder/encode "/bbs" "UTF-8")))))
    ([request respond raise]
     (handler request respond raise))))

(defmethod ig/init-key ::bouncr [_ {:keys [error-handler]}]
  #(wrap-bouncr %))
