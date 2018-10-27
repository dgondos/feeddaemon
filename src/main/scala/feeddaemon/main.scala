package feeddaemon

import com.typesafe.scalalogging.Logger
import feeddaemon.config.ConfigHandler

object Feedaemon extends App {
  val logger: Logger = Logger("Feeddaemon")
  logger.info("Starting daemon")
  while(true) {
    Daemon.run()
    Thread.sleep(ConfigHandler.read().daemonIntervalMs)
  }
}