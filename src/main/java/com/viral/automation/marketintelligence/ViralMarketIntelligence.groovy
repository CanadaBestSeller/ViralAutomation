package com.viral.automation.marketintelligence

import com.viral.automation.main.ChromeBrowserProvider
import com.viral.automation.main.Log
import com.viral.automation.marketintelligence.modules.*
import geb.Browser
import geb.Page

import static groovy.json.JsonOutput.prettyPrint
import static groovy.json.JsonOutput.toJson

/**
 * This class essentially scrapes 2 components of the market intelligence page:
 *
 * A) Top sellers standard view: TBD
 *
 * B) Detailed statistics, this includes:
 * 1. Top 5 avgSales/avgRevenue/avgPrice
 * 2. Top 5 avgReview/avgReviewRating
 * 3. Top 10 avgSales/avgRevenue/avgPrice
 * 4. Top 10 avgReview/avgReviewRating
 * 5. Page 1 avgSales/avgRevenue/avgPrice
 * 6. Page 1 avgReview/avgReviewRating
 * 7. Market trends
 * 8. Viral Launch analysis
 *
 * ### Conveniently, all 8 detailed statistics in component B can be selected via $(".stats-row.el-row")
 * Should this be changed in the future, PLEASE modify the Module classes to preserve code cleanliness :)
 */
class ViralMarketIntelligence {

    public static final int PAGE_LOAD_TIMEOUT_IN_SECONDS = 45

    static Browser searchAndRecord(searchTerm, marketIntelligenceResults) {

        def requestParams = [marketplace: "US", search: searchTerm]
        Log.info "Analyzing with $requestParams..."

        // Populate this map with final results
        def marketIntelligenceResult = [:]

        def browser = ChromeBrowserProvider.get()
        browser.drive {

            try {

                to(requestParams, ViralMarketIntelligencePage)

                waitFor(PAGE_LOAD_TIMEOUT_IN_SECONDS) { header.displayed }
                Log.info "Market intelligence page started loading..."

                waitFor(PAGE_LOAD_TIMEOUT_IN_SECONDS) { !spinner.displayed }
                Log.info "Market intelligence page fully loaded!"

                standardView.open() // Due to AJAX, we must click to open the window to populate the data
                standardView.transcribe(marketIntelligenceResult)

                detailedView.open() // Due to AJAX, we must click to open the window to populate the data
                detailedView.transcribe(marketIntelligenceResult)

                marketTrendsView.open() // Due to AJAX, we must click to open the window to populate the data
                marketTrendsView.transcribe(marketIntelligenceResult)

                analysisView.open() // Due to AJAX, we must click to open the window to populate the data
                analysisView.transcribe(marketIntelligenceResult)

                costCalculatorView.open() // Due to AJAX, we must click to open the window to populate the data
                costCalculatorView.transcribe(marketIntelligenceResult)

                if (estimatedSearchVolume) { // Do this last. Takes time to load. Sometimes the search volume isn't provided
                    marketIntelligenceResult['estimatedSearchVolume'] = estimatedSearchVolume.text()
                }

                Log.info("Analyzed!")
                Log.debug("Market intelligence RAW result:\n" + prettyPrint(toJson(marketIntelligenceResult)))

                marketIntelligenceResults[searchTerm] = marketIntelligenceResult

            } catch (Throwable t) {
                final StringWriter stringWriter = new StringWriter()
                t.printStackTrace(new PrintWriter(stringWriter))
                Log.error("Failed to gather market intelligence for ${searchTerm}: " + stringWriter.toString())

            }

        }

        return browser
    }

}

class ViralMarketIntelligencePage extends Page {
    static url = "https://viral-launch.com/sellers/launch-staging/pages/market-intelligence.html"

    static content = {
        header { $("h3.search-phrase", 0) }
        spinner { $("div.el-loading-mask", 0) }
        estimatedSearchVolume { $("div.search-volume", 0).$("a", 0) }

        standardView { module StandardViewModule }
        detailedView { module DetailedViewModule }
        marketTrendsView { module MarketTrendsModule }
        analysisView { module AnalysisViewModule }
        costCalculatorView { module CostCalculatorViewModule }
    }
}


