package taskManager.core

import sttp.tapir.server.ServerEndpoint

trait AppApi[F[_]] {
  def api: List[ServerEndpoint[Any, F]]
}
