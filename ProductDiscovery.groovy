/**
 * You must install ChromeDriver V72 locally!
 * https://chromedriver.storage.googleapis.com/index.html
 * move driver to '/usr/local/bin/chromedriver'
 *
 * This file is for bash only. It's an entry point for bash which can be execute with the following command:
 * groovy --classpath "./src/main/java" ProductDiscovery.groovy <terms.csv/term> <preset-name> <pages-to-transcribe>
 * If you want to use a file containing a list of terms to search, there are example files in the folder "product_discovery_terms"
 * e.g. groovy --classpath "./src/main/java" ProductDiscovery.groovy 'product_discovery_terms/test.terms' low-barrier-v1 3
 *
 * You can execute Product Discovery with an Intellij target as well (which comes with debugging capabilities).
 * However, the entry point is a different file:
 * Target's Main class: com.viral.automation.ProductDiscoveryMain
 * Target's Working directory: (root of git folder, e.g. /Users/lidavid/workspace/ViralAutomation
 * Target's Program arguments: <viral-launch-email> <viral-launch-password> <terms.csv/term> <preset-name> <pages-to-transcribe>
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
        @Grab("org.seleniumhq.selenium:selenium-support:3.14.0")
])

import com.viral.automation.main.Log

import static com.viral.automation.main.ProductDiscoveryMain.executeProductDiscovery

class ProductDiscovery {

    static void main(String... args) {

        clearScreen()
        exitIfInsufficientArguments(args)

        final String[] credentials = getCredentials()
        final String USERNAME = credentials[0]
        final String PASSWORD = credentials[1]

        final String SEARCH_TERM = args[0]
        final String PRESET_NAME = args[1]
        final String PAGES_TO_TRANSCRIBE = args[2]

        final Set<String> discoverResults = executeProductDiscovery(USERNAME, PASSWORD, SEARCH_TERM, PRESET_NAME, PAGES_TO_TRANSCRIBE)
        final String resultsFileName = writeFile(discoverResults)

        Log.success("Product Discovery executed successfully! Results @ ${resultsFileName}")
    }

    private static exitIfInsufficientArguments(String... args) {
        if (args.length != 3) {
            clearScreen()
            Log.error "ProductDiscovery.groovy <terms.csv/term> <preset-name> <pages-to-transcribe>"
            System.exit(0)
        }
    }

    private static getCredentials() {
        return new File("./david.credentials").text.split('\n')
    }

    private static void clearScreen() {
        for (int i = 0; i < 50; ++i) System.out.println();
    }

    static String writeFile(Set<String> discoverResults) {
        final String now = new Date().format("yyyy_MM_dd-HH_mm_ss", TimeZone.getTimeZone('America/Los_Angeles'))
        final String fileName = "./product_discovery_result_" + now + ".result"
        def output = new File(fileName)
        for (String result : discoverResults) {
            output.append(result + "\n")
        }
        return fileName
    }
}
