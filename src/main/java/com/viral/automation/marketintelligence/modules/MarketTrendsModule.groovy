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
        Log.info("Opened Market Trends.")
        waitFor(2) { loaded }
        Log.success("Opened Market Trends.")
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
        marketIntelligenceResult['trends_avgPriceOver90Days'] = avgPriceOver90Days.text()
        marketIntelligenceResult['trends_bestSellerPeriod'] = bestSellerPeriod.text()
        marketIntelligenceResult['trends_rateOfReviewIncrease'] = rateOfReviewIncrease.text()
        marketIntelligenceResult['trends_annualSalesTrend'] = annualSalesTrend.text()
    }
}
