package taskManager.endpoints.model

import sttp.tapir.Schema

case class UserModel(
  login: String,
  password: String
) {
  def getToken: String = (login + password).hashCode.toString
}

object UserModel {

  implicit val UserSchema: Schema[UserModel] =
    Schema.derived[UserModel]

}
