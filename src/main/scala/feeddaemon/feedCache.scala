package feeddaemon

import org.joda.time.DateTime

class FeedCache {
  var lastPulled: DateTime = new DateTime(0)
  var rssText: String = ""
}
