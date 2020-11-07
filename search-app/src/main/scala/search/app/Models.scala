package search.app

import cats.effect.Sync
import cats.implicits._
import io.circe._, io.circe.generic.semiauto._
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.client._
import org.http4s.Uri
import org.http4s.Uri._

case class Req(value: String) {
  def call[F[_]: Sync](implicit client: Client[F]): F[Resp] =
    client.expect[Resp](
      Uri(
        scheme = Some(Scheme.https),
        authority = Some(Authority(host = RegName("api.duckduckgo.com"))),
        path = "/",
        query = Query(
          ("q", Some(this.value)),
          ("format", Some("json"))
        )
      )
    )
}

case class Resp(
    AbstractURL: String,
    AbstractSource: String
)

object Resp {
  implicit val decoder: Decoder[Resp] = deriveDecoder
  implicit val encoder: Encoder[Resp] = deriveEncoder
}
