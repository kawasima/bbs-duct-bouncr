(ns bbs-duct.boundary.tables
  (:require [clojure.java.jdbc :as jdbc]
            [duct.database.sql]))

(defprotocol Tables
  (get-comments [db])
  (post-comment [db record]))

(extend-protocol Tables
  duct.database.sql.Boundary
  (get-comments [{:keys [spec]}]
    (jdbc/query spec ["SELECT id, name, description, posted_at FROM comments ORDER BY posted_at DESC LIMIT 10"]))
  (post-comment [{:keys [spec]} record]
    (jdbc/insert! spec :comments (select-keys record [:name :description :posted_at]))))
