(ns aws.sdk.ec2_test
  (:use clojure.test)
  (:import com.amazonaws.regions.Regions
           com.amazonaws.regions.Region )
  (:require [aws.sdk.ec2 :as ec2]))

(def us_east_1 (Region/getRegion Regions/US_EAST_1))

(deftest ec2-test
  (testing "regions are accessible with keywords"
    (let [get-region #'ec2/get-region]
      (is (= us_east_1 (get-region :US_EAST_1)))))
  )
