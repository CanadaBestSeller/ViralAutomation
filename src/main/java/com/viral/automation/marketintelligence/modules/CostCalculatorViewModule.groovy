package com.viral.automation.marketintelligence.modules

import com.viral.automation.main.Log
import geb.Module

class CostCalculatorViewModule extends Module {

    static content = {
        costCalculatorViewTab { $("div#tab-costCalculator") }

        totalAveragePrice { $("p.el-tooltip.item.product-price", 0) }
        profitPerUnit { $("p.profit", 0) }
        referralFeeCost { $("div.fees-container", 0).$("div.fee-amount", 0) }
        amazonFeesCost { $("div.fees-container", 0).$("div.fee-amount", 1) }
        landedUnitCost { $("div.fees-container", 0).$("div.fee-amount", 2) }
    }

    void open() {
        costCalculatorViewTab.click()
        Log.info("Clicked Cost Calculator tab.")
        waitFor(2) { loaded }
        Log.success("Opened Cost Calculator.")
    }

    boolean isLoaded() {
        return $("div.fees-container", 0).$("p")*.text() ==
                [
                        "Referral Fee",
                        "Amazon Fees",
                        "Landed Unit Cost"
                ]
    }

    def transcribe(marketIntelligenceResult) {
        marketIntelligenceResult['cost_totalAveragePrice'] = totalAveragePrice.text()
        marketIntelligenceResult['cost_profitPerUnit'] = profitPerUnit.text()
        marketIntelligenceResult['cost_landedUnitCost'] = landedUnitCost.text()
        marketIntelligenceResult['cost_amazonFeesCost'] = amazonFeesCost.text()
        marketIntelligenceResult['cost_referralFeeCost'] = referralFeeCost.text()
    }
}
