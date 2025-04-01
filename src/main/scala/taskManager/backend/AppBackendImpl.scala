package taskManager.backend

import cats.Monad
import taskManager.dataBase.DataBaseConnection
import taskManager.endpoints.model.{
  AppDeleteRequest,
  AppError,
  AppRequest,
  AppResponse,
  TaskModel,
  UserModel
}
import cats.implicits._
import cats.effect.kernel.Async

import scala.annotation.tailrec

case class AppBackendImpl[F[_]: Async](db: DataBaseConnection[F]) extends AppBackend[F] {

  override def addTaskPage(token: String): F[Either[AppError, AppResponse]] = {
    for {
      tasks <- db.get(token)
      // Генерация HTML для каждой задачи
      result = tasks match {
        case Left(e) => Left(e)
        case Right(tasks) =>
          val taskHtml = tasks.map { case (id, task) =>
            s"""
               |<tr>
               |  <td>$id</td>
               |  <td>${task.title}</td>
               |  <td>${task.deadline.replace('T', ' ')}</td>
               |  <td>${task.duration} ч.</td>
               |  <td><a href="${task.link}" target="_blank">${task.link}</a></td>
               |</tr>
               |""".stripMargin
          }

          val tasksHtml = taskHtml.mkString

          Right(AppResponse(HtmlAddPage.get(tasksHtml, token)))
      }
    } yield result
  }

  override def addTask(request: AppRequest): F[Either[AppError, AppResponse]] =
    for {
      _        <- db.insert(request.query)
      response <- addTaskPage(request.query.userToken)
    } yield response

  override def deleteTask(task: AppDeleteRequest): F[Either[AppError, AppResponse]] =
    for {
      _        <- db.delete(task.taskID, task.userToken)
      response <- addTaskPage(task.userToken)
    } yield response

  override def getPlaning(token: String): F[Either[AppError, AppResponse]] = {
    for {
      tasks <- db.get(token)
      result = tasks match {
        case Left(e) => Left(e)
        case Right(tasks) =>
          val tasks_plan = PlaningAlgo.planingAlgo(tasks)
          val taskHtml = tasks_plan.map { case (_, task) =>
            s"""
               |<tr>
               |  <td>${task.title}</td>
               |  <td>${task.deadline.replace('T', ' ')}</td>
               |  <td>${task.duration} ч.</td>
               |  <td><a href="${task.link}" target="_blank">${task.link}</a></td>
               |</tr>
               |""".stripMargin
          }

          val tasksHtml = taskHtml.mkString

          Right(AppResponse(HtmlPlanPage.get(tasksHtml, token)))
      }
    } yield result
  }

  override def registerPage: F[Either[AppError, AppResponse]] =
    Monad[F].pure(Right(AppResponse(HtmlRegisterPage.get)))

  override def registerNewUser(userModel: UserModel): F[Either[AppError, AppResponse]] =
    Monad[F].pure(Right(AppResponse(HtmlRegisterPageNewUser.get(userModel.getToken))))

}

object PlaningAlgo {

  private case class HelpStruct(
    taskId: Int = 0,
    timeUntilDeadline: Int = 0,
    taskModel: TaskModel = TaskModel()
  )

  private def localSort(arr: List[HelpStruct]): List[HelpStruct] = arr.sortWith {
    case (task1, task2) =>
      task1.timeUntilDeadline <= task2.timeUntilDeadline
  }

  private def intoHelpStruct(arr: List[(Int, TaskModel)]): List[HelpStruct] =
    arr.map { case (id, task) =>
      HelpStruct(
        id,
        DateTime.getTimeUntilDeadline(task.deadline),
        task
      )
    }

  def planingAlgo(arr: List[(Int, TaskModel)]): List[(Int, TaskModel)] = {

    def compare_pair(a: HelpStruct, b: HelpStruct): HelpStruct =
      if (a.taskModel.duration >= b.taskModel.duration) a else b

    @tailrec
    def loop(
      arrInput: List[HelpStruct],
      acc: List[HelpStruct],
      time: Int
    ): List[HelpStruct] =
      arrInput match {
        case Nil => acc
        case task :: tail =>
          if (time + task.taskModel.duration > task.timeUntilDeadline) {
            val taskWithMaxDuration =
              acc.foldLeft(HelpStruct())((acc, task) => compare_pair(acc, task))
            loop(
              tail,
              acc.filter(_ != taskWithMaxDuration).appended(task),
              time + task.taskModel.duration - taskWithMaxDuration.taskModel.duration
            )
          } else loop(tail, acc.appended(task), time + task.taskModel.duration)
      }

    localSort(loop(localSort(intoHelpStruct(arr)), Nil, 0)).map(task =>
      (task.taskId, task.taskModel)
    )
  }

}

object DateTime {

  import java.time.LocalDateTime
  import java.time.format.DateTimeFormatter
  import java.time.temporal.ChronoUnit

  private def now: LocalDateTime = LocalDateTime.now()

  private def formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")

  private def timeNow: String = now.format(formatter)

  def getTimeUntilDeadline(deadline: String): Int = {
    val start = LocalDateTime.parse(timeNow, formatter)
    val end   = LocalDateTime.parse(deadline, formatter)
    ChronoUnit.HOURS.between(start, end).toInt
  }

}
