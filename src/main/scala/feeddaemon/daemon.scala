package feeddaemon

import com.typesafe.scalalogging.Logger
import feeddaemon.config.{ConfigHandler, Feed, Group}
import org.apache.http.util.EntityUtils
import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
import org.joda.time.DateTime

import scala.util.Try

object Daemon {
  val logger: Logger = Logger("daemon")
  val feedCache: scala.collection.mutable.Map[(String, String), FeedCache] = scala.collection.mutable.Map[(String, String), FeedCache]()

  private def refreshFeedCache(group: Group, feed: Feed): FeedCache = {
    val http: CloseableHttpClient = HttpClients.createDefault()
    var res: CloseableHttpResponse = null
    (for {
      req <- Try { new HttpGet(feed.url) }
      _ <- Try { res = http.execute(req) }
      _ <- Try { assert(res.getStatusLine != null && res.getStatusLine.getStatusCode >= 200 && res.getStatusLine.getStatusCode < 299) }
      entity <- Try { res.getEntity }
      rssText <- Try { EntityUtils.toString(entity) }
      _ <- Try { EntityUtils.consume(entity) }
    } yield rssText)
    .flatMap { rssText =>
      res.close()
      logger.info(s"new rss feed retrieved for group ${group.name}, feed ${feed.name}")
      val newFeedCache: FeedCache = new FeedCache
      newFeedCache.rssText = rssText
      newFeedCache.lastPulled = DateTime.now()
      return newFeedCache
    }
    .recoverWith {
      case e: Throwable => {
        if (res != null) {
          res.close()
        }
        logger.error(s"Exception while trying to retrieve rss feed ${feed.name} for group ${group.name}: ${e.getMessage}", e)
        val emptyFeedCache: FeedCache = new FeedCache
        emptyFeedCache.lastPulled = DateTime.now()
        return emptyFeedCache
      }
    }.get
  }

  def run(): Unit = {
    logger.debug("loop triggering")
    ConfigHandler.read().groups.foreach(group => {
      logger.debug(s"found group ${group.name} with ${group.feeds.length} feeds")
      group.feeds.foreach(feed => {
        logger.debug(s"found feed ${feed.name} with url: ${feed.url} and frequency: ${feed.frequencySeconds}")
        var cachedFeed: FeedCache = feedCache.getOrElse((group.name, feed.name), new FeedCache)
        if (DateTime.now().getMillis - cachedFeed.lastPulled.getMillis > (feed.frequencySeconds * 1000)) {
          cachedFeed = refreshFeedCache(group, feed)
          feedCache.put((group.name, feed.name), cachedFeed)
        }
        // TODO parse feedCache.rssText
      })
    })
  }
}
