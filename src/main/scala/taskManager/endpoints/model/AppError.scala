package taskManager.endpoints.model

import sttp.model.StatusCode
import sttp.tapir.json.tethysjson.jsonBody
import sttp.tapir.{EndpointOutput, Schema, oneOf, oneOfVariant}
import tethys.derivation.semiauto.{jsonReader, jsonWriter}
import tethys.{JsonReader, JsonWriter}

//import scala.annotation.nowarn

sealed trait AppError {
  def message: String
}

object AppError {

  case class DataBaseError(override val message: String) extends AppError

  object DataBaseError {

    implicit val dataBaseErrorWriter: JsonWriter[DataBaseError] = jsonWriter[DataBaseError]
    implicit val dataBaseErrorReader: JsonReader[DataBaseError] = jsonReader[DataBaseError]

    implicit val notFoundErrorSchema: Schema[DataBaseError] =
      appErrorSchema
        .description("Сообщение об ошибке когда произошло неудачное обращение к базе данных")
        .as[DataBaseError]

  }

  object GatewayTimeoutError extends AppError {

    override def message: String = "Gateway Timeout"

    implicit val gatewayTimeoutErrorWriter: JsonWriter[GatewayTimeoutError.type] =
      JsonWriter
        .obj[GatewayTimeoutError.type]
        .addField("message")(_.message)

    implicit val gatewayTimeoutErrorReader: JsonReader[GatewayTimeoutError.type] =
      JsonReader.builder
        .addField[String]("message")
        .buildReader(_ => GatewayTimeoutError)

    implicit val gatewayTimeoutErrorSchema: Schema[GatewayTimeoutError.type] =
      appErrorSchema
        .description("Сообщение об ошибке когда внутренняя система слишком долго отвечает")
        .as[GatewayTimeoutError.type]

  }

  object InternalError extends AppError {

    override def message: String = "Непредвиденная ошибка, попробуйте позже"

    implicit val internalErrorWriter: JsonWriter[InternalError.type] =
      JsonWriter
        .obj[InternalError.type]
        .addField("message")(_.message)

    implicit val internalErrorReader: JsonReader[InternalError.type] =
      JsonReader.builder
        .addField[String]("message")
        .buildReader(_ => InternalError)

    implicit val internalErrorSchema: Schema[InternalError.type] =
      appErrorSchema
        .description("Сообщение об ошибке когда произошла непредвиденная ошибка")
        .as[InternalError.type]

  }

  private val appErrorSchema: Schema[AppError] =
    Schema
      .anyObject[AppError]
      .modify(_.message)(_.description("Сообщение об ошибке"))

  val output: EndpointOutput.OneOf[AppError, AppError] = oneOf[AppError](
    oneOfVariant[DataBaseError](
      StatusCode.NotFound,
      jsonBody[DataBaseError].example(
        DataBaseError(s"Произошла ошибка при получении значений из базы данных")
      )
    ),
    oneOfVariant[GatewayTimeoutError.type](
      StatusCode.GatewayTimeout,
      jsonBody[GatewayTimeoutError.type]
    ),
    oneOfVariant[InternalError.type](
      StatusCode.InternalServerError,
      jsonBody[InternalError.type]
    )
  )

}
