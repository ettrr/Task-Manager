package taskManager.endpoints.model

import sttp.tapir.Schema

case class TaskModel(
  userToken: String = "",
  title: String = "Empty",
  deadline: String = "ГГ-ММ-ДД-ЧЧ",
  duration: Int = 0,
  link: String = "'https://example.com'"
)

object TaskModel {

  implicit val TaskSchema: Schema[TaskModel] =
    Schema.derived[TaskModel]

}
