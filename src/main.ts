import { Daemon } from "./daemon";
import { Config } from "./config/config"
import { logger } from "./log"

function loop() {
    Daemon.run()
    Config.read()
        .then(config => setTimeout(loop, config.daemonIntervalMs))
        .catch(e => {
            logger.error("Config file couldn't be read, exiting.")
            logger.error(e)
            throw e
        })
}

logger.info("Starting daemon")
loop()