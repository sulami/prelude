(ns sulami.prelude
  (:require [clojure.pprint :refer [pprint]]))

;; Development

(defn dpr
  "Prints something and returns it."
  [x]
  (prn x)
  x)

(defn dppr
  "Pretty-prints something and returns it."
  [x]
  (pprint x)
  x)

;; Control Flow

(defn update-when
  "Update value with f if pred, otherwise return value."
  [value pred f]
  (if pred (f value) value))

;; Collections

(defn in?
  "True if coll contains element.
  Contrary to `contains?`, this works for lists."
  [coll element]
  (some #(= element %) coll))

(defn insert-at
  "Inserts elm into coll at idx, overwriting whatever was there before."
  [elm idx coll]
  (let [head (vec (take idx coll))
        tail (-> idx (+ 1) (drop coll) vec)]
    (reduce into [head [elm] tail])))

(defn insert-in
  "Inserts elm into coll at idx, overwriting whatever was there before."
  [elm idx coll]
  (let [head (take idx coll)
        tail (drop idx coll)]
    (concat head (list elm) tail)))

;; Maths

(defn distance
  "Distance between two numbers."
  [x y]
  (Math/abs (- x y)))

(defn avg [coll]
  (/ (apply + coll) (count coll)))

(def fibonacci
  "Lazy generator of the fibonacci sequence.
  Probably not the fastest implementation."
  (->> [1 2]
       (iterate (fn [[x y]] [y (+' x y)]))
       (map first)))

(defn factors
  "Returns all the factors for n."
  [n]
  (->> (range 1 n)
       (filter #(zero? (mod n %)))))

(defn prime?
  "Checks if n is prime."
  [n]
  (->> (range 2 n)
       (filter #(zero? (mod n %)))
       empty?))

(defn prime-factors
  "Finds all prime factors of n."
  [n]
  (->> (factors n)
       (filter prime?)))

(def primes
  "Lazy generator of prime numbers."
  (filter prime? (range)))

(defn clamp
  "Clamps a value to boundaries."
  [x lower upper]
  (-> x
      (max lower)
      (min upper)))

(defn clamp-f
  "Modifies a function to clamp the result."
  [f lower upper]
  (fn [& args]
    (-> f
        (apply args)
        (clamp lower upper))))

;; Monte Carlo

(defn monte-carlo
  "Find a result via Monte Carlo approximation."
  [sample-fn eval-fn sample-size]
  (->> sample-fn
       (repeatedly sample-size)
       eval-fn))

;; Genetic Programming

(defn mutate
  "Generator that mutates a base, the first element being the base."
  [base mutator]
  (concat [base]
          (repeatedly #(mutator base))))

(defn attach-score
  "Attaches the score to a specimen."
  [score-fn specimen]
  [specimen (score-fn specimen)])

(defn generation
  "Picks out the best one from a generation."
  [base mutator score-fn gen-size]
  (->> (mutate base mutator)
       (take gen-size)
       (map (partial attach-score score-fn))
       (sort-by second)
       first
       first))

(defn evolution
  "Generator for generations."
  [base mutator score-fn gen-size]
  (iterate #(generation % mutator score-fn gen-size) base))

;; Example to approximate the square root of 2.
;; (def base 0.0)
;; (defn mutator [base]
;;   (-> (rand)
;;       (- 0.5)
;;       (+ base)))
;; (defn score-fn [x]
;;   (-> x
;;       (- (Math/sqrt 2))
;;       Math/abs))
;; (take 10 (evolution base mutator score-fn 10))

;; Build a pipeline

;; (def base '(->))
;; (defn mutator [base]
;;   (let [a (rand-nth [+ - * quot])
;;         b (+ 1 (rand-int 10))]
;;     (concat base (list (list a b)))))

;; (defn proto [x]
;;   (-> x (+ 5) (quot 2) (- 1) (* 3)))

;; (def tests
;;   [[-6 (proto -6)]
;;    [-1 (proto -1)]
;;    [0 (proto 0)]
;;    [1 (proto 1)]
;;    [2 (proto 2)]
;;    [5 (proto 5)]
;;    [7 (proto 7)]
;;    [10 (proto 10)]
;;    [100 (proto 100)]])

;; (defn score-fn [x]
;;   (->> (map
;;         (fn [[i o]]
;;           (-> (insert-in i 1 x)
;;               eval
;;               (distance o)))
;;         tests)
;;        avg))

;; (->> (mutate base mutator)
;;      (take 10))
;; (generation base mutator score-fn 10)
;; (nth (evolution base mutator score-fn 10) 20)
;; (def t (nth (evolution base mutator score-fn 10) 25))
;; t

;; (let [f (nth (evolution base mutator score-fn 20) 25)]
;;   [(score-fn f) f])
;; (-> (insert-in 1 1 t)
;;     eval)
