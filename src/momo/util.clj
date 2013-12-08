(ns momo.util)

(defn keywordify-keys
  [m]
  (into {}
        (for [[k v] m]
          [(keyword k) v])))
