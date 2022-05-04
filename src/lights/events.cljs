(ns lights.events
  (:require
   [re-frame.core :as re-frame]
   [lights.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]))

(defn square [i j]
  [:rect {:key [i j]
          :height 0.9
          :width 0.9
          :fill "#E99504"
          :x i
          :y j
          :on-click #(re-frame/dispatch [::switch-light-status i j])}])

(defn board [size]
  (let [square-sequence (for [i (range size) j (range size)] (square i j))]
    [:div
     (into [:svg
            {:view-box (str "0 0 " size " " size)
             :width 500
             :height 500}]
           square-sequence)]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::generate-new-board
 (fn-traced [db]
             (let [size (:size db)
                   grid (vec (repeat size (vec (repeat size true))))
                   new-board (board size)]
               (-> db
                   (assoc-in [:light-status] grid)
                   (dissoc :board)
                   (assoc-in [:board] new-board)))))

(re-frame/reg-event-db
 ::switch-light-status
 (fn-traced [db params]
            (let [x (get-in params [1])
                  y (get-in params [2])
                  light-status (get-in db [:light-status x y])
                  board-elements (get-in db [:board 1])
                  relevant-square (first (filter (fn [element]
                                                   (let [key (get-in element [1 :key])
                                                         key-x (get-in key [0])
                                                         key-y (get-in key [1])]
                                                     (and (= x key-x) (= y key-y)))) board-elements))
                  square-index (.indexOf board-elements relevant-square)]
              (if (true? light-status)
                (-> db 
                    (assoc-in [:light-status x y] false)
                    (assoc-in [:board 1 square-index 1 :fill] "#25253A"))
                (-> db 
                    (assoc-in [:light-status x y] true)
                    (assoc-in [:board 1 square-index 1 :fill] "#E99504"))))))
