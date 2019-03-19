package com.viral.automation.modules

import com.viral.automation.Log
import geb.Module

class DetailedViewModule extends Module {

    static content = {
        topSellersTab { $("div#tab-topSellers", 0) }
        detailedStatisticsSubTab { $("div#tab-1", 0) }

        top5AverageSales { $(".stats-row.el-row", 0).$("p", 0) }
        top5AverageRevenue { $(".stats-row.el-row", 0).$("p", 1) }
        top5AveragePrice { $(".stats-row.el-row", 0).$("p", 2) }
        top5AverageReviewCount { $(".stats-row.el-row", 1).$("p", 0) }
        top5AverageReviewRating { $(".stats-row.el-row", 1).$("p", 1) }

        top10AverageSales { $(".stats-row.el-row", 2).$("p", 0) }
        top10AverageRevenue { $(".stats-row.el-row", 2).$("p", 1) }
        top10AveragePrice { $(".stats-row.el-row", 2).$("p", 2) }
        top10AverageReviewCount { $(".stats-row.el-row", 3).$("p", 0) }
        top10AverageReviewRating { $(".stats-row.el-row", 3).$("p", 1) }

        page1AverageSales { $(".stats-row.el-row", 4).$("p", 0) }
        page1AverageRevenue { $(".stats-row.el-row", 4).$("p", 1) }
        page1AveragePrice { $(".stats-row.el-row", 4).$("p", 2) }
        page1AverageReviewCount { $(".stats-row.el-row", 5).$("p", 0) }
        page1AverageReviewRating { $(".stats-row.el-row", 5).$("p", 1) }
    }

    void open() {
        topSellersTab.click()
        Log.info("Clicked Top Sellers tab.")

        waitFor(2) { detailedStatisticsSubTab.displayed }
        detailedStatisticsSubTab.click()
        Log.info("Clicked Detailed Statistics sub-tab.")

        waitFor(10) { loaded }
        Log.success("Opened Detailed Statistics.")
    }

    boolean isLoaded() {
        boolean loaded2 = $(".stats-row.el-row", 2).find("h5")*.text() == ["AVERAGE\nSALES", "AVERAGE\nREVENUE", "AVERAGE\nPRICE"]
        boolean loaded3 = $(".stats-row.el-row", 3).find("h5")*.text() == ["AVERAGE REVIEW\nCOUNT", "AVERAGE REVIEW\nRATING"]
        boolean loaded4 = $(".stats-row.el-row", 4).find("h5")*.text() == ["AVERAGE\nSALES", "AVERAGE\nREVENUE", "AVERAGE\nPRICE"]
        boolean loaded5 = $(".stats-row.el-row", 5).find("h5")*.text() == ["AVERAGE REVIEW\nCOUNT", "AVERAGE REVIEW\nRATING"]
        return loaded2 && loaded3 && loaded4 && loaded5
    }

    def transcribe(marketIntelligenceResult) {
        marketIntelligenceResult['top10AverageSales'] = top10AverageSales.text()
        marketIntelligenceResult['top10AverageRevenue'] = top10AverageRevenue.text()
        marketIntelligenceResult['top10AveragePrice'] = top10AveragePrice.text()
        marketIntelligenceResult['top10AverageReviewCount'] = top10AverageReviewCount.text()
        marketIntelligenceResult['top10AverageReviewRating'] = top10AverageReviewRating.text()

        marketIntelligenceResult['page1AverageSales'] = page1AverageSales.text()
        marketIntelligenceResult['page1AverageRevenue'] = page1AverageRevenue.text()
        marketIntelligenceResult['page1AveragePrice'] = page1AveragePrice.text()
        marketIntelligenceResult['page1AverageReviewCount'] = page1AverageReviewCount.text()
        marketIntelligenceResult['page1AverageReviewRating'] = page1AverageReviewRating.text()
    }
}
