package org.daniilpdd.crawler

import org.daniilpdd.crawler.routes.CrawlerRoutes
import org.daniilpdd.crawler.routes.wrapper.ResponseWrapper
import org.daniilpdd.crawler.service.client.RequestService
import zio.http.{Client, Server}
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}

object MainApp extends ZIOAppDefault {

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =
    Server
      .serve(CrawlerRoutes.routes.handleErrorZIO(CrawlerRoutes.handleError))
      .provide(
        Server.defaultWithPort(8080),
        RequestService.live,
        Client.default,
        ResponseWrapper.json
      )
}