package com.viral.automation.modules

import com.viral.automation.Log
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

        Log.success("Opened Standard View.")
    }
}
