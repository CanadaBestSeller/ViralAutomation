package com.viral.automation.marketintelligence.modules

import com.viral.automation.main.Log
import geb.Module

class StandardViewModule extends Module {

    static content = {
        topSellersTab { $("div#tab-topSellers", 0) }
        standardViewSubTab { $("div#tab-0", 0) }
        productListings { $("div.el-table__body-wrapper.is-scrolling-left", 0).$("table tr").moduleList(Product) }
    }

    void open() {
        topSellersTab.click()
        Log.info("Clicked Top Sellers tab.")

        waitFor(2) { standardViewSubTab.displayed }
        standardViewSubTab.click()
        Log.info("Clicked Standard View tab.")

        waitFor(10) { productListings }
        Log.success("Opened Standard View.")
    }

    def transcribe(marketIntelligenceResult) {
        marketIntelligenceResult['standard_top10BsrList'] = productListings.getAt(0..9)*.bsr
        marketIntelligenceResult['standard_page1BsrList'] = productListings*.bsr
        marketIntelligenceResult['standard_page1BrandList'] = productListings*.brand
        marketIntelligenceResult['standard_page1SoldByList'] = productListings*.soldBy
        marketIntelligenceResult['standard_netMonthlyProfit'] = productListings*.netProfit
    }
}

class Product extends Module {
    static content = {
        cell { $("td", it) }

        isOutlier { !cell(0).find("label.el-checkbox.is-checked").any() }
        rank { cell(1).$("div.cell.el-tooltip").text() }
        brand { cell(2).$("div.cell.el-tooltip").text() }
        title { cell(3).$("div.cell.el-tooltip").text() }
        category { cell(4).$("div.cell.el-tooltip").text() }
        bsr { cell(5).$("div.cell.el-tooltip").text() }
        bsrTrendPicture { cell(6).$("div.cell.el-tooltip").text() }
        monthlyRevenue { cell(7).$("div.cell.el-tooltip").text() }
        price { cell(8).$("div.cell.el-tooltip").text() }
        unitMargin { cell(9).$("div.cell.el-tooltip").text() }
        monthlySales { cell(10).$("div.cell.el-tooltip").text() }
        reviewQuantity { cell(11).$("div.cell.el-tooltip").text() }
        reviewRate { cell(12).$("div.cell.el-tooltip").text() }
        averageRating { cell(13).$("div.cell.el-tooltip").text() }
        soldBy { cell(14).$("div.cell.el-tooltip").text() }
        salesToReviewsRatio { cell(15).$("div.cell.el-tooltip").text() }
        dateListed { cell(16).$("div.cell.el-tooltip").text() }
        netProfit { cell(17).$("div.cell.el-tooltip").text() }
        last12mSales { cell(18).$("div.cell.el-tooltip").text() }
        last12mRevenue { cell(19).$("div.cell.el-tooltip").text() }
        next12mSales { cell(20).$("div.cell.el-tooltip").text() }
        next12mRevenue { cell(21).$("div.cell.el-tooltip").text() }
    }
}