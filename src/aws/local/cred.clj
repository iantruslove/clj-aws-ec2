(ns aws.local.cred
  (:use [environ.core :as env]))

(def aws-creds {:access-key (env/env :aws-access-key)
                :secret-key (env/env :aws-secret-key)})
