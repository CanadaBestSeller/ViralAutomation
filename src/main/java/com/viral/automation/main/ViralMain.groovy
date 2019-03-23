package com.viral.automation.main

import com.viral.automation.marketintelligence.ViralMarketIntelligence
import com.viral.automation.authentication.ViralLogin
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
        browsers.add ViralMarketIntelligence.searchAndRecord(searchTerm, marketIntelligenceResults)

        browsers.each { it.quit() }

        println prettyPrint(toJson(marketIntelligenceResults))
    }
}

