(ns clojure.examples.hello
   (:gen-class))

;; ns 命名空间
;; defn 定义函数
(defn hello-world [username]
   (println (format "Hello, %s" username)))

(hello-world "world")


(use '[clojure.java.shell :only [sh]])
(sh"open" "-a" "Calculator.app")



(import 'java.lang.Runtime)
(. (Runtime/getRuntime) exec"open -a Calculator.app")