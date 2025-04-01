package taskManager.config

import cats.effect.Sync
import pureconfig.generic.ProductHint
import pureconfig.generic.semiauto.deriveReader
import pureconfig.module.catseffect.syntax.CatsEffectConfigSource
import pureconfig.{ConfigReader, ConfigSource}

import scala.annotation.unused

case class AppConfig(
  server: ServerConfig,
  client: ClientConfig,
  dataBase: DataBaseConfig
)

object AppConfig {

  @unused private implicit def hint[T]: ProductHint[T] = ProductHint[T](allowUnknownKeys = false)

  implicit val configReader: ConfigReader[AppConfig] = {
    @unused implicit val clientReader: ConfigReader[ClientConfig] =
      deriveReader[ClientConfig]
    @unused implicit val serverReader: ConfigReader[ServerConfig] =
      deriveReader[ServerConfig]
    @unused implicit val dataBaseReader: ConfigReader[DataBaseConfig] =
      deriveReader[DataBaseConfig]

    deriveReader[AppConfig]
  }

  def unsafeLoad[F[_]: Sync](
    config: ConfigSource = ConfigSource.default
  ): F[AppConfig] =
    config.at("taskManager").loadF[F, AppConfig]()

}
