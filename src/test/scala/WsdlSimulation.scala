import io.gatling.core.Predef._
import io.gatling.http.Predef._
import bootstrap._

import io.gatling.http.Headers.Names._
import scala.concurrent.duration._

class WsdlSimulation extends Simulation {

    val httpConf = http
        .baseURL("http://localhost:8080/wsdl_first/services")

      val scn = scenario("Wsdl Simulation")
        .feed(csv("usernames.csv").random)
        .exec(
          http("Get customers by name")
            .post("/CustomerServicePort")
              .body(ELFileBody("getCustomersByName.xml")).check(regex("<customerId>(\\d*)</customerId>").saveAs("id")))
        .exec(
          http("Update customer")
            .post("/CustomerServicePort")
              .body(ELFileBody("updateCustomer.xml")).check(status.is(202))
          )

    setUp(scn.inject(ramp(10 users) over(1 seconds))).protocols(httpConf)

}