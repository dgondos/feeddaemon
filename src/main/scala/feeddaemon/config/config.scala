package feeddaemon.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

case class Config
(
  daemonIntervalMs: Int = 1000,
  groups: Array[Group]
)

object ConfigHandler {
  def read(): Config = {
    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
    mapper.readValue(new java.io.File("build/config.json"), classOf[Config]) // TODO sort this out
  }
}