package com.viral.automation.marketintelligence.modules

import com.viral.automation.main.Log
import geb.Module

class AnalysisViewModule extends Module {

    static content = {
        analysisViewTab { $("div#tab-vlAnalysis") }

        productIdeaScore { $(".stats-row.el-row", 7).$("p", 0) }
        possibleMonthlySales { $(".stats-row.el-row", 7).$("p", 1) }
        reviewsNeededToSellWell { $(".stats-row.el-row", 7).$("p", 2) }
        salesPatternAnalysis { $(".stats-row.el-row", 7).$("p", 3) }
        tipContent { $("div.tip-content") }
    }

    void open() {
        analysisViewTab.click()
        Log.info("Clicked VL Analysis tab.")
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
        marketIntelligenceResult['analysis_productIdeaScore'] = productIdeaScore.text()
        marketIntelligenceResult['analysis_possibleMonthlySales'] = possibleMonthlySales.text()
        marketIntelligenceResult['analysis_reviewsNeededToSellWell'] = reviewsNeededToSellWell.text()
        marketIntelligenceResult['analysis_salesPatternAnalysis'] = salesPatternAnalysis.text()
        marketIntelligenceResult['analysis_tipContent'] = tipContent.$("li")*.text().join(" | ")
    }
}
