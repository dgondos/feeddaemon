import { Daemon } from "./daemon";
import { Config } from "./config/config"

function loop() {
    Daemon.run()
    Config.read()
        .then(config => setTimeout(loop, config.daemonIntervalMs))
        .catch(e => {
            console.log("Config file couldn't be read, exiting.")
            throw e
        })
}

console.log("Starting daemon")
loop()