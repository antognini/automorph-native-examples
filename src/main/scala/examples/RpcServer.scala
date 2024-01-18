package examples

import automorph.{Default, RpcServer}
import automorph.codec.json.{CirceJsonCodec, JacksonJsonCodec}
import automorph.protocol.JsonRpcProtocol
import automorph.system.ZioSystem
import automorph.transport.http.server.UndertowServer
import zio.{Task, Unsafe, ZIO}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

// Create server implementation of the remote API
class ApiImpl:
  def hello(some: String, n: Int): Task[String] =
    ZIO.succeed(s"Hello $some $n!")

@main def rpcServer(): Unit =
  val api = ApiImpl()

//  val server = Default.rpcServerCustom(ZioSystem.default, 9000, "/api")
  val server = RpcServer(
    UndertowServer(effectSystem = ZioSystem.default, 9000, "/api"),
    JsonRpcProtocol(JacksonJsonCodec())
  )

  // Expose the server API implementation to be called remotely
  val apiServer = server.bind(api)

  Unsafe.unsafe{ implicit unsafe =>
    ZioSystem.defaultRuntime.unsafe.run(
      for
        // Start the JSON-RPC server
        activeServer <- apiServer.init()

        // let it run indefinitively
        never <- ZIO.none
      yield ()
    )
  }