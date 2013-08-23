import io.gatling.core.Predef._
import io.gatling.http.Predef._
import bootstrap._
import scala.concurrent.duration._

class JMeterBenchmark extends Simulation {

  val PAUSE = 1 second
  val REPETITIONS = 2

  val httpConf = http
    .baseURL("http://localhost:8080/examples/servlets")
    .acceptCharsetHeader("ISO-8859-1,utf-8;q=0.7,*;q=0.7")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("fr,fr")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:8.0.1) Gecko/20100101 Firefox/8.0.1")
    .shareConnections

  val scn = scenario("JMeter Benckmark with Gatling")
    .repeat(REPETITIONS) {
      exec(http("sessionExample1")
        .get("/servlet/SessionExample"))
      .pause(PAUSE)
      .exec(http("sessionExample2Post")
        .post("/servlet/SessionExample")
        .param("dataname", "TOTO")
        .param("datavalue", "TITI")
        .header("Content-Type", "application/x-www-form-urlencoded"))
       .pause(PAUSE)
      .exec(http("index")
        .get("/index.html"))
      .pause(PAUSE)
      .exec(http("sessionExample3")
        .get("/servlet/SessionExample"))
      .pause(PAUSE)
  }

  setUp(scn.inject(nothingFor(2 seconds),
                   constantRate(100 usersPerSec) during (50 seconds))).protocols(httpConf)

}
