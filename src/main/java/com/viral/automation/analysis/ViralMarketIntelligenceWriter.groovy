package com.viral.automation.analysis

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter

class ViralMarketIntelligenceWriter {

    static void appendAnalysisIntoCsv(final String filePath, final LinkedHashMap analysis) {
        final CSVPrinter printer = new CSVPrinter(new FileWriter(filePath, true), CSVFormat.EXCEL)
        try {
            printer.printRecord(analysis.values())
        } catch (IOException e) {
            e.printStackTrace()
        } finally {
            printer.close()
        }
    }

    static void createFileAndWriteHeadersIntoCsv(final String filePath, final LinkedHashMap analysis) {
        final CSVPrinter printer = new CSVPrinter(new FileWriter(filePath), CSVFormat.EXCEL)
        try {
            printer.printRecord(analysis.keySet())
        } catch (IOException e) {
            e.printStackTrace()
        } finally {
            printer.close()
        }
    }
}
