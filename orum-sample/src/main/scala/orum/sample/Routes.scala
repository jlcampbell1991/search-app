package orum.sample

import cats.effect.Sync
import cats.implicits._
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.client._
import org.http4s.dsl.Http4sDsl
import org.http4s.HttpRoutes
import org.http4s.Uri._

object Routes {
  def of[F[_]: Sync: Client]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._

    HttpRoutes.of {
      case GET -> Root / query => Ok(Req(query).call)
    }
  }
}
