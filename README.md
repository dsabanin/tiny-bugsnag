# tiny-bugsnag 

A Clojure library designed to send exception reports to [BugSnag](http://bugsnag.com).

## Usage

```clojure
(bugsnag/setup! bugsnag-key "production")

;; simplest form
(bugsnag/notify :exception exc)

;; more options
(bugsnag/notify :context "Some Context"
                :exception exc
                :data {"Optional Tab" {"Key" "Value"}}
                :user optional-user-id
                :severity "warning")
```

## Install

[![Clojars Project](http://clojars.org/tiny-bugsnag/latest-version.svg)](http://clojars.org/tiny-bugsnag)

## License

Copyright © 2014 Dima

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
