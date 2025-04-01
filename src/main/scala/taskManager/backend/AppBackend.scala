package taskManager.backend

import taskManager.endpoints.model.{AppDeleteRequest, AppError, AppRequest, AppResponse, UserModel}

trait AppBackend[F[_]] {

  def addTask(request: AppRequest): F[Either[AppError, AppResponse]]
  def addTaskPage(userToken: String): F[Either[AppError, AppResponse]]
  def deleteTask(deleteRequest: AppDeleteRequest): F[Either[AppError, AppResponse]]
  def getPlaning(userToken: String): F[Either[AppError, AppResponse]]
  def registerPage: F[Either[AppError, AppResponse]]
  def registerNewUser(userModel: UserModel): F[Either[AppError, AppResponse]]

}
