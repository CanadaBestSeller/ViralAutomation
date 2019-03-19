package com.viral.automation.modules

import com.viral.automation.Log
import geb.Module

class DetailedViewModule extends Module {

    static content = {
        topSellersTab { $("div#tab-topSellers", 0) }
        detailedStatisticsSubTab { $("div#tab-1", 0) }

        top10AverageSales { $(".stats-row.el-row", 2).$("p", 0).text() }
        top10AverageRevenue { $(".stats-row.el-row", 2).$("p", 1).text() }
        top10AveragePrice { $(".stats-row.el-row", 2).$("p", 2).text() }
        top10AverageReviewCount { $(".stats-row.el-row", 3).$("p", 0).text() }
        top10AverageReviewRating { $(".stats-row.el-row", 3).$("p", 1).text() }

        page1AverageSales { $(".stats-row.el-row", 4).$("p", 0).text() }
        page1AverageRevenue { $(".stats-row.el-row", 4).$("p", 1).text() }
        page1AveragePrice { $(".stats-row.el-row", 4).$("p", 2).text() }
        page1AverageReviewCount { $(".stats-row.el-row", 5).$("p", 0).text() }
        page1AverageReviewRating { $(".stats-row.el-row", 5).$("p", 1).text() }
    }

    void openTopSellersTab() {
        topSellersTab.click()
        Log.info("Clicked Top Sellers tab.")

        waitFor(2) { detailedStatisticsSubTab.displayed }

        detailedStatisticsSubTab.click()
        Log.success("Opened Detailed Statistics.")
    }

    boolean isDetailedStatisticsSubTabLoaded() {
        boolean loaded2 = $(".stats-row.el-row", 2).find("h5")*.text() == ["AVERAGE\nSALES", "AVERAGE\nREVENUE", "AVERAGE\nPRICE"]
        boolean loaded3 = $(".stats-row.el-row", 3).find("h5")*.text() == ["AVERAGE REVIEW\nCOUNT", "AVERAGE REVIEW\nRATING"]
        boolean loaded4 = $(".stats-row.el-row", 4).find("h5")*.text() == ["AVERAGE\nSALES", "AVERAGE\nREVENUE", "AVERAGE\nPRICE"]
        boolean loaded5 = $(".stats-row.el-row", 5).find("h5")*.text() == ["AVERAGE REVIEW\nCOUNT", "AVERAGE REVIEW\nRATING"]
        return loaded2 && loaded3 && loaded4 && loaded5
    }
}
