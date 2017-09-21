(ns bbs-duct.handler.bbs
  (:require [compojure.core :refer :all]
            [clojure.java.io :as io]
            [clojure.java.jdbc :as jdbc]
            [rum.core :as rum]
            [integrant.core :as ig]
            [ring.util.response :refer [redirect]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [bbs-duct.boundary.tables :as tables])
  (:import [java.sql Timestamp]))

(defmethod ig/init-key :bbs-duct.handler/bbs [_ {:keys [db]}]
  (context "/" []
    (GET "/" [:as {:keys [identity]}]
      (let [comments (tables/get-comments db)]
        (rum/render-html
         [:html
          [:head
           [:meta {:charset "utf-8"}]]
          [:body
           [:h1 "BBS"]
           (for [comment comments]
             [:div
              [:p (:id comment) ": " (:name comment)]
              [:p (:description comment)]])
           (if (some #(= % "WRITE_COMMENT") (:permissions identity))
             [:form {:method "post" :action ""}
              [:div {:dangerouslySetInnerHTML {:__html (anti-forgery-field)}}]
              [:p "Name:"
               [:input {:type "text" :name "comment[name]"
                        :value (:id identity)}]]
              [:p [:textarea {:name "comment[description]"}]]
              [:p [:button {:type "submit"} "Post"]]])

           [:form {:method "post" :action "/my/signOut"}
            [:button {:type "submit"} "Sign out"]]]])))

    (POST "/" [comment]
      (tables/post-comment db (assoc comment :posted_at (Timestamp. (System/currentTimeMillis))))
      (redirect "/bbs/"))))
