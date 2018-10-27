package feeddaemon

import com.typesafe.scalalogging.Logger
import feeddaemon.config.ConfigHandler

object Daemon {
  val logger: Logger = Logger("daemon")

  def run(): Unit = {
    logger.debug("loop triggering")
    ConfigHandler.read().groups.foreach(group => {
      logger.debug(s"found group ${group.name} with ${group.feeds.length} feeds")
      group.feeds.foreach(feed => {
        logger.debug(s"found feed ${feed.name} with url: ${feed.url} and frequency: ${feed.frequencySeconds}")
      })
    })
  }
}
