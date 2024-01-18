package examples


import automorph.codec.json.{JacksonJsonCodec, JacksonJsonRpc}
import automorph.protocol.JsonRpcProtocol
import automorph.{Default, RpcClient}
import automorph.system.IdentitySystem
import automorph.transport.http.client.SttpClient.Context
import automorph.transport.http.client.{SttpClient, UrlClient}
import sttp.client3.{HttpClientSyncBackend, HttpURLConnectionBackend, Identity, SttpBackend}

import java.net.URI
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


@main def rpcClient(): Unit =
  // Configure JSON-RPC HTTP client to send POST requests to 'http://localhost:9000/api'

  val backend = HttpClientSyncBackend()
  val clientTransport = SttpClient.http(IdentitySystem(), backend,  new URI("http://localhost:9000/api"))

  val client = RpcClient(
    clientTransport,
    JsonRpcProtocol(JacksonJsonCodec())
  )
  // Create a type-safe local proxy for the remote API from the API trait
  val remoteApi = client.bind[ApiClient]

  // Call the remote API function via the local proxy
  val range = Vector.range(0, 10000)

  val activeClient = client.init()

  (0 until 20) foreach: _ =>
    val result = range.map(_ => remoteApi.hello("world", 1)).last
    println(result)

  activeClient.close()
