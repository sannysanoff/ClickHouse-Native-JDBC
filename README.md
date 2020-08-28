
# ClickHouse-Native-JDBC

This is a FORK of original ClickHouse-Native-JDBC found on github and written in java. 
For readme of original package, see previous version of this README.

The JDBC keyword in a project name is a lie. No JDBC here.

Goal of fork is:

- to enable async io in kotlin, not using plain old sockets
- to speed up data access to maximum degree.

Changes in this fork:

- moved from java to kotlin
- completely async/suspend with async sockets from ktor.
- optimized quite a few places for speed / low GC pressure
- added array access of incoming batch, instead of slower row by row access.
- some functionality has not been ported completely or is sub-optimal. In particular, array types may be missing
- some basic types exc strings/symbols, doubles, ints may be sub-optimal.

