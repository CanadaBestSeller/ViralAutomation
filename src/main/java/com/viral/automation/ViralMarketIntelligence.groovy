package com.viral.automation

import geb.Browser
import geb.Module
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
 * 7. Market Trends
 * 8. Viral Launch analysis
 *
 * ### Conveniently, all 8 detailed statistics in component B can be selected via $(".stats-row.el-row")
 * Should this be changed in the future, PLEASE modify the Module classes to preserve code cleanliness :)
 */
class ViralMarketIntelligence {
    static Browser launch(String searchTerm) {
        def requestParams = [marketplace:"US", search:searchTerm]
        Log.info "Analyzing with $requestParams..."

        def window = ChromeBrowserProvider.get()
        window.drive {
            to(requestParams, ViralMarketIntelligencePage)
            waitFor { !$("div.container").hasClass("el-loading-spinner") }
            Log.success "Market intelligence page fully loaded."
        }

        Log.success "Analyzed!"
        return window
    }
}

class ViralMarketIntelligencePage extends Page {
    static url = "https://viral-launch.com/sellers/launch-staging/pages/market-intelligence.html"
    static content = {
        stanardView { module(StandardViewModule) }
        detailedView { module(DetailedViewModule) }
        analysisView { module(AnalysisViewModule) }
        costCalculatorView { module(CostCalculatorViewModule) }
    }
}

class StandardViewModule extends Module {
}

class DetailedViewModule extends Module {
    static content = {
        tab {}
    }
}

class AnalysisViewModule extends Module {
    static content = {
        tab { $("div#tab-vlAnalysis", 0) }
        productComment {  }
        possibleMonthlySales {  }
        reviewsNeeded {  }
        salesPattern {  }
    }
    void open() {
        tab.click()
    }
}

class CostCalculatorViewModule extends Module {

}

