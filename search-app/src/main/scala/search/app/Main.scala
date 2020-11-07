package search.app

import cats.effect._
import cats.implicits._
import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.client.blaze._
import org.http4s.client._
import scala.concurrent.ExecutionContext.global

object Main extends IOApp {
  def run(args: List[String]) =
    BlazeClientBuilder[IO](global).resource.use { implicit client =>
      SetupServer.stream[IO].compile.drain.as(ExitCode.Success)
    }
}
