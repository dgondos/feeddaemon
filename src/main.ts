import { Daemon } from "./daemon";

function loop() {
    Daemon.run()
    setTimeout(loop, 1000)
}

console.log("Starting loop")
loop()