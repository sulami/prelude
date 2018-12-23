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
        tail (-> idx inc (drop coll) vec)]
    (reduce into [head [elm] tail])))

(defn insert-in
  "Inserts elm into coll at idx, overwriting whatever was there before."
  [elm idx coll]
  (let [head (take idx coll)
        tail (-> idx inc (drop coll))]
    (concat head (list elm) tail)))

;; Maths

(defn sdiv [x y]
  "Safe division. 1 if x == y, 0 if y == 0."
  (cond
    (= x y) 1
    (zero? y) 0
    :else (/ x y)))

(defn distance
  "Distance between two numbers."
  [x y]
  (Math/abs (- x y)))

(defn avg [coll]
  "Average of a collection."
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
       (pmap (partial attach-score score-fn))
       (sort-by second)
       first
       first))

(defn evolution
  "Generator for generations."
  [base mutator score-fn gen-size]
  (iterate #(generation % mutator score-fn gen-size) base))
