(ns dactyl.2d.cherry
  (:require [analemma.svg :as svg]))

;; Not to scale!
;;
;;    --------tw--------
;;    smh              -
;; -pw-                ----
;; ph                     -
;; ----                ----
;;    |                -
;;    pgh              -
;;    |                -
;; ----                ----
;; -                      -  
;; ----                ----
;;    -                -
;;    --------tw--------

(def tw 49.5)     ;; Top width
(def smh 3.474)   ;; Side margin height
(def pw 2.88)     ;; Peg width
(def ph 12.402)   ;; Peg height
(def pgh 17.748)  ;; Peg gap height

(def keyswitch-height (+ smh ph pgh ph smh))
(def keyswitch-width (+ pw tw pw))

(defn- flip-path [points] (map (partial map -) points))

(def keyswitch
  (let [left-bottom-path [[0 smh]
                          [(- pw) 0] [0 ph] [pw 0]
                          [0 pgh]
                          [(- pw) 0] [0 ph] [pw 0]
                          [0 smh]
                          [tw 0]]]
    (svg/path
     (cons :M
           (interpose :l
                      (concat [[pw 0]]
                              left-bottom-path
                              (flip-path left-bottom-path)))))))
