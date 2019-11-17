# People

A collection of libraries for working with people data. 

The libraries were split up in order to allow each one to focus on
their individual concerns.

## person

Provide general functionality for parsing and sorting people data.

## person-cli

Provides a cli based interface for reading and sorting files
containing people data.

## person-api

Provides an api interface and storage mechanism for people data.

# Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

# Local Setup

Using checkouts folders is the easiest way to work with the separate
projects. The [Leiningen guide](https://github.com/technomancy/leiningen/blob/stable/doc/TUTORIAL.md)
contains a detailed outline of how to use checkouts. Please refer to
the guide if the below instructions don't work.

```bash
# install the person library
$ cd person
$ lein install

# setup the cli application
$ cd ../people-cli
$ mkdir checkouts
$ cd checkouts
$ ln -s ../../person person

# setup the api application
$ cd ../people-api
$ mkdir checkouts
$ cd checkouts
$ ln -s ../../person person
```

After following the above steps, you can start the cli and api
applications by executing `lein run` in the corresponding folder.

Each project contains it's own coverage report. To get the coverage,
navigate to the desired project and execute `lein cloverage`.
