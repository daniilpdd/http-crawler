package org.daniilpdd.crawler.service

import org.daniilpdd.crawler.service.client.RequestService
import zio.http.URL
import zio.{Cause, Fiber, UIO, ZIO}

object CrawlerService {
  private val handleCause: Cause[Throwable] => UIO[String] = cause => for {
    _ <- ZIO.logWarningCause(cause)
    msg <- ZIO.fromOption(cause.failureOption).map(_.getMessage).fold(_ => "", m => m)
  } yield msg

  def getTitles(urls: Seq[URL]): ZIO[RequestService, Throwable, Map[String, String]] = for {
    tf <- ZIO.foreach(urls)(u => RequestService.getTitle(u).foldCauseZIO(cause => ZIO.attempt(u.encode).zip(handleCause(cause)),title => ZIO.attempt(u.encode -> title)).fork)
    t <- Fiber.collectAll(tf).join
  } yield t.toMap
}