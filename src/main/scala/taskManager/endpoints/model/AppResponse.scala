package taskManager.endpoints.model

import sttp.tapir.Schema

import scala.annotation.nowarn

case class AppResponse(
  htmlBody: String
)

@nowarn
object AppResponse {

  implicit val ResponseSchema: Schema[AppResponse] =
    Schema
      .derived[AppResponse]
      .modify(_.htmlBody)(_.description("Тело ответа в формате html"))
      .description("Json обёртка над html ответом")

}
