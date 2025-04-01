package taskManager.core

import cats.effect.{Async, Resource}
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Server
import taskManager.config.ServerConfig
import sttp.apispec.openapi.circe.yaml.RichOpenAPI
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.SwaggerUI
import tofu.logging.Logging

import scala.concurrent.duration.DurationInt

case class HttpServerModule(
  server: Server
)

object HttpServerModule {

  private def withDocs[I[_]](
    endpoints: List[ServerEndpoint[Any, I]],
    config: ServerConfig
  ): List[ServerEndpoint[Any, I]] = {
    val openApi: String =
      OpenAPIDocsInterpreter()
        .toOpenAPI(es = endpoints.map(_.endpoint), config.name, "1.0")
        .toYaml

    if (config.swaggerEnabled) endpoints ::: SwaggerUI[I](openApi) else endpoints
  }

  private def registerZone[I[_]: Async](
    endpoints: List[ServerEndpoint[Any, I]],
    config: ServerConfig
  ): Resource[I, Server] =
    BlazeServerBuilder[I]
      .bindHttp(config.port, config.host)
      .withHttpApp(Http4sServerInterpreter[I]().toRoutes(endpoints).orNotFound)
      .enableHttp2(true)
      .withResponseHeaderTimeout(60.seconds)
      .resource

  def startServer[I[_]: Async: Logging](
    internalApi: AppApi[I],
    serverConf: ServerConfig
  ): Resource[I, HttpServerModule] = {
    val publicEndpoints = withDocs(internalApi.api, serverConf)

    for {
      server <- registerZone(publicEndpoints, serverConf)
      _ <- Resource.eval(
        Logging[I].info(s"Server started. Docs at localhost:8080/docs")
      )
    } yield HttpServerModule(server)
  }

}
