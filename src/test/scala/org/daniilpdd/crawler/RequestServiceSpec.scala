package org.daniilpdd.crawler

import org.daniilpdd.crawler.service.client.RequestService
import zio.http.URL
import zio.test._
import zio.{Scope, ZIO}

object RequestServiceSpec extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment with Scope, Any] = suite("RequestServiceSpec")(
    test("RequestServiceIdentical returns URL instead of title") {
      val url = "https://test.org"
      for {
        testURL <- ZIO.fromEither(URL.decode(url))
        res <- RequestService.getTitle(testURL)
      } yield assertTrue(url == res)
    }
  ).provide(RequestService.identical)
}
