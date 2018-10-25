import { Config } from "./config/config"

export class Daemon {
    static run = function() {
        console.log("loop triggering")
        Config.read().then(config => {
            console.log("config read")
            config.feeds.forEach( feed => {
                console.log(`found feed ${feed.name} with url: ${feed.url} and frequency: ${feed.frequencySeconds}`)
            })
        })
    }
}
