(ns aws.sdk.ec2
  "Functions to access the Amazon EC2 service.

  Each function takes a map of credentials as its first argument,
  and the AWS region as the second parameter.
  The credentials map should contain an :access-key key and a :secret-key key.
  Region keys look like :US_EAST_2."
  (:import com.amazonaws.auth.BasicAWSCredentials
           com.amazonaws.services.ec2.AmazonEC2Client
           com.amazonaws.regions.Region
           com.amazonaws.regions.Regions
           com.amazonaws.services.ec2.model.SpotPrice)
  (:require [clj-time.format :as format]
            [clj-time.coerce :as coerce]))

(defprotocol Mappable
             "Convert a value into a Clojure map."
             (to-map [x] "Return a map of the value."))

(extend-protocol Mappable
  SpotPrice
  (to-map [spot-price]
    {:availability-zone (.getAvailabilityZone spot-price)
     :instance-type     (.getInstanceType spot-price)
     :product-description (.getProductDescription spot-price)
     :spot-price (.getSpotPrice spot-price)
     :timestamp (format/unparse (format/formatters :date-time) (coerce/from-date (.getTimestamp spot-price)))
     }))

(defn- get-region
  "Returns the AWS region corresponding to the keyword or string passed"
  [region]
  (Region/getRegion
   (Regions/valueOf (name region))))

(defn- with-credentials [cred]
  (let [{:keys [access-key secret-key]} cred]
    (BasicAWSCredentials. access-key secret-key)))

(defn- create-client*
  "Create an EC2 Client for a specific region.

  Region is a keyword such as :US_EAST_1, :US_WEST_2, etc."
  [cred region]
  (let [client (AmazonEC2Client. (with-credentials cred))]
    (.setRegion client (get-region region))
    client))

(def ^{:private true}
  create-client
  (memoize create-client*))

(defn describe-instances
  "List of all instances in the specified region"
  [cred region]
  (throw (UnsupportedOperationException. "Not implemented"))
  )

;; (defn describe-spot-price-history)





