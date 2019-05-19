package com.viral.automation.analysis

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter

class ViralMarketIntelligenceWriter {

    static String writeAnalysisIntoCsv(final String filePath, final LinkedHashMap analyses) {
        final CSVPrinter printer = new CSVPrinter(new FileWriter(filePath), CSVFormat.EXCEL)
        try {
            printer.printRecord(getHeaders(analyses))
            for (LinkedHashMap itemAnalysis: analyses.values()) {
                printer.printRecord(itemAnalysis.values())
            }
        } catch (IOException e) {
            e.printStackTrace()
        } finally {
            printer.close()
        }
        return filePath
    }

    private static Set getHeaders(final LinkedHashMap analyses) {
        def firstEntry = analyses.entrySet().first().getValue()
        return (firstEntry as LinkedHashMap).keySet()
    }
}
