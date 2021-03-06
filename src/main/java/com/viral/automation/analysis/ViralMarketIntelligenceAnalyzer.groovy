package com.viral.automation.analysis

import com.google.common.math.Quantiles
import com.viral.automation.main.Log

import static groovy.json.JsonOutput.prettyPrint
import static groovy.json.JsonOutput.toJson

class ViralMarketIntelligenceAnalyzer {

    /**
     * Analyzes results. analyze() method expects something like the following:
     *
     * {
     *     "product A": {
     *         "raw_standard_top10BsrList": [
     *             "2,399", ...
     *         ],
     *         "raw_standard_page1BsrList": [
     *             "2,399", ...
     *         ],
     *         "raw_details_top10AverageSales": "608.2",
     *         "raw_details_top10AverageRevenue": "$15,173.76",
     *         "raw_details_top10AveragePrice": "$24.64",
     *         ...
     *     },
     *
     *     "product B": {
     *         "raw_standard_top10BsrList": [
     *             "2,399", ...
     *         ],
     *         ...
     *     }, ...
     * }
     */
    static analyzeProduct(productName, rawProductInfo) {

        LinkedHashMap productAnalysis = [:]

        def page1AverageSales = toDouble(rawProductInfo['raw_details_page1AverageSales'])
        def landedCost = toDouble(rawProductInfo['raw_calc_landedUnitCost'])
        def amazonFees = toDouble(rawProductInfo['raw_calc_amazonFeesCost'])
        def referralFees = toDouble(rawProductInfo['raw_calc_referralFeeCost'])
        def averagePrice = toDouble(rawProductInfo['raw_calc_totalAveragePrice'])
        def averageCost = landedCost + amazonFees + referralFees
        def averageProfit = toDouble(rawProductInfo['raw_calc_profitPerUnit'])

        productAnalysis['Tip'] = rawProductInfo['raw_analysis_tipContent']

        productAnalysis['analysis_averagePrice'] = averagePrice
        productAnalysis['analysis_averageCost'] = averageCost
        productAnalysis['analysis_2.5xInventoryCost'] = page1AverageSales * averageCost * 2.5

        productAnalysis['analysis_unitMargin'] = averageProfit
        productAnalysis['analysis_unitMarginPercentage'] = averageProfit / averagePrice * 100

        productAnalysis['analysis_top10AverageMonthlySales'] = toDouble(rawProductInfo['raw_details_top10AverageSales'])

        final List<Double> monthlyProfit = toDoubleList(rawProductInfo['raw_standard_netMonthlyProfit'])
        productAnalysis['analysis_medianMonthlyProfit'] = Quantiles.percentiles().index(50).compute(monthlyProfit)
        productAnalysis['analysis_25PercentileMonthlyProfit'] = Quantiles.percentiles().index(25).compute(monthlyProfit)

        productAnalysis['analysis_top10AverageAverageReviewCount'] = toDouble(rawProductInfo['raw_details_top10AverageReviewCount'])

        final List<Double> top10bsrDouble = toDoubleList(rawProductInfo['raw_standard_top10BsrList'])
        final List<Double> top10sub3000bsrDouble = top10bsrDouble.findAll { it <= 3000 }
        productAnalysis['analysis_top10Sub3000BsrCount'] = top10sub3000bsrDouble.size()

        final List<Double> top10sub10000bsrDouble = top10bsrDouble.findAll { it <= 10000 }
        productAnalysis['analysis_top10Sub10000BsrCount'] = top10sub10000bsrDouble.size()

        final List<Double> averagePage1Bsr = toDoubleList(rawProductInfo['raw_standard_page1BsrList'])
        productAnalysis['analysis_averagePage1Bsr'] = averagePage1Bsr.sum() / averagePage1Bsr.count { it != null }

        productAnalysis['analysis_exactSearches'] = toDouble(rawProductInfo['estimatedSearchVolume'])

        productAnalysis['analysis_amznListingCount'] = rawProductInfo['raw_standard_page1SoldByList'].count { it == "AMZN" }

        productAnalysis['analysis_dominatingBrandListingCount'] = maxFrequency(rawProductInfo['raw_standard_page1BrandList'])

        productAnalysis['analysis_isSeasonal'] = rawProductInfo['raw_analysis_tipContent'].contains("season")
        productAnalysis['analysis_isTrend'] = rawProductInfo['raw_analysis_tipContent'].contains("trend")

        productAnalysis['analysis_productSummedVolume'] = calculateSummedVolume(rawProductInfo)
        productAnalysis['analysis_isTrend'] = rawProductInfo['raw_analysis_tipContent'].contains("trend")

        Log.info("\n\n" + productName.toUpperCase() + ":\n" + prettyPrint(toJson(productAnalysis)) + "\n")

        return ViralMarketIntelligenceProductScorer.addFinalScoreAndReasons(productAnalysis, productName)
    }

    private static List<Double> toDoubleList(final List<String> input) {
        return input.collect { toDouble(it) }.findAll { it != null }
    }

    private static Double toDouble(final String input) {
        if (input == null || input == "") {
            return null
        }

        return input.replace(',', '').replace('$', '').replace('<', '').toDouble().round(2)
    }

    private static double maxFrequency(List<String> inputList) {
        List<String> inputListClone = new ArrayList<>()
        for(String s : inputList) {
            inputListClone.add(s)
        }

        List<String> uniqueList = inputList.unique() // Unique is a mutative method
        List<Integer> uniqueOccurrenceList = new ArrayList<>()
        for (String s : uniqueList) {
            Log.debug("Occurrence for brand '" + s + ':"')
            int frequency = Collections.frequency(inputListClone, s)
            uniqueOccurrenceList.add(frequency)
            Log.debug(frequency.toString())
        }
        return Collections.max(uniqueOccurrenceList)
    }

    private static double calculateSummedVolume(rawProductInfo) {

        final Double w = Double.parseDouble(rawProductInfo['raw_calc_widthInInches'])
        final Double h = Double.parseDouble(rawProductInfo['raw_calc_heightInInches'])
        final Double l = Double.parseDouble(rawProductInfo['raw_calc_lengthInInches'])

        final Double totalVolume = w + h + l  // Using addition here to make calculation easier
        Log.debug("Total summed volume of product (w + h + l) is ${totalVolume}")

        return totalVolume
    }
}
