package taskManager.config

case class DataBaseConfig(
  driver: String,
  url: String,
  user: String,
  password: String,
  dbName: String
)
