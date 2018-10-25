import { readFile } from "fs"
import { promisify } from "util"
import { Feed } from "./feed"

export class Config {
    daemonIntervalMs: number = 1000
    feeds: Array<Feed> = []

    static read = function(): Promise<Config> {
        return promisify(readFile)("build/config.json") // TODO test only, sort this out
            .then(rawConfig => {
                let parsedConfig: Config = JSON.parse(rawConfig.toString())
                return Promise.resolve(Object.assign(new Config(), parsedConfig))
            })
    }
}
