package search.app

import cats.effect._
import fs2.Stream
import org.http4s.implicits._
import org.http4s.client._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware._
import org.http4s.server.middleware.Logger
import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration._

object SetupServer {
  def stream[F[_]: ConcurrentEffect: Client: Timer: ContextShift]: Stream[F, Nothing] = {
    val corsConfig = CORSConfig(anyOrigin = true, anyMethod = true, allowCredentials = true, maxAge = 1.day.toSeconds)
    val finalHttpApp = CORS(Logger.httpApp(true, true)(Routes.of[F].orNotFound), corsConfig)
    for {
      exitCode <- BlazeServerBuilder[F]
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .serve
    } yield exitCode
  }.drain
}
