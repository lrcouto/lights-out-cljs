(ns lights.views
  (:require
   [re-frame.core :as re-frame]
   [lights.subs :as subs]
   [lights.events :as events]))

(defn main-panel []
  (let [current-board (re-frame/subscribe [::subs/board])]
    [:div
     [:h1
      "Lights Out!"]
     [:button {:on-click #(re-frame/dispatch [::events/generate-new-board])} "Generate New Board"]
     (when (not-empty @current-board) @current-board)]))

;; TODO: 
;; Calculate adjacents for light switching.
;; Verify victory condition.
;; Create randomized starting condition.
;; Allow for the player to pick the size of the board.
;; Make it look a little better