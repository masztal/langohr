;; Copyright (c) 2011 Michael S. Klishin
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns langohr.confirm
  (:import (com.rabbitmq.client Channel AMQP$Confirm$SelectOk ConfirmListener)))


;;
;; API
;;


(defn listener
  [^clojure.lang.IFn ack-handler, ^clojure.lang.IFn nack-handler]
  (reify ConfirmListener
    (handleAck [this, delivery-tag, multiple]
      (ack-handler delivery-tag multiple))
    (handleNack [this, delivery-tag, multiple]
      (nack-handler delivery-tag multiple))))


(defn add-listener
  [^Channel channel, ^ConfirmListener cl]
  (.setConfirmListener channel cl)
  cl)


(defn select
  ([^Channel channel]
     (.confirmSelect channel))
  ([^Channel channel, ^clojure.lang.IFn ack-handler, ^clojure.lang.IFn nack-handler]
     (let [select-ok (.confirmSelect channel)
           cl        (listener ack-handler nack-handler)]
       (.setConfirmListener channel cl)
       select-ok)))