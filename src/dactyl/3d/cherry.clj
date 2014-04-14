(ns dactyl.3d.cherry
  (:use [scad-clj.scad])
  (:use [scad-clj.model]))

;; All of these numbers are in the wrong units!  This is a direct translation of
;; the values from the SVG, which are in pixels (90 per inch).  SCAD uses mm, so
;; do some scaling at the end.

(def tw 49.5)     ;; Top width
(def smh 3.474)   ;; Side margin height
(def pw 2.88)     ;; Peg width
(def ph 12.402)   ;; Peg height
(def pgh 17.748)  ;; Peg gap height

(def keyswitch-depth (+ smh ph pgh ph smh))
(def keyswitch-width (+ pw tw pw))
(def plate-height 20)

(defn- flip-path [points] (map (partial map -) points))

(def keyswitch-plate-hole-shape
  (polygon [[2.88 0] [2.88 3.474] [0.0 3.474] [0.0 15.876] [2.88 15.876] [2.88 33.624] [0.0 33.624] [0.0 46.026] [2.88 46.026] [2.88 49.5] [52.38 49.5] [52.38 46.026] [55.26 46.026] [55.26 33.624] [52.38 33.624] [52.38 15.876] [55.260 15.876] [55.26 3.474] [52.38 3.474] [52.38 0]]))

(def keyswitch-plate-hole
  (->> keyswitch-plate-hole-shape
       (extrude-linear {:height plate-height :twist 0 :convexity 0})
       (translate (map #(/ (- %) 2) [keyswitch-width keyswitch-depth 0]))))

(def hole-height 25)

(def pillar-width (+ keyswitch-width 10))
(def pillar-height (+ hole-height (/ plate-height 2)))
(def pillar-depth (+ keyswitch-depth 15))

(def keyswitch-full-hole
  (->>
   (union
    keyswitch-plate-hole
    (->> (cube pillar-width (/ ph 2) (* plate-height 2))
         (translate [0 (+ (/ pgh 2) (/ ph 2)) 0]))
    (->> (cube (/ ph 2) pillar-depth (* plate-height 2))
         (translate [0 0 0]))
    
    (translate
     [0 0 (/ hole-height -2)]
     (cube keyswitch-width
           keyswitch-depth
           hole-height)))
   (translate [0 0 hole-height])))

(def full-pillar
  (->> (cube pillar-width  pillar-depth
             pillar-height)
       (translate [0 0 (/ pillar-height 2)])))

(def pillar
  (difference
   full-pillar
   keyswitch-full-hole))

(def key-height 45)

(def pillar-with-fake-key
  (union pillar
         (->> (cube (+ -0 pillar-width) (+ -0 pillar-depth) key-height)
              (translate [0 0 (+ (/ key-height 2) pillar-height 1)]))))

(def full-height (+ pillar-height key-height 1))
