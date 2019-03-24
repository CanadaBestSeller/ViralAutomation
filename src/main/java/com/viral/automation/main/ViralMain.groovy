package com.viral.automation.main

import com.viral.automation.analysis.ViralMarketIntelligenceAnalyzer
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

        def marketIntelligenceAnalysis = ViralMarketIntelligenceAnalyzer.analyzeProducts(marketIntelligenceResults)
        println prettyPrint(toJson(marketIntelligenceAnalysis))
        println prettyPrint(toJson(marketIntelligenceAnalysis))
    }

    static void writeFile(file) {
        final String now = new Date().format("yyyy_MM_dd-HH_mm_ss", TimeZone.getTimeZone('America/Los_Angeles'))
        def output = new File("./market_intelligence_result_" + now + ".txt")
        output.write("LOL")
    }
}

