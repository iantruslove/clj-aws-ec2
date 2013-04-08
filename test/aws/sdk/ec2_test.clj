(ns aws.sdk.ec2_test
  (:use clojure.test
        [aws.sdk.ec2])
  (:import com.amazonaws.regions.Regions
           com.amazonaws.regions.Region
           com.amazonaws.services.ec2.model.SpotPrice
           com.amazonaws.services.ec2.model.InstanceType
           java.util.Date)
  (:require [aws.sdk.ec2 :as ec2]))

(def us_east_1 (Region/getRegion Regions/US_EAST_1))

(deftest private-helpers
  (testing "regions are accessible with keywords"
    (let [get-region #'ec2/get-region]
      (is (= us_east_1 (get-region :US_EAST_1)))))

  (testing "creation of SpotPriceHistoryRequests"
    (let [get-spot-price-history-request #'ec2/get-spot-price-history-request
          params {
                  :availability-zone "us-east-1a"
                  :end-time "2010-01-03T00:00:00.000Z"
                  ;:filters
                  ;:instance-types
                  :max-results 10
                  :next-token "ahPY8+qAPr" 
                  ;:product-descriptions "some description"
                  :start-time "2010-01-02T00:00:00.000Z"
                  }
          spotPriceHistoryRequest (get-spot-price-history-request params)]
      (is (= (:availability-zone params)    (.getAvailabilityZone spotPriceHistoryRequest)))
      (is (= (:end-time params)             (date-to-string (.getEndTime spotPriceHistoryRequest))))
      (is (= (:max-results params)          (.getMaxResults spotPriceHistoryRequest)))
      (is (= (:start-time params)           (date-to-string (.getStartTime spotPriceHistoryRequest))))
      (is (= (:next-token params)           (.getNextToken spotPriceHistoryRequest)))
      )))

(deftest mappables
  (testing "SpotPrice is mappable"
    (let [spot-price (doto (SpotPrice.)
                       (.setAvailabilityZone "zone")
                       (.setInstanceType InstanceType/T1Micro)
                       (.setProductDescription "linux")
                       (.setSpotPrice "1.234")
                       (.setTimestamp (Date. 0)))
          expected-map {:availability-zone "zone"
                        :instance-type "t1.micro"
                        :product-description "linux"
                        :spot-price "1.234"
                        :timestamp "1970-01-01T00:00:00.000Z"
                        }]
      (is (= expected-map (to-map spot-price))))))
