/**
 * This file is for bash only. It's an entry point for bash which can be execute with the following command:
 * groovy --classpath "./src/main/java" BrandFilter.groovy <filepath>
 */

import com.viral.automation.main.Log
import com.viral.automation.marketintelligence.modules.Blacklist

class BrandFilter {

    static void main(String... args) {

        clearScreen()
        exitIfInsufficientArguments(args)

        final Set<String> result = new HashSet<>()

        final String filepath = args[0]
        final List<String> inputTerms = new File(filepath).getText().split('\n')*.trim()

        for (String term : inputTerms) {
            if (term == null || term.isEmpty() || term.trim().isEmpty()) {
                Log.info("Filtered because term is null/empty")
                continue
            }

            if (Blacklist.BLACKLISTED_TERMS.any { term.toUpperCase().contains(it.toUpperCase()) }) {
                Log.info("Filtered because term is BLACKLISTED")
                continue
            }

            if (Blacklist.ALREADY_ANALYZED_PRODUCTS.any { (term.toUpperCase().trim() == it.toUpperCase().trim()) }) {
                Log.info("Filtered because term is ALREADY DONE")
                continue
            }
            result.add(term)
        }

        final String resultsFileName = writeFile(result)
        Log.success("Brand Filter executed successfully! Results @ ${resultsFileName}")
    }

    private static exitIfInsufficientArguments(String... args) {
        if (args.length != 1) {
            clearScreen()
            Log.error "BrandFilter.groovy <filepath>"
            System.exit(0)
        }
    }

    private static void clearScreen() {
        for (int i = 0; i < 50; ++i) System.out.println()
    }

    static String writeFile(Set<String> inputTerms) {
        final String now = new Date().format("yyyy-MM-dd___HH-mm-ss", TimeZone.getTimeZone('America/Los_Angeles'))
        final String fileName = "./brand_filter_result_" + now + ".result"
        def output = new File(fileName)
        for (String result : inputTerms) {
            output.append(result + "\n")
        }
        return fileName
    }
}
