package feeddaemon.config

case class Group
(
  name: String,
  feeds: Array[Feed]
)