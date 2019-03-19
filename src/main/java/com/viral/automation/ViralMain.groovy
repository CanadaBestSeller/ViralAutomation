package com.viral.automation

import geb.Browser

import static groovy.json.JsonOutput.prettyPrint
import static groovy.json.JsonOutput.toJson

class ViralMain {

    static void main(String... args) {

        final String email = args[0]
        final String password = args[1]
        final String searchTerm = args[2]

        def marketIntelligenceResults = [:]

        List<Browser> browsers = new ArrayList<>()

        browsers.add ViralLogin.launch(email, password)
        browsers.add ViralMarketIntelligence.searchAndRecord("black glue", marketIntelligenceResults)
        browsers.add ViralMarketIntelligence.searchAndRecord("yellow glue", marketIntelligenceResults)

        browsers.each { it.quit() }

        println prettyPrint(toJson(marketIntelligenceResults))
    }
}

