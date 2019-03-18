package com.viral.automation

import com.viral.automation.modules.AnalysisViewModule
import com.viral.automation.modules.CostCalculatorViewModule
import com.viral.automation.modules.DetailedViewModule
import com.viral.automation.modules.MarketTrendsModule
import com.viral.automation.modules.StandardViewModule
import geb.Page

class ViralMarketIntelligencePage extends Page {
    static url = "https://viral-launch.com/sellers/launch-staging/pages/market-intelligence.html"

    static content = {
        stanardView { module StandardViewModule }
        detailedView { module DetailedViewModule }
        marketTrendsView { module MarketTrendsModule }
        analysisView { module AnalysisViewModule }
        costCalculatorView { module CostCalculatorViewModule }
    }
}
