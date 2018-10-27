package feeddaemon.config

case class Feed
(
  name: String,
  url: String,
  frequencySeconds: Int
)