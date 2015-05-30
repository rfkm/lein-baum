# lein-baum

A tiny Leiningen plugin to embed a piece of config in an external file
into your project.clj by using [Baum](https://github.com/rkworks/baum).

## Usage

Put `[rkworks/lein-baum "0.1.0-SNAPSHOT"]` into the `:plugins` vector
of your project.clj. Then, add `:baum-path` key to specify which file
Baum should read. Finally, you can now embed a part of the specified
config file by using `^:baum/get-in`:

```clj
:foo ^:baum/get-in [:key1 :key2]
```

## Experimental feature

lein-baum has an experimental support for project map transformation
using Baum's DSL. For more details, please see `example/project.clj`.

**NB:** Since there is no way to use custom reader macros in
 `project.clj`, you need to use special syntax to use Baum's reader
 macros like the following:

```clj
#baum/env :port
;; ↓ ↓ ↓
{baum/env :port}
```

## Limitation

Your config file will be evaluated in the context of Leiningen, so
some features of Baum does not work properly. You cannot also use
custom readers or reducers because you cannot pass options to Baum's
reader.

## License

Copyright © 2015 Ryo Fukumuro

Distributed under the Eclipse Public License, the same as Clojure.
