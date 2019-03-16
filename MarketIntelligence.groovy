// You must install ChromeDriver locally!
// brew install chromedriver
// move driver to '/usr/local/bin/chromedriver'

// Dependencies need to be declared here, as well as in the POM file.
// By doing so, you will be able to execute this file via either bash or IntelliJ
// Bash command:
// groovy --classpath "./src/main/java" MarketIntelligence.groovy 'SEARCH TERM'
@Grapes([
        @Grab("org.gebish:geb-core:2.3.1"),
        @Grab("org.seleniumhq.selenium:selenium-chrome-driver:3.14.0"),
        @Grab("org.seleniumhq.selenium:selenium-support:3.14.0")
])

import com.viral.automation.ViralMain
import com.viral.automation.Log

class MarketIntelligence {

    static void main(String... args) {

        exitIfNoArguments(args)

        clearScreen()
        String SEARCH_TERM = args[0]
        Log.info "Executing market intelligence for '${SEARCH_TERM}'..."

        String[] credentials = getCredentials()
        ViralMain.main(credentials[0], credentials[1], SEARCH_TERM)
    }

    private static exitIfNoArguments(String... args) {
        if (args.length == 0) {
            clearScreen()
            Log.error "Please enter the search term in quotation marks!"
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
}

