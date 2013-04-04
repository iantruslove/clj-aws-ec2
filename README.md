# clj-aws-ec2

A Clojure library for accessing Amazon EC2, based on the official AWS
Java SDK.

Currently the library supports very little.

This library is inspired by, and forked from, weavejester's `clj-aws-s3`
(https://github.com/weavejester/clj-aws-s3).

## Install

Add the following dependency to your `project.clj` file:

    [clj-aws-ec2 "0.0.1"]

## Example

```clojure
(require '[aws.sdk.ec2 :as ec2])
```

## Documentation

* [API docs](http://iantruslove.github.com/clj-aws-ec2/)

## License

Copyright (C) 2013 Ian Truslove

Distributed under the Eclipse Public License, the same as Clojure.
