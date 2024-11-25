package org.daniilpdd.crawler

import org.daniilpdd.crawler.api.CrawlerApi
import org.daniilpdd.crawler.client.RequestService
import zio.Console.ConsoleLive
import zio.http.{Client, Request, Server, URL}
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}

object MainApp extends ZIOAppDefault {

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =
    Server.serve(CrawlerApi.routes()).provide(Server.default, RequestService.live, Client.default)
}


object SimpleClient extends ZIOAppDefault {
  val url = URL.decode("https://jsonplaceholder.typicode.com/todos").toOption.get

  val program = for {
    client <- ZIO.service[Client]
    res    <- client.url(url).batched(Request.get("/"))
    data   <- res.body.asString
    _      <- ConsoleLive.printLine(data)
  } yield ()

  override val run = program.provide(Client.default)

}