package examples

import automorph.codec.json.JacksonJsonCodec
import automorph.protocol.JsonRpcProtocol
import automorph.system.FutureSystem
import automorph.transport.http.server.{JettyServer, NanoServer}
import automorph.{Default, RpcServer}
import automorph.codec.json.CirceJsonCodec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

// Create server implementation of the remote API
class ApiImpl:
  def hello(some: String, n: Int): Future[String] =
    Future(s"Hello $some $n!")


@main def rpcServer(): Unit =
  val api = ApiImpl()

  val nanoServer = JettyServer(
    effectSystem = FutureSystem(),
    port = 9000,
    pathPrefix = "/api",
    webSocket = false
  )

//  val server = RpcServer(nanoServer, JsonRpcProtocol(CirceJsonCodec()))
  val server = Default.rpcServer(9000, "/api")

  // Expose the server API implementation to be called remotely
  val apiServer = server.bind(api)

  Await.ready(
    for
      // Start the JSON-RPC server
      activeServer <- apiServer.init()

      // let it run indefinitively
      never <- Future.never
    yield (),
    Duration.Inf
  )
