package com.viral.automation.modules

import com.viral.automation.Log
import geb.Module

class AnalysisViewModule extends Module {

    static content = {
        marketTrendsTab { $("div#tab-vlAnalysis") }

        productIdeaScore { $(".stats-row.el-row", 7).$("p", 0) }
        possibleMonthlySales { $(".stats-row.el-row", 7).$("p", 1) }
        reviewsNeededToSellWell { $(".stats-row.el-row", 7).$("p", 2) }
        salesPatternAnalysis { $(".stats-row.el-row", 7).$("p", 3) }
    }

    void open() {
        marketTrendsTab.click()
        waitFor(2) { loaded }
        Log.success("Opened Viral Launch Analysis.")
    }

    boolean isLoaded() {
        return $(".stats-row.el-row", 7).find("h5")*.text() ==
            [
                "PRODUCT IDEA SCORE",
                "POSSIBLE MONTHLY SALES",
                "REVIEWS NEEDED TO SELL WELL",
                "SALES PATTERN ANALYSIS"
            ]
    }

    def transcribe(marketIntelligenceResult) {
        marketIntelligenceResult['productIdeaScore'] = productIdeaScore.text()
        marketIntelligenceResult['possibleMonthlySales'] = possibleMonthlySales.text()
        marketIntelligenceResult['reviewsNeededToSellWell'] = reviewsNeededToSellWell.text()
        marketIntelligenceResult['salesPatternAnalysis'] = salesPatternAnalysis.text()
    }
}
