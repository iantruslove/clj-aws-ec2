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
           com.amazonaws.services.ec2.model.SpotPrice
           com.amazonaws.services.ec2.model.DescribeSpotPriceHistoryRequest)
  (:require [clj-time.format :as format]
            [clj-time.coerce :as coerce]))

(defprotocol Mappable
             "Convert a value into a Clojure map."
             (to-map [x] "Return a map of the value."))

(defn date-to-string
  "Converts a java.util.Date to a nice ISO 8601 time"
  [date]
  (format/unparse (format/formatters :date-time)
                  (coerce/from-date date)))

(defn string-to-date
  "Converts a nice ISO 8601 time to a java.util.Date"
  [iso]
  (coerce/to-date (format/parse (format/formatters :date-time) iso)))

(extend-protocol Mappable
  SpotPrice
  (to-map [spot-price]
    {:availability-zone   (.getAvailabilityZone spot-price)
     :instance-type       (.getInstanceType spot-price)
     :product-description (.getProductDescription spot-price)
     :spot-price          (.getSpotPrice spot-price)
     :timestamp           (date-to-string (.getTimestamp spot-price))}))

(defn- get-region
  "Returns the AWS region corresponding to the keyword or string passed"
  [region]
  (Region/getRegion
   (Regions/valueOf (name region))))

(defn- with-credentials [cred]
  (let [{:keys [access-key secret-key]} cred]
    (BasicAWSCredentials. access-key secret-key)))

(defn- create-client*
  "Create an EC2 Client for a specific region."
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

(defn get-spot-price-history-request
  [params]
  (let [request (DescribeSpotPriceHistoryRequest.)]
    (doseq [[k v] params]
      (cond
       (= :availability-zone k) (.setAvailabilityZone request v)
       (= :max-results k)       (.setMaxResults request (int v))
       (= :start-time k)        (.setStartTime request (string-to-date v))
       (= :end-time k)          (.setEndTime request (string-to-date v))
       (= :next-token k)        (.setNextToken request v)
       ))
    request))

(defn describe-spot-price-history
  "params can contain:
   :availability-zone
   :end-time
   :max-results
   :start-time"
  ([cred region]
     (describe-spot-price-history cred region {}))
  ([cred region params]
     (let [client (create-client cred region)]
       (.describeSpotPriceHistory client (get-spot-price-history-request params)))))
