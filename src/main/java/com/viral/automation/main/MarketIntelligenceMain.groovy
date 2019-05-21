package com.viral.automation.main

import com.viral.automation.analysis.ViralMarketIntelligenceWriter
import com.viral.automation.authentication.ViralLogin
import com.viral.automation.marketintelligence.ViralMarketIntelligence
import geb.Browser

class MarketIntelligenceMain {

    private static final BLACKLISTED_TERMS = // case-insensitive
            [
               "Aquaphor", "Alani Nu", "Adidas", "Amoxicillin", "Avengers", "Allegra", "American Ninja Warrior", "Alvababy", "Amika",
               "biore", "Bey Blade", "Beyblade", "Bablades", "Ben 10", "Bayblades", "Blade Blade",
               "charizard", "channel", "Coq10", "Cialis", "Clinique", "Calvin Klein", "Clorox", "Coast Flashlight", "Cottonelle", "Crest ",
               "Disney", "Dixie", "Dreft", "Dove shampoo",
               // e
               "Funko", "Fortnite", "Fabreeze",
               "Gladiator Garageworks", "Game Of Thrones", "Giorgio", "Glade", "Gopro", "Garmin",
               "Huggies", "Hello toothpaste", "Huk Shirt", "hulk",
               "itzy ritzy", "Invicta", "Iron Man", "Iphone", "Imodium", "It's A 10",
               "johnny b", "Jolyn", "j6", "j8", "Juul", "Jag Jeans",
               "Kyocera", "Kenmore", "Kiehls", "Kleenex",
               "Lexus", "Lenox", "Lego", "Lilly Pulitzer", "Lily Pulitzer",
               "Marvel", "Macbook", "Monster Jam", "Matrix Shampoo",
               "nike", "nintendo", "neutrogena", "Nyquil", "Nitraflex", "Nutrisystem",
               "Omeprazole", "Orijen", "One Touch Verio", "OneTouch Verio",
               "ps3", "ps4", "Playtex", "Pqq", "Puffs", "Pepcid", "Pampers", "Pledge", "Pokemon", "Petzl", "Pittsburgh Pirates", "Pure Romance", "Purex",
               "Que Water", "Quick Fix Synthetic Urine",
               "Reuzel", "ryan toy", "ryans toy", "Rebook", "Reebok", "Rhino Pills",
               "samsung", "Sebastian Shaper", "Redken", "Snorerx", "Spiderman", "Seventh Generation Toilet Paper", "Steve Madden", "silent mind",
               "Tampax", "Torino Pro", "tide ", "Taylormade", "Thompson Tee", "The Kind Pen",
               "Ubiquinol", "Under Armour", "UnderArmour",
               "Vera Bradley",
               "Warhammer", "Wolverine", "Woodland", "Windex",
               "xbox",
               // y
               "Zyrtec", "Zantac",
               "303 fabric guard"
            ]

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

        if (BLACKLISTED_TERMS.any { term.toUpperCase().contains(it.toUpperCase()) }) {
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
