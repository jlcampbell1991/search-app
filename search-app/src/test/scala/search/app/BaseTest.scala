package search.app

import cats.data._
import cats.effect._
import cats.effect.{Blocker, IO}
import cats.implicits._
import org.http4s._
import org.http4s.client._
import org.http4s.server._
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import java.util.concurrent._
import scala.concurrent.ExecutionContext.global

abstract class BaseTest extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with TypeCheckedTripleEquals {
  val service = {
    val blockingPool = Executors.newFixedThreadPool(5)
    val blocker = Blocker.liftExecutorService(blockingPool)
    implicit val cs: ContextShift[IO] = IO.contextShift(global)
    implicit val client: Client[IO] = JavaNetClientBuilder[IO](blocker).create
    Routes.of[IO]
  }

  def check[A](actual: IO[Response[IO]], expectedStatus: org.http4s.Status, expectedBody: Option[A])(
      implicit ev: EntityDecoder[IO, A]
  ): Assertion = {
    val actualResp = actual.unsafeRunSync
    val bodyCheck = expectedBody match {
      case Some(_) =>
        expectedBody.fold[Boolean](actualResp.body.compile.toVector.unsafeRunSync.isEmpty)(expected =>
          actualResp.as[A].unsafeRunSync == expected
        )
      case None => true
    }
    assert(actualResp.status == expectedStatus)
  }
}
