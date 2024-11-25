package org.daniilpdd.crawler.api

import org.daniilpdd.crawler.client.RequestService
import zio.Console.ConsoleLive
import zio.http.{Method, Request, Response, Routes, Status, handler}
import zio.{Fiber, ZIO}

object CrawlerApi {
  def routes() = Routes(
    Method.POST / "api" / "urls" -> handler { req: Request =>
      (for {
        urls <- req.body.asString.map(_.split('\n').map(_.trim).filter(_.nonEmpty))
        titlesFibers <- ZIO.foreach(urls)(RequestService.getTitle(_).fork)
        titles <- Fiber.collectAll(titlesFibers.toSeq).join
      } yield Response.text(titles.mkString("\n"))).orElseFail({
        Response.error(Status.InternalServerError)
      })
    }
  )

}