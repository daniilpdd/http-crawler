package org.daniilpdd.crawler.service.client

import zio.http.{Client, Request, URL}
import zio.macros.accessible
import zio.{UIO, ZIO, ZLayer}

import scala.util.matching.Regex

@accessible
trait RequestService {
  def getTitle(url: URL): UIO[String]
}

object RequestService {
  val identical: ZLayer[Any, Nothing, RequestServiceIdentical] = ZLayer.fromFunction(RequestServiceIdentical.apply _)
  val live: ZLayer[Client, Nothing, RequestServiceLive] = ZLayer.fromFunction(RequestServiceLive.apply _)
}

case class RequestServiceIdentical() extends RequestService {
  override def getTitle(url: URL): UIO[String] = ZIO.succeed(url.encode)
}

case class RequestServiceLive(client: Client) extends RequestService {
  override def getTitle(url: URL): UIO[String] = (for {
    response <- client.url(url).batched(Request.get("/"))
    data <- response.body.asString
    m <- ZIO.fromOption(RequestServiceLive.titleRegex.findFirstMatchIn(data)).orElseFail(new RuntimeException("title not found"))
    res <- ZIO.attempt(m.group(1))
  } yield res).foldZIO(e => for {
    _ <- ZIO.logWarning(s"An error occurred while getting the title from the $url: ${e.getMessage}")
    res <- ZIO.succeed(e.getMessage)
  } yield res, r => ZIO.succeed(r))
}

private object RequestServiceLive {
  val titleRegex: Regex = "<title.*>(.*)</title>".r
}