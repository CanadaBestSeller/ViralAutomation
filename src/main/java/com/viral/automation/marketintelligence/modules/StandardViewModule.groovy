package com.viral.automation.marketintelligence.modules

import com.viral.automation.utils.Log
import geb.Module

class StandardViewModule extends Module {

    static content = {
        topSellersTab { $("div#tab-topSellers", 0) }
        standardViewSubTab { $("div#tab-0", 0) }
    }

    void open() {
        topSellersTab.click()
        Log.info("Clicked Top Sellers tab.")

        waitFor(2) { standardViewSubTab.displayed }
        standardViewSubTab.click()
        Log.info("Clicked Stnadard View tab.")

        waitFor(10) { loaded }
        Log.success("Opened Standard View.")
    }

    boolean isLoaded() {
        boolean loaded2 = $(".stats-row.el-row", 2).find("h5")*.text() == ["AVERAGE\nSALES", "AVERAGE\nREVENUE", "AVERAGE\nPRICE"]
        boolean loaded3 = $(".stats-row.el-row", 3).find("h5")*.text() == ["AVERAGE REVIEW\nCOUNT", "AVERAGE REVIEW\nRATING"]
        boolean loaded4 = $(".stats-row.el-row", 4).find("h5")*.text() == ["AVERAGE\nSALES", "AVERAGE\nREVENUE", "AVERAGE\nPRICE"]
        boolean loaded5 = $(".stats-row.el-row", 5).find("h5")*.text() == ["AVERAGE REVIEW\nCOUNT", "AVERAGE REVIEW\nRATING"]
        return loaded2 && loaded3 && loaded4 && loaded5
    }

    def transcribe(marketIntelligenceResult) {
        marketIntelligenceResult['details_top10AverageSales'] = top10AverageSales.text()
        marketIntelligenceResult['details_top10AverageRevenue'] = top10AverageRevenue.text()
        marketIntelligenceResult['details_top10AveragePrice'] = top10AveragePrice.text()
        marketIntelligenceResult['details_top10AverageReviewCount'] = top10AverageReviewCount.text()
        marketIntelligenceResult['details_top10AverageReviewRating'] = top10AverageReviewRating.text()

        marketIntelligenceResult['details_page1AverageSales'] = page1AverageSales.text()
        marketIntelligenceResult['details_page1AverageRevenue'] = page1AverageRevenue.text()
        marketIntelligenceResult['details_page1AveragePrice'] = page1AveragePrice.text()
        marketIntelligenceResult['details_page1AverageReviewCount'] = page1AverageReviewCount.text()
        marketIntelligenceResult['details_page1AverageReviewRating'] = page1AverageReviewRating.text()
    }
}
