package examples


import automorph.Default
import java.net.URI
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


@main def rpcClient(): Unit =
  // Configure JSON-RPC HTTP client to send POST requests to 'http://localhost:9000/api'
  val client = Default.rpcClient(new URI("http://localhost:9000/api"))

  // Create a type-safe local proxy for the remote API from the API trait
  val remoteApi = client.bind[Api]

  Await.ready(
    for
      // Initialize the JSON-RPC client
      activeClient <- client.init()

      // Call the remote API function via the local proxy
      result <- remoteApi.hello("world", 1)
      _ = println(result)

      // Call the remote API function dynamically without an API trait
      result <- activeClient.call[String]("hello")("some" -> "world", "n" -> 1)
      _ = println(result)

      // Close the JSON-RPC client
      _ <- activeClient.close()
   yield (),
   Duration.Inf
  )

