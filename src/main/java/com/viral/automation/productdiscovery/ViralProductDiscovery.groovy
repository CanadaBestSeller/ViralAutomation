package com.viral.automation.productdiscovery

import com.viral.automation.main.ChromeBrowserProvider
import com.viral.automation.main.Log
import geb.Browser
import geb.Module
import geb.Page

import static org.codehaus.groovy.runtime.StackTraceUtils.*

class ViralProductDiscovery {

    static Browser discoverAndRecord(String discoverTerm, int numberOfPagesToTranscribe, Set<String> productDiscoveryResults) {

        def params = [marketplace: "US", discoverTerm: discoverTerm]
        Log.info "***** Discovering products with $params *****\n"

        def browser = ChromeBrowserProvider.get()
        browser.drive {

            to ViralProductDiscoveryPage
            waitFor(30, massage: "Waiting for keywordField") { keywordField }

            presets.openLastPreset()  // See PresetsModule for documentation

            keywordField.value(discoverTerm)  // set value of keyword field to discoverTerm
            waitFor(30, message: "Clicking submit button...") { submitButton.click() }

            try {
                keywordResults.transcribe(productDiscoveryResults, numberOfPagesToTranscribe)

            } catch (Throwable t) {
                final StringWriter stringWriter = new StringWriter()
                t.printStackTrace(new PrintWriter(stringWriter))
                Log.error("Failed to retrieve keyword results for ${discoverTerm}: " + stringWriter.toString())
            }
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
        presets { module PresetsModule }
    }
}

/**
 * We are using some keyboard magic to select the last preset in the preset menu. Some of the parameters we're controlling are:
 * - Keyword Contains: This we're defaulting to EMPTY because this way, ViralLaunch won't search
 * (You can attach a keyword to a preset, if you do, then every time a preset is selected, ViralLaunch searches the word.
 * We want to avoid this since we want to input search terms via automation).
 * - Average price range
 * - Average review count range
 * - Average net profit range
 * - Estimated search volume range
 */
class PresetsModule extends Module {
    static content = {
        dropdownButton { $("input", 1, placeholder: "Select a Preset") }
    }

    void openLastPreset() { // It's too time-consuming to code to open a specific preset for now, let's just open the last one.
        waitFor(30, message: "Opening preset dropdown...") { dropdownButton.click() }
        interact { sendKeys(org.openqa.selenium.Keys.UP) }
        interact { sendKeys(org.openqa.selenium.Keys.RETURN) }
    }
}

class KeywordResultModule extends Module {

    static content = {
        listings { $("span.keyword-item__name.mb5.trunc-text.result__title")*.text() }
        nextButton { $("button.btn-next", 0) }
        sortBySalesToReviewsButton { $("div.text-center.header-item.column-lg", 4) }
    }

    def transcribe(final Set<String> productDiscoveryResults, final int numberOfPagesToTranscribe) {
        waitForListingToLoad()

        waitFor(30, message: "Sort salesToReview ASC") { sortBySalesToReviewsButton.click() }  // First click sorts ascending
        waitFor(30, message: "Sort salesToReview DESC") { sortBySalesToReviewsButton.click() }  // Second click sorts descending

        for (int i = 0; i < numberOfPagesToTranscribe; i++) {
            productDiscoveryResults.addAll(listings)
            waitFor(5, message:"Clicking next button...") { nextButton.click() }
            waitForListingToLoad()
        }
    }

    void waitForListingToLoad() {
        waitFor(60) { listings }
        waitFor(60) { listings.any {it != ""} }
    }
}
