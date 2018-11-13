(ns schlachtschiff)

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
