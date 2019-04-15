package com.viral.automation.productdiscovery

import com.viral.automation.main.ChromeBrowserProvider
import com.viral.automation.main.Log
import geb.Browser
import geb.Module
import geb.Page

class ViralProductDiscovery {

    static Browser discoverAndRecord(discoverTerm, productDiscoveryResults, int numberOfPagesToTranscribe) {

        def params = [marketplace: "US", discoverTerm: discoverTerm]
        Log.info "Discovering products with $params..."

        def browser = ChromeBrowserProvider.get()
        browser.drive {

            to ViralProductDiscoveryPage

            waitFor(30) { keywordField }
            keywordField.value(discoverTerm)  // set value of keyword field to discoverTerm
            waitFor(30, message:"Clicking submit button...") { submitButton.click() }

            keywordResults.open()
            keywordResults.transcribe(productDiscoveryResults, numberOfPagesToTranscribe)
        }

        return browser
    }
}

class ViralProductDiscoveryPage extends Page {
    static url = "https://viral-launch.com/sellers/launch-staging/pages/product-discovery.html#/filter"

    static content = {
        keywordField { $(placeholder: 'Keyword Phrase', 0) }
        submitButton { $("button.el-button.filter-button.el-button--warning.is-plain", 2) }
        keywordResults { module KeywordResultModule }
    }
}

class KeywordResultModule extends Module {

    static content = {
        listings { $("span.keyword-item__name.mb5.trunc-text.result__title")*.text() }
        nextButton { $("button.btn-next", 0) }
    }

    def void open() {
        waitFor(30) { listings }
        waitFor(30) { listings.every {it != ""} }
    }

    def transcribe(final List<String> productDiscoveryResults, final int numberOfPagesToTranscribe) {
        for (int i = 0; i < numberOfPagesToTranscribe; i++) {
            productDiscoveryResults.addAll(listings)
            waitFor(5, message:"Clicking next button...") { nextButton.click() }
            open()
        }
    }
}
