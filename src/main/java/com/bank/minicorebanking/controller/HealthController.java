package com.bank.minicorebanking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


//what is the health controller ?
//Why does this class exist at all?
//Its only job is to answer the question "is my app alive?" — nothing to do with banking. It's a common pattern: almost every backend project has a tiny /health endpoint so you (or monitoring tools later) can quickly check "is the server up?" without hitting real business logic.

@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
                "status", "UP",
                "service", "mini-core-banking"
        );
    }
    //public Map<String, String> health() {
    //This is just a regular Java method named health. It returns a Map<String, String> — think of a Map as a simple list of label: value pairs (like a dictionary).
    //return Map.of("status", "UP", "service", "mini-core-banking");
    //This builds that little dictionary on the spot:
    //
    //status → UP
    //service → mini-core-banking
    //
    //Here's the magic part: Spring automatically converts that Java Map into JSON before sending it back over the internet. That's exactly the JSON you saw in Postman:
    //json{
    //  "service": "mini-core-banking",
    //  "status": "UP"
    //}
    //You never wrote any JSON-formatting code — Spring does that conversion for you behind the scenes, because of another setting already baked into your project (spring-boot-starter-web, imported in pom.xml).
    //Why this matters going forward
    //Customer will follow the exact same shape:
    //
    //A @GetMapping("/customers") (or @PostMapping for creating one) — the doorbell label
    //A method underneath it — what happens when that doorbell rings
    //Returning Java objects — Spring auto-converts them to JSON
    //
    //Same pattern, just with real data instead of a hardcoded map. Ready to build the Customer entity now?

}
//Here's the magic part: Spring automatically converts that Java Map into JSON before sending it back over the internet. That's exactly the JSON you saw in Postman:

//You never wrote any JSON-formatting code — Spring does that conversion for you behind the scenes, because of another setting already baked into your project (spring-boot-starter-web, imported in pom.xml).
//Why this matters going forward
//Customer will follow the exact same shape:
//
//A @GetMapping("/customers") (or @PostMapping for creating one) — the doorbell label
//Same pattern, just with real data instead of a hardcoded map. Ready to build the Customer entity now?
