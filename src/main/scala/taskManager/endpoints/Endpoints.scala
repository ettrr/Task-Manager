package taskManager.endpoints

import taskManager.endpoints.model.{
  AppDeleteRequest,
  AppError,
  AppRequest,
  AppResponse,
  TaskModel,
  UserModel
}
import sttp.tapir._
import sttp.tapir.CodecFormat.XWwwFormUrlencoded

object Endpoints {

  implicit val intCodec: Codec[String, Int, XWwwFormUrlencoded] = Codec.string
    .map(str => str.substring("id=".length).toInt)(id => s"id=$id")
    .format(XWwwFormUrlencoded())

  implicit val strCodec: Codec[String, String, XWwwFormUrlencoded] = Codec.string
    .map(str => str.substring("userToken=".length))(token => s"userToken=$token")
    .format(XWwwFormUrlencoded())

  val createTaskPage: Endpoint[Unit, String, AppError, AppResponse, Any] =
    endpoint.post
      .in("api" / "add_task_page")
      .in(formBody[String])
      .out(
        htmlBodyUtf8.map(AppResponse(_))(_.htmlBody)
      )
      .errorOut(AppError.output)

  val createTask: Endpoint[Unit, AppRequest, AppError, AppResponse, Any] =
    endpoint.post
      .in("api" / "add_task")
      .in(
        formBody[TaskModel]
      )
      .out(
        htmlBodyUtf8.map(AppResponse(_))(_.htmlBody)
      )
      .errorOut(AppError.output)
      .mapInTo[AppRequest]

  val deleteTask: Endpoint[Unit, AppDeleteRequest, AppError, AppResponse, Any] =
    endpoint.post
      .in("api" / "delete_task")
      .in(formBody[AppDeleteRequest])
      .out(
        htmlBodyUtf8.map(AppResponse(_))(_.htmlBody)
      )
      .errorOut(AppError.output)

  val getPlaning: Endpoint[Unit, String, AppError, AppResponse, Any] =
    endpoint.post
      .in("api" / "get_planing")
      .in(formBody[String])
      .out(
        htmlBodyUtf8.map(AppResponse(_))(_.htmlBody)
      )
      .errorOut(AppError.output)

  val registerPage: Endpoint[Unit, Unit, AppError, AppResponse, Any] =
    endpoint.get
      .in("api" / "register")
      .out(
        htmlBodyUtf8.map(AppResponse(_))(_.htmlBody)
      )
      .errorOut(AppError.output)

  val registerPageNewUser: Endpoint[Unit, UserModel, AppError, AppResponse, Any] =
    endpoint.post
      .in("api" / "register_new_user")
      .in(formBody[UserModel])
      .out(
        htmlBodyUtf8.map(AppResponse(_))(_.htmlBody)
      )
      .errorOut(AppError.output)

}
