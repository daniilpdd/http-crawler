package org.daniilpdd.crawler.service

import org.daniilpdd.crawler.service.client.RequestService
import zio.{Fiber, Task, ZIO}

object CrawlerService {
  def getTitles(urls: Seq[String]) = for {
    tf <- ZIO.foreach(urls)(_t => RequestService.getTitle(_t).map(x => _t -> x).fork)
    t <- Fiber.collectAll(tf).join
  } yield t.toMap
}