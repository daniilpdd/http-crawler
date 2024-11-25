package org.daniilpdd.crawler.service.client

import zio.http.{Client, Request, URL}
import zio.macros.accessible
import zio.{Task, ZIO, ZLayer}

import scala.util.matching.Regex

@accessible
trait RequestService {
  def getTitle(url: String): Task[String]
}

object RequestService {
  val identical: ZLayer[Any, Nothing, RequestServiceIdentical] = ZLayer.fromFunction(RequestServiceIdentical.apply _)
  val live: ZLayer[Client, Nothing, RequestServiceLive] = ZLayer.fromFunction(RequestServiceLive.apply _)
}

case class RequestServiceIdentical() extends RequestService {
  override def getTitle(url: String): Task[String] = ZIO.succeed(url)
}

case class RequestServiceLive(client: Client) extends RequestService {
  override def getTitle(url: String): Task[String] = for {
    _url <- ZIO.fromEither(URL.decode(url))
    response <- client.url(_url).batched(Request.get("/"))
    data <- response.body.asString
    m <- ZIO.fromOption(RequestServiceLive.titleRegex.findFirstMatchIn(data)).orElseFail(new RuntimeException("title not found"))
    res <- ZIO.attempt(m.group(1))
  } yield res
}

private object RequestServiceLive {
  val titleRegex: Regex = "<title.*>(.*)</title>".r
}