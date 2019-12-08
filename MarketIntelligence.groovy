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
@Grapes([
        @Grab("org.gebish:geb-core:2.3.1"),
        @Grab("org.seleniumhq.selenium:selenium-chrome-driver:3.14.0"),
        @Grab("org.seleniumhq.selenium:selenium-support:3.14.0"),
        @Grab("org.apache.commons:commons-csv:1.6")
])
import com.viral.automation.main.Log

import static com.viral.automation.main.MarketIntelligenceMain.executeMarketIntelligence

class MarketIntelligence {

    static void main(String... args) {

        clearScreen()
        exitIfInsufficientArguments(args)

        final String[] credentials = getCredentials()
        final String USERNAME = credentials[0]
        final String PASSWORD = credentials[1]
        final String SEARCH_TERM = args[0]

        executeMarketIntelligence(USERNAME, PASSWORD, SEARCH_TERM)
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
        for (int i = 0; i < 50; ++i) System.out.println()
    }
}
