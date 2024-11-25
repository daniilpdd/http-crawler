package org.daniilpdd.crawler.routes

import org.daniilpdd.crawler.service.CrawlerService
import zio.http.{Method, Request, Response, Routes, Status, handler}

object CrawlerRoutes {
  def routes() = Routes(
    Method.POST / "api" / "urls" -> handler { req: Request =>
      (for {
        urls <- req.body.asString.map(_.split('\n').map(_.trim).filter(_.nonEmpty).distinct)
        res <- CrawlerService.getTitles(urls)
      } yield Response.text(res.mkString("\n"))).orElseFail({
        Response.error(Status.InternalServerError)
      })
    }
  )

}