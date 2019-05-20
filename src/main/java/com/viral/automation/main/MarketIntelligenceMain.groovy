package com.viral.automation.main

import com.viral.automation.analysis.ViralMarketIntelligenceAnalyzer
import com.viral.automation.authentication.ViralLogin
import com.viral.automation.marketintelligence.ViralMarketIntelligence
import geb.Browser

import static groovy.json.JsonOutput.prettyPrint
import static groovy.json.JsonOutput.toJson

class MarketIntelligenceMain {

    private static final BLACKLISTED_TERMS = // case-insensitive
            [
               "Aquaphor", "Alani Nu", "Adidas", "Amoxicillin", "Avengers", "Allegra", "American Ninja Warrior", "Alvababy", "Amika",
               "biore", "Bey Blade", "Beyblade", "Bablades", "Ben 10", "Bayblades", "Blade Blade",
               "charizard", "channel", "Coq10", "Cialis", "Clinique Acne", "Calvin Klein", "Clorox", "Coast Flashlight", "Cottonelle", "Crest ",
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
               "Warhammer", "Wolverine", "Woodland",
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

        final LinkedHashMap intelligence = executeMarketIntelligence(EMAIL, PASSWORD, TERM)
        prettyPrint(toJson(intelligence))
   }

    // This is a method serving a different file (the endpoint for bash). In bash we return the result to be written to a file.
    static LinkedHashMap executeMarketIntelligence(final String email, final String password, final String termOrFilename) {

        final List<String> terms = resolveInput(termOrFilename)
        // Comment above & uncomment below to test in IntlliJ using custom list of terms.
        // final List<String> terms = Arrays.asList("rose gold cups", "nike", "maternity jeans")

        def marketIntelligenceResults = [:]
        def blacklistedTerms = []

        final List<Browser> browsers = new ArrayList<>()
        browsers.add ViralLogin.launch(email, password)
        for (String term : terms) {
            if (isValid(term)) {
                browsers.add ViralMarketIntelligence.searchAndRecord(term, marketIntelligenceResults)
            } else {
                blacklistedTerms.add(term)
            }
        }
        browsers.each { it.quit() }

        final LinkedHashMap analysis = ViralMarketIntelligenceAnalyzer.analyzeProducts(marketIntelligenceResults)

        logBlacklistedTerms(blacklistedTerms)

        return analysis
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
        try {
            final File fileContainingTerms = new File(termOrFilename)
            final String rawTerms = fileContainingTerms.getText('UTF-8')
            return rawTerms.split('\n')
        } catch (FileNotFoundException e) {
            return Arrays.asList(termOrFilename)
        }
    }
}
