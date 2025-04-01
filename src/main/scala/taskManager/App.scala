package taskManager

import  taskManager.backend.{AppBackendApi, AppBackendImpl}
import cats.effect.{Async, ExitCode, IO, IOApp, Resource}
import taskManager.config.AppConfig
import taskManager.core.HttpServerModule
import taskManager.dataBase.DataBaseConnection
import tofu.logging.Logging

object App extends IOApp {

  implicit val initLoggingMake: Logging.Make[IO] = Logging.Make.plain[IO]

  override def run(args: List[String]): IO[ExitCode] =
    application[IO].useForever.attempt
      .flatMap {
        case Right(_)  => IO.println("Exit OK").as(ExitCode.Success)
        case Left(err) => IO.println(s"Exit with error: $err").as(ExitCode.Error)
      }
      .onCancel(IO.println("Exit with 'Cancel'"))

  private def application[I[_]: Async: Logging.Make]: Resource[I, Unit] = {
    implicit val logging: Logging[I] = Logging.Make[I].byName("startup")
    for {
      _ <- Resource.make(logging.info("Starting application"))(_ =>
        logging.info("Application closed")
      )
      config <- Resource.eval(AppConfig.unsafeLoad[I]())
      api <- Resource.pure(
        AppBackendApi(AppBackendImpl(DataBaseConnection(config.dataBase)))
      )
      _ <- HttpServerModule.startServer[I](api, config.server)
      _ <- Resource.make(logging.info("Application started"))(_ =>
        logging.info("Start closing application")
      )
    } yield ()
  }

}
