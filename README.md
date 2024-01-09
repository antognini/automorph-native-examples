# Automorph examples with GraalVM native images

* Make sure you have GraalVM JDK 21 or higher available in `~/graalvm`
* Make sure your `PATH` includes `~/graalvm/bin`
* run `sbt nativeImages`
* run `./rpcServer/target/native-image/rpcServer`
* run `./rpcClient/target/native-image/rpcClient` (in another shell)

```
$ ./rpcServer/target/native-image/rpcServer
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
```

```
$ ./rpcClient/target/native-image/rpcClient
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
Hello world 1!
Hello world 1!
$
```

### TODO
* make SBT plugin download native-image ver 21 via Coursier (currently does not work)
* make Undertow server work natively (currently runs on Nano server)
