package com.viral.automation

import com.viral.automation.modules.AnalysisViewModule
import com.viral.automation.modules.CostCalculatorViewModule
import com.viral.automation.modules.DetailedViewModule
import com.viral.automation.modules.MarketTrendsModule
import com.viral.automation.modules.StandardViewModule
import geb.Browser
import geb.Page

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

    public static final int PAGE_LOAD_TIMEOUT_IN_SECONDS = 20

    static Browser launch(String searchTerm) {
        def requestParams = [marketplace:"US", search:searchTerm]
        Log.info "Analyzing with $requestParams..."

        def window = ChromeBrowserProvider.get()
        window.drive {
            to(requestParams, ViralMarketIntelligencePage)
            waitFor { $("h3.search-phrase", 0).displayed }
            Log.info "Market intelligence page started loading..."

            waitFor(PAGE_LOAD_TIMEOUT_IN_SECONDS) { !$("div.el-loading-mask", 0).displayed }
            Log.success "Market intelligence page fully loaded."

            detailedView.openTopSellersTab()

            waitFor(2) { $("div#tab-1", 0).displayed }
            detailedView.openDetailedStatisticsSubTab()

            sleep(10000)

            Log.success("Opened Detailed Statistics.")
            Log.success "detailedView.top10AverageSales = " + detailedView.top10AverageSales
        }

        Log.success "Analyzed!"
        return window
    }
}


