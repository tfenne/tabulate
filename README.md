[![License](http://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/tfenne/tabulate/blob/master/LICENSE)
[![Language](http://img.shields.io/badge/language-scala-brightgreen.svg)](http://www.scala-native.org/)

# tabulate

Simple command line utility to generate fixed-width tables from tab-separated inputs. Intended as a simple replacement
for `column -t` that is more useful for viewing data from tab-separated inputs.  Treats multiple consecutive tabs
as multiple fields (instead of collapsing them) and also ignores/suppresses empty lines and comment lines (lines
beginning with `#`).

## building

To build

1. Install [Scala Native](http://www.scala-native.org/) as per the online guide.
2. Clone the repo: `git clone https://github.com/tfenne/tabulate.git`
3. `cd tabulate && sbt nativeLink`

After which `target/scala-2.11/tabulate-out` should exist!
