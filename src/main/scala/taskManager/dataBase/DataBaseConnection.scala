package taskManager.dataBase

import taskManager.config.DataBaseConfig
import taskManager.endpoints.model.{AppError, TaskModel}
import doobie._
import doobie.implicits._
import cats.effect._
import cats.implicits._
import doobie.util.transactor.Transactor.Aux

case class DataBaseConnection[F[_]: Async](config: DataBaseConfig) {

  private val dbName = Fragment.const(config.dbName)

  def xa: Aux[F, Unit] = Transactor.fromDriverManager[F](
    driver = config.driver,     // JDBC driver classname
    url = config.url,           // Connect URL
    user = config.user,         // Database user name
    password = config.password, // Database password
    logHandler =
      None // Don't setup logging for now. See Logging page for how to log events in detail
  )

  def insert(task: TaskModel): F[Either[AppError, Unit]] = {
    val inserting =
      sql"""
           INSERT INTO $dbName (user_token, title, deadline, duration, link)
           VALUES (${task.userToken}, ${task.title}, ${task.deadline}, ${task.duration}, ${task.link})""".update
    inserting.run.transact(xa).map {
      case res if res < 1 => Left(AppError.DataBaseError("Error inserting data into database"))
      case _              => Right(())
    }
  }

  def get(token: String): F[Either[AppError, List[(Int, TaskModel)]]] = {
    val getting =
      sql"""
        SELECT id, user_token, title, deadline, duration, link FROM $dbName
        WHERE user_token = $token
        ORDER BY id
         """.query[(Int, TaskModel)].to[List]

    getting.transact(xa).attempt.map {
      case Left(e) =>
        Left(AppError.DataBaseError(e.getMessage))
      case Right(value) => Right(value)
    }
  }

  def delete(id: Int, token: String): F[Either[AppError, Unit]] = {
    val deleting =
      sql"""
           DELETE FROM $dbName
           WHERE user_token = $token AND id = $id
         """.update
    deleting.run.transact(xa).attempt.map {
      case Left(e) =>
        Left(AppError.DataBaseError(e.getMessage))
      case Right(value) => Right(value)
    }
  }

}
