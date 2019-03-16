package com.viral.automation

import geb.Browser

class ViralMarketIntelligence {

    static Browser launch(String searchTerm) {
        Log.info "Analyzing $searchTerm..."

        String encodedSearchTerm = java.net.URLEncoder.encode(searchTerm, "UTF-8")

        def window = ChromeBrowserProvider.get()
        window.drive {
            go "https://viral-launch.com/sellers/launch-staging/pages/market-intelligence.html?marketplace=US&search=" + encodedSearchTerm
            waitFor { !$("div.container").hasClass("el-loading-spinner") }

            Log.success "Market intelligence page fully loaded."
            sleep(10_000)
        }

        Log.success "Analyzed!"
        return window
    }
}

