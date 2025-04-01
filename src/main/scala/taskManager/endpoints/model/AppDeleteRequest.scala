package taskManager.endpoints.model

import sttp.tapir.Schema

case class AppDeleteRequest(
  userToken: String,
  taskID: Int
)

object AppDeleteRequest {

  implicit val TaskSchema: Schema[AppDeleteRequest] =
    Schema.derived[AppDeleteRequest]

}
