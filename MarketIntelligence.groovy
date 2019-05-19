/**
 * You must install ChromeDriver V72 locally!
 * https://chromedriver.storage.googleapis.com/index.html
 * move driver to '/usr/local/bin/chromedriver'
 *
 * This file is for bash only. It's an entry point for bash which can be execute with the following command:
 * groovy --classpath "./src/main/java" MarketIntelligence.groovy <search-term-in-quotes>
 *
 * You can execute Product Discovery with an Intellij target as well (which comes with debugging capabilities).
 * However, the entry point is a different file:
 * Target's Main class: com.viral.automation.MarketIntelligenceMain
 * Target's Working directory: (root of git folder, e.g. /Users/lidavid/workspace/ViralAutomation
 * Target's Program arguments: <viral-launch-email> <viral-launch-password> <search-term-in-quotes>
 *
 * Output of this groovy file will be in this top-level folder.
 *
 * *** IMPORTANT ***
 * Dependencies need to be declared here, as well as in the POM file.
 * By doing so, you will be able to execute this file via either bash or IntelliJ.
 */


import com.viral.automation.analysis.ViralMarketIntelligenceWriter
import com.viral.automation.main.Log
@Grapes([
        @Grab("org.gebish:geb-core:2.3.1"),
        @Grab("org.seleniumhq.selenium:selenium-chrome-driver:3.14.0"),
        @Grab("org.seleniumhq.selenium:selenium-support:3.14.0"),
        @Grab("org.apache.commons:commons-csv:1.6")
])

import com.viral.automation.main.MarketIntelligenceMain
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter

class MarketIntelligence {

    static void main(String... args) {

        clearScreen()
        exitIfInsufficientArguments(args)

        final String[] credentials = getCredentials()
        final String USERNAME = credentials[0]
        final String PASSWORD = credentials[1]
        final String SEARCH_TERM = args[0]

        final LinkedHashMap analyses = MarketIntelligenceMain.executeMarketIntelligence(USERNAME, PASSWORD, SEARCH_TERM)
        ViralMarketIntelligenceWriter.writeAnalysisToFile(getCsvFileName(), analyses)
    }

    private static exitIfInsufficientArguments(String... args) {
        if (args.length != 1) {
            clearScreen()
            Log.error "Usage: MarketIntelligence.groovy <search-term-in-quotes>"
            System.exit(0)
        }
    }

    private static getCredentials() {
        final String[] credentials = new File("./david.credentials").text.split('\n')
        credentials
    }

    private static void clearScreen() {
        for (int i = 0; i < 50; ++i) System.out.println();
    }

    private static String getCsvFileName() {
        final String now = new Date().format("yyyy_MM_dd-HH_mm_ss", TimeZone.getTimeZone('America/Los_Angeles'))
        return "./market_intelligence_result_" + now + ".csv"
    }

    static writeAnalysisToFile(final String filePath, final LinkedHashMap analyses) {
        try {
            CSVPrinter printer = new CSVPrinter(new FileWriter(filePath), CSVFormat.EXCEL)
            printer.printRecord(getHeaders(analyses))

            for (LinkedHashMap.Entry entry : analyses.entrySet()) {
                printer.printRecord(entry.getValue())
                printer.println()
            }

        } catch (IOException e) {
            e.printStackTrace()
        }
    }

    static Collection getHeaders(final LinkedHashMap analyses) {
        final LinkedHashMap.Entry<String, LinkedHashMap> firstEntry = analyses.entrySet()[0]
        final LinkedHashMap firstAnalysis = firstEntry.getValue()
        return firstAnalysis.values()
    }
}
