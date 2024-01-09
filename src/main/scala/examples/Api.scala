package examples

import scala.concurrent.Future

// Define a remote API
trait Api {
  def hello(some: String, n: Int): Future[String]
}
