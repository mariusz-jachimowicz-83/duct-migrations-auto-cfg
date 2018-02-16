# duct-migrations-auto-cfg
Duct module that automatically fill in ragtime migrations configuration based on folder with migrations files.  

It won't work if you have already applied migrations that was specified manually because during migrations load ragtime will assign ids for migrations using default mechanism - your migrations might have different ids already.


[![CircleCI](https://circleci.com/gh/mariusz-jachimowicz-83/duct-migrations-auto-cfg.svg?style=svg)](https://circleci.com/gh/mariusz-jachimowicz-83/duct-migrations-auto-cfg)

## Installation

[![Clojars Project](https://img.shields.io/clojars/v/com.mjachimowicz/duct-migrations-auto-cfg.svg)](https://clojars.org/com.mjachimowicz/duct-migrations-auto-cfg)

## Usage

To add this module to your configuration, add the `:duct-migrations-auto-cfg/module` configuration before `:duct.module/sql`:

```clojure
{:duct-migrations-auto-cfg/module {}
 :duct.module/sql {:database-url "jdbc:sqlite:"}}
```

It will fill in migrations from `migrations` or `resources/migrations` folder

Or

```clojure
{:duct.core/project-ns my-project
 :duct-migrations-auto-cfg/module {}
 :duct.module/sql {:database-url "jdbc:sqlite:"}}
```

It will fill in migrations from `my-project/migrations` or `resources/my-project/migrations` folder

Or you can specify custom path

```clojure
{:duct-migrations-auto-cfg/module {:migrations-path "my_custom_path"}
 :duct.module/sql {:database-url "jdbc:sqlite:"}}
```

It will fill in migrations from your custom path

## License

Copyright © 2018 Mariusz Jachimowicz

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
