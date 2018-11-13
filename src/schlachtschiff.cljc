(ns schlachtschiff
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

;; Maps

(defn set-in
  "Like update in, but just sets."
  [m ks v]
  (update-in m ks (constantly v)))

;; Maths

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
