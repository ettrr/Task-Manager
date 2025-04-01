package taskManager.backend

import taskManager.core.AppApi
import taskManager.endpoints.Endpoints
import taskManager.endpoints.model.{AppDeleteRequest, AppError, AppRequest, AppResponse, UserModel}
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.ServerEndpoint.Full

case class AppBackendApi[F[_]](appBackend: AppBackendImpl[F]) extends AppApi[F] {

  private def createTaskPage: Full[Unit, Unit, String, AppError, AppResponse, Any, F] =
    Endpoints.createTaskPage.serverLogic(token => appBackend.addTaskPage(token))

  private def createTask: Full[Unit, Unit, AppRequest, AppError, AppResponse, Any, F] =
    Endpoints.createTask
      .serverLogic((request: AppRequest) => appBackend.addTask(request))

  private def deleteTask: Full[Unit, Unit, AppDeleteRequest, AppError, AppResponse, Any, F] =
    Endpoints.deleteTask
      .serverLogic(task => appBackend.deleteTask(task))

  private def getPlaning: Full[Unit, Unit, String, AppError, AppResponse, Any, F] =
    Endpoints.getPlaning
      .serverLogic(token => appBackend.getPlaning(token))

  private def registerPage: Full[Unit, Unit, Unit, AppError, AppResponse, Any, F] =
    Endpoints.registerPage
      .serverLogic(_ => appBackend.registerPage)

  private def registerNewUser: Full[Unit, Unit, UserModel, AppError, AppResponse, Any, F] =
    Endpoints.registerPageNewUser
      .serverLogic(user => appBackend.registerNewUser(user))

  def api: List[ServerEndpoint[Any, F]] =
    List(createTaskPage, createTask, deleteTask, getPlaning, registerPage, registerNewUser)

}
