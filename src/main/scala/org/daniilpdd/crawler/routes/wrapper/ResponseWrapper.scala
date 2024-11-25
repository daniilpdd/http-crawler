package org.daniilpdd.crawler.routes.wrapper

import zio.ZLayer
import zio.http.Response
import zio.json.EncoderOps
import zio.macros.accessible

@accessible
trait ResponseWrapper {
  def wrap(res: Map[String, String]): Response
}

object ResponseWrapper {
  val json: ZLayer[Any, Nothing, JsonResponseWrapper] = ZLayer.fromFunction(JsonResponseWrapper.apply _)
  val text: ZLayer[Any, Nothing, TextResponseWrapper] = ZLayer.fromFunction(TextResponseWrapper.apply _)
}

case class JsonResponseWrapper() extends ResponseWrapper {
  override def wrap(res: Map[String, String]): Response = {
    Response.json(res.toJson)
  }
}

case class TextResponseWrapper() extends ResponseWrapper {
  override def wrap(res: Map[String, String]): Response = {
    Response.text(res.mkString("\n"))
  }
}


