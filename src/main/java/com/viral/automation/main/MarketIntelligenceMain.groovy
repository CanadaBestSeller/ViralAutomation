package com.viral.automation.main

import com.viral.automation.analysis.ViralMarketIntelligenceWriter
import com.viral.automation.authentication.ViralLogin
import com.viral.automation.marketintelligence.ViralMarketIntelligence
import com.viral.automation.marketintelligence.modules.Blacklist
import geb.Browser

class MarketIntelligenceMain {

    // This is an entry point for IntelliJ target only
    static void main(String... args) {

        final String EMAIL = args[0]
        final String PASSWORD = args[1]
        final String TERM = args[2]

        executeMarketIntelligence(EMAIL, PASSWORD, TERM, true)  // IntelliJ target should always be test mode
   }

    static LinkedHashMap executeMarketIntelligence(final String email, final String password, final String termOrFilename) {
        return executeMarketIntelligence(email, password, termOrFilename, false)
    }

    // This is a method serving a different file (the endpoint for bash). In bash we return the result to be written to a file.
    static void executeMarketIntelligence(final String email, final String password, final String termOrFilename, final boolean isTestMode) {

        final List<String> terms = isTestMode ? Arrays.asList(termOrFilename, "nike", "rose gold cups") : resolveInput(termOrFilename)

        def csvFilePath = getCsvFilePath()
        final List<Browser> browsers = new ArrayList<>()

        final List<String> validatedTerms = validateAndFilter(terms)
        for (int i = 0; i < validatedTerms.size(); i++) {
            def result = [:]
            if (i == 0) {
                browsers.add ViralLogin.launch(email, password)
                browsers.add ViralMarketIntelligence.searchAndRecord(validatedTerms[i], result, isTestMode)
                ViralMarketIntelligenceWriter.createFileAndWriteHeadersIntoCsv(csvFilePath, result)
                ViralMarketIntelligenceWriter.appendAnalysisIntoCsv(csvFilePath, result)
            } else {
                browsers.add ViralMarketIntelligence.searchAndRecord(validatedTerms[i], result, isTestMode)
                ViralMarketIntelligenceWriter.appendAnalysisIntoCsv(csvFilePath, result)
            }
            Log.success("${validatedTerms[i]} Market Intelligence results added to ${csvFilePath}")
        }

        browsers.each { it.quit() }

        Log.success("ALL DONE! Market Intelligence executed successfully! Results @ ${csvFilePath}")
    }

    static List<String> validateAndFilter(terms) {
        def blacklistedTerms = []

        final List<String> result = new ArrayList<>()
        for (String term : terms) {
            if (isValid(term)) {
                result.add(term)
            } else {
                blacklistedTerms.add(term)
            }
        }

        logBlacklistedTerms(blacklistedTerms)
        return result
    }

    static void logBlacklistedTerms(final List<String> blacklistedTerms) {
        if (blacklistedTerms.empty) {
            return
        }

        Log.warn("==== Some terms have been SKIPPED due to containing *blacklisted* words =====")
        for (String term : blacklistedTerms) {
            Log.warn("SKIPPED: ${term}")
        }
        Log.warn("==== END OF SKIPPED TERMS =====\n")
    }

    static boolean isValid(final String term) {
        if (term == null || term.isEmpty() || term.trim().isEmpty()) {
            return false
        }

        if (Blacklist.BLACKLISTED_TERMS.any { term.toUpperCase().contains(it.toUpperCase()) }) {
            return false
        }

        if (Blacklist.ALREADY_ANALYZED_PRODUCTS.any { (term.toUpperCase().trim() == it.toUpperCase().trim()) }) {
            return false
        }

        return true
    }

    static List<String> resolveInput(final String termOrFilename) {
        if (termOrFilename.contains(',')) {
            return termOrFilename.split(',')*.trim()
        }
        try {
            final File fileContainingTerms = new File(termOrFilename)
            final String rawTerms = fileContainingTerms.getText('UTF-8')
            return rawTerms.split('\n')
        } catch (FileNotFoundException e) {
            return Arrays.asList(termOrFilename)
        }
    }

    private static String getCsvFilePath() {
        final String now = new Date().format("yyyy-MM-dd___HH-mm-ss", TimeZone.getTimeZone('America/Los_Angeles'))
        return "./market_intelligence_result_" + now + ".csv"
    }
}
