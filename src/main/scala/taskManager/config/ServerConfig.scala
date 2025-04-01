package taskManager.config

case class ServerConfig(
  name: String,
  host: String,
  port: Int,
  swaggerEnabled: Boolean
)
