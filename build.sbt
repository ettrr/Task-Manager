ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.15"

lazy val root = (project in file("."))
  .settings(
    name := "TaskManager"
  )

libraryDependencies ++= Seq(
  // Cats Effect
  "org.typelevel" %% "cats-effect" % "3.5.5",

  // PureConfig для конфигов
  "com.github.pureconfig" %% "pureconfig" % "0.17.7",
  "com.github.pureconfig" %% "pureconfig-cats-effect" % "0.17.7",

  // STTP Client 4 для HTTP клиента
  "com.softwaremill.sttp.client4" %% "core" % "4.0.0-M1",
  "com.softwaremill.sttp.client4" %% "cats" % "4.0.0-M1",

  // STTP Tapir для описания endpoint
  "com.softwaremill.sttp.tapir" %% "tapir-core" % "1.10.15",
  "com.softwaremill.sttp.tapir" %% "tapir-sttp-client" % "1.10.15",
  "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % "1.10.15",

  // Tethys для сериализации/десериализации JSON
  "com.tethys-json" %% "tethys-core" % "0.28.3",
  "com.tethys-json" %% "tethys-jackson" % "0.26.0",
  "com.tethys-json" %% "tethys-derivation" % "0.28.4",

  // Tofu
  "tf.tofu" %% "tofu-core-ce3" % "0.13.2", // Core utils for cats-effect 3
  "tf.tofu" %% "tofu-logging" % "0.13.2", // Logging utils
  "tf.tofu" %% "tofu-derivation" % "0.13.2", // Derivation utils
  "tf.tofu" %% "tofu-config" % "0.13.2", // Config utils

  // Logback для логирования
  "ch.qos.logback" % "logback-classic" % "1.5.6",

  // Tapir для OpenAPI и Swagger
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % "1.10.15",
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui" % "1.10.14",
  "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % "1.10.15",
  "com.softwaremill.sttp.apispec" %% "openapi-circe-yaml" % "0.11.3",

  // Tapir для Tethys JSON
  "com.softwaremill.sttp.tapir" %% "tapir-json-tethys" % "1.10.15",

  // Другие зависимости
  "org.http4s" %% "http4s-blaze-server" % "0.23.11",
  "org.http4s" %% "http4s-dsl" % "0.23.11",
  "org.http4s" %% "http4s-circe" % "0.23.11",

  //База данных
  "org.tpolecat" %% "doobie-core"     % "1.0.0-RC4",
  "org.tpolecat" %% "doobie-postgres" % "1.0.0-RC4",
  "org.tpolecat" %% "doobie-specs2"   % "1.0.0-RC4",
  "org.postgresql" % "postgresql"      % "42.7.3",

  // Тестирование
  "org.scalatest" %% "scalatest" % "3.2.19" % Test
)

// Указываем, что используем версию 3.5.5 для cats-effect
dependencyOverrides += "org.typelevel" %% "cats-effect" % "3.5.5"