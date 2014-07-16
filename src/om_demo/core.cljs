(ns om-demo.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

(def app-state (atom {:active 0
                      :tabs
                      [{:title "First" :class "dark-blue" :view "First View"}
                       {:title "Second" :class "red" :view "Second View"}
                       {:title "Third" :class "light-blue" :view "Third View"}]}))

(defn tab-bar-view [view owner]
  (dom/div #js {:className "tab-view"} view))

(defn tab [tab app index]
  (let [className (str (:class tab) " tab " (if (= index (:active app)) "active" "" ))]
    (dom/div #js {:onClick (fn [e] (om/update! app :active index))
                  :className className} (:title tab))))

(defn tab-bar-component [app owner]
  (reify
    om/IRender
    (render [this]
      (dom/div nil
        (apply dom/div #js {:className "tab-bar"}
          (map tab (:tabs app) (iterate identity app) (range)))
        (om/build tab-bar-view (:view (nth (:tabs app) (:active app))))))))


(om/root
  tab-bar-component
  app-state
  {:target (. js/document (getElementById "app"))})
