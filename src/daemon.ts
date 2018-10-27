import { Config } from "./config/config"
import { logger } from "./log"

export class Daemon {
    static run = function() {
        logger.silly("loop triggering")
        Config.read().then(config => {
            logger.silly("config read")
            config.groups.forEach( group => {
                logger.silly(`found group ${group.name} with feeds: ${group.feeds.length}`)
                group.feeds.forEach( feed => {
                    logger.silly(`found feed ${feed.name} with url: ${feed.url} and frequency: ${feed.frequencySeconds}`)
                })
            })
        })
    }
}
