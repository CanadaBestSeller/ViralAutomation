package com.viral.automation.marketintelligence.modules

import com.viral.automation.main.Log
import geb.Module

class CostCalculatorViewModule extends Module {

    static content = {
        costCalculatorViewTab { $("div#tab-costCalculator") }

        totalAveragePrice { $("p.el-tooltip.item.product-price", 0) }

        dimensions { $("ul.product-specs", 0) }  // Eg) Unit Weight: 1 lb \n Dimensions: 10 X 2 X 14 in

        profitPerUnit { $("p.profit", 0) }
        referralFeeCost { $("div.fee-amount", 0) }
        amazonFeesCost { $("div.fee-amount", 1) }
        landedUnitCost { $("div.fee-amount", 2) }
    }

    void open() {
        costCalculatorViewTab.click()
        Log.debug("Clicked Cost Calculator tab.")
        waitFor(2) { loaded }
        waitFor(20) { !referralFeeCost.text().isEmpty() }
        Log.info("Opened Cost Calculator.")
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
        marketIntelligenceResult['raw_calc_totalAveragePrice'] = totalAveragePrice.text()
        marketIntelligenceResult['raw_calc_profitPerUnit'] = profitPerUnit.text()
        marketIntelligenceResult['raw_calc_landedUnitCost'] = landedUnitCost.text()
        marketIntelligenceResult['raw_calc_amazonFeesCost'] = amazonFeesCost.text()
        marketIntelligenceResult['raw_calc_referralFeeCost'] = referralFeeCost.text()

        def explodedDimensions = dimensions.text().split()

        marketIntelligenceResult['raw_calc_totalAverageWeightInPounds'] = explodedDimensions[2]
        marketIntelligenceResult['raw_calc_widthInInches'] = explodedDimensions[5]
        marketIntelligenceResult['raw_calc_heightInInches'] = explodedDimensions[7]
        marketIntelligenceResult['raw_calc_lengthInInches'] = explodedDimensions[9]
    }
}
