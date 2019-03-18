package com.viral.automation.modules

import geb.Module

class MarketTrendsModule extends Module {

    static content = {
        moduleTab { $("div#tab-marketTrends") }
        avgPriceOver90Days { $(".stats-row.el-row", 6).$("p", 0).text() }
        bestSellerPeriod { $(".stats-row.el-row", 6).$("p", 1).text() }
        rateOfReviewIncreaase { $(".stats-row.el-row", 6).$("p", 2).text() }
        annualSalesTrend { $(".stats-row.el-row", 6).$("p", 3).text() }
    }

    void open() {
        moduleTab.click()
        assert $(".stats-row.el-row", 6).find("h5")*.text() ==
        [
            "AVERAGE PRICE OVER 90 DAYS",
            "BEST SELLING PERIOD",
            "RATE OF REVIEW INCREASE",
            "ANNUAL SALES TREND"
        ]
    }
}
