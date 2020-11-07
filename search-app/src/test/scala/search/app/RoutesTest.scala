package search.app

import cats.effect.IO
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.implicits._
import org.http4s.twirl._

final class RoutesTest extends BaseTest {
  """GET -> Root / query""" in {
    check[String](
      service.orNotFound
        .run(
          Request(method = Method.GET, uri = Uri.uri("/tofu"))
        ),
      Status.Ok,
      None
    )
  }
}
