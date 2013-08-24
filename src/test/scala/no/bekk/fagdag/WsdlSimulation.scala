package no.bekk.fagdag

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class WsdlSimulation extends Simulation {

  val httpConf = http
    .baseURL("http://localhost:8080/wsdl_first/services")
    .shareConnections
    .baseHeaders(Map("SOAPAction" -> "", "Content-Type" -> "text/xml;charset=UTF-8"))

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

  setUp(scn.inject(ramp(1 users) over (1 seconds))).protocols(httpConf)

}
