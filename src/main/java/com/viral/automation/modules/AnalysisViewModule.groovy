package com.viral.automation.modules

import geb.Module

class AnalysisViewModule extends Module {

    static content = {
        moduleTab { $("div#tab-vlAnalysis", 0) }
        productComment {}
        possibleMonthlySales {}
        reviewsNeeded {}
        salesPattern {}
    }

    void open() {
        moduleTab.click()
    }
}
