package org.daniilpdd.crawler.routes

import org.daniilpdd.crawler.service.CrawlerService
import org.daniilpdd.crawler.service.client.RequestService
import zio.ZIO
import zio.http.codec.TextCodec.StringCodec
import zio.http.{Method, Request, Response, Routes, Status, URL, handler}

object CrawlerRoutes {
  def routes(): Routes[RequestService, Nothing] = Routes(
    Method.POST / "api" / "urls" -> handler { req: Request =>
      (for {
        urlsStr <- req.body.asString.map(_.split('\n').map(_.trim).filter(_.nonEmpty).distinct)
        urls <- ZIO.foreach(urlsStr)(u => ZIO.fromEither(URL.decode(u)))
        _ <- ZIO.logDebug(urls.mkString("(", ",", ")"))
        _ <- ZIO.logInfo(s"urls size: ${urls.length}")
        res <- CrawlerService.getTitles(urls)
      } yield Response.text(res.mkString("\n"))).orElseFail({
        Response.error(Status.InternalServerError)
      })
    },
    Method.GET / "api" / "urls" -> handler { req: Request =>
      (for {
        queryParam <- req.queryParamToZIO("urls")(StringCodec)
        encodedUrls <- ZIO.attempt(queryParam.split(',').map(_.trim).filter(_.nonEmpty))
        urls <- ZIO.foreach(encodedUrls)(u => ZIO.fromEither(URL.decode(u)))
        _ <- ZIO.logDebug(urls.mkString("(", ",", ")"))
        _ <- ZIO.logInfo(s"urls size: ${urls.length}")
        res <- CrawlerService.getTitles(urls)
      } yield Response.text(res.mkString("\n"))).orElseFail({
        Response.error(Status.InternalServerError)
      })
    }
  )

}