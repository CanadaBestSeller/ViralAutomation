package com.viral.automation.marketintelligence.modules

import com.viral.automation.main.Log
import geb.Module

class MarketTrendsModule extends Module {

    static content = {
        marketTrendsTab { $("div#tab-marketTrends") }

        avgPriceOver90Days { $(".stats-row.el-row", 6).$("p", 0) }
        bestSellerPeriod { $(".stats-row.el-row", 6).$("p", 1) }
        rateOfReviewIncrease { $(".stats-row.el-row", 6).$("p", 2) }
        annualSalesTrend { $(".stats-row.el-row", 6).$("p", 3) }
    }

    void open() {
        marketTrendsTab.click()
        Log.debug("Opened Market Trends.")
        waitFor(2) { loaded }
        Log.info("Opened Market Trends.")
    }

    boolean isLoaded() {
        return $(".stats-row.el-row", 6).find("h5")*.text() ==
        [
            "AVERAGE PRICE OVER 90 DAYS",
            "BEST SELLING PERIOD",
            "RATE OF REVIEW INCREASE",
            "ANNUAL SALES TREND"
        ]
    }

    def transcribe(marketIntelligenceResult) {
        marketIntelligenceResult['raw_trends_avgPriceOver90Days'] = avgPriceOver90Days.text()
        marketIntelligenceResult['raw_trends_bestSellerPeriod'] = bestSellerPeriod.text()
        marketIntelligenceResult['raw_trends_rateOfReviewIncrease'] = rateOfReviewIncrease.text()
        marketIntelligenceResult['raw_trends_annualSalesTrend'] = annualSalesTrend.text()
    }
}
