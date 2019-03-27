package com.viral.automation.main

import com.viral.automation.analysis.ViralMarketIntelligenceAnalyzer
import com.viral.automation.authentication.ViralLogin
import com.viral.automation.marketintelligence.ViralMarketIntelligence
import com.viral.automation.productdiscovery.ViralProductDiscovery
import geb.Browser

class ViralMain {

    static void main(String... args) {

        final String email = args[0]
        final String password = args[1]
        final String input = args[2]

        def marketIntelligenceResults = [:]

        List<Browser> browsers = new ArrayList<>()

        browsers.add ViralLogin.launch(email, password)

        browsers.add ViralProductDiscovery.discoverAndRecord(input, marketIntelligenceResults)
//        browsers.add ViralMarketIntelligence.searchAndRecord(input, marketIntelligenceResults)

        browsers.each { it.quit() }

//        def marketIntelligenceAnalysis = ViralMarketIntelligenceAnalyzer.analyzeProducts(marketIntelligenceResults)
    }

    static void writeFile(file) {
        final String now = new Date().format("yyyy_MM_dd-HH_mm_ss", TimeZone.getTimeZone('America/Los_Angeles'))
        def output = new File("./market_intelligence_result_" + now + ".txt")
        output.write("LOL")
    }
}

