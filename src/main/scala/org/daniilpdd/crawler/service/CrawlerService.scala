package org.daniilpdd.crawler.service

import org.daniilpdd.crawler.service.client.RequestService
import zio.http.URL
import zio.{Fiber, ZIO}

object CrawlerService {
  def getTitles(urls: Seq[URL]): ZIO[RequestService, Throwable, Map[String, String]] = for {
    tf <- ZIO.foreach(urls)(u => RequestService.getTitle(u).map(title => u.encode -> title).fork)
    t <- Fiber.collectAll(tf).join
  } yield t.toMap
}