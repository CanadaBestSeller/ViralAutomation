package com.viral.automation.analysis

import com.google.common.math.Quantiles
import com.viral.automation.main.Log

class ViralMarketIntelligenceAnalyzer {

    /**
     * Analyzes results. analyze() method expects something like the following:
     *
     * {
     *     "product A": {
     *         "standard_top10BsrList": [
     *             "2,399", ...
     *         ],
     *         "standard_page1BsrList": [
     *             "2,399", ...
     *         ],
     *         "details_top10AverageSales": "608.2",
     *         "details_top10AverageRevenue": "$15,173.76",
     *         "details_top10AveragePrice": "$24.64",
     *         ...
     *     },
     *
     *     "product B": {
     *         "standard_top10BsrList": [
     *             "2,399", ...
     *         ],
     *         ...
     *     }, ...
     * }
     */
    static analyzeProducts(productToMarketIntelligenceMap) {
        def productAnalysis = [:]
        productToMarketIntelligenceMap.each {
            product, productMarketIntelligence -> productAnalysis[product] = analyzeProduct(productMarketIntelligence)
        }
        return productAnalysis
    }

    private static analyzeProduct(productInfo) {

        def productAnalysis = [:]

        def page1AverageSales = toDouble(productInfo['details_page1AverageSales'])
        def landedCost = toDouble(productInfo['calc_landedUnitCost'])
        def amazonFees = toDouble(productInfo['calc_amazonFeesCost'])
        def referralFees = toDouble(productInfo['calc_referralFeeCost'])
        def averagePrice = toDouble(productInfo['calc_totalAveragePrice'])
        def averageCost = landedCost + amazonFees + referralFees
        def averageProfit = toDouble(productInfo['calc_profitPerUnit'])

        productAnalysis['averagePrice'] = averagePrice
        productAnalysis['averageCost'] = averageCost
        productAnalysis['2.5xInventoryCost'] = page1AverageSales * averageCost * 100

        productAnalysis['unitMargin'] = averageProfit
        productAnalysis['unitMarginPercentage'] = averageProfit / averagePrice

        productAnalysis['top10AverageMonthlySales'] = toDouble(productInfo['details_top10AverageSales'])

        final List<Double> monthlyProfit = toDoubleList(productInfo['standard_netMonthlyProfit'])
        productAnalysis['medianMonthlyProfit'] = Quantiles.percentiles().index(50).compute(monthlyProfit)
        productAnalysis['25PercentileMonthlyProfit'] = Quantiles.percentiles().index(25).compute(monthlyProfit)

        productAnalysis['top10AverageAverageReviewCount'] = toDouble(productInfo['details_top10AverageReviewCount'])

        final List<Double> top10bsrDouble = toDoubleList(productInfo['standard_top10BsrList'])
        final List<Double> top10sub3000bsrDouble = top10bsrDouble.findAll { it <= 3000 }
        productAnalysis['top10Sub3000BsrCount'] = top10sub3000bsrDouble.size()

        final List<Double> top10sub10000bsrDouble = top10bsrDouble.findAll { it <= 10000 }
        productAnalysis['top10Sub10000BsrCount'] = top10sub10000bsrDouble.size()

        final List<Double> averagePage1Bsr = toDoubleList(productInfo['standard_page1BsrList'])
        productAnalysis['averagePage1Bsr'] = averagePage1Bsr.sum() / averagePage1Bsr.count { it != null }

        productAnalysis['exactSearches'] = toDouble(productInfo['estimatedSearchVolume'])

        productAnalysis['amznListingCount'] = productInfo['standard_page1SoldByList'].count { it == "AMZN" }

        productAnalysis['dominatingBrandListingCount'] = maxFrequency(productInfo['standard_page1BrandList'])

        productAnalysis['isSeasonal'] = productInfo['analysis_tipContent'].contains("season")
        productAnalysis['isTrend'] = productInfo['analysis_tipContent'].contains("trend")

        productAnalysis['finalScore'] = calculateFinalScore(productAnalysis)
        return productAnalysis
    }

    private static List<Double> toDoubleList(final List<String> input) {
        return input.collect { toDouble(it) }.findAll { it != null }
    }

    private static Double toDouble(final String input) {
        if (input == null || input == "") {
            return null
        }

        return input.replace(',', '').replace('$', '').toDouble()
    }

    private static double maxFrequency(List<String> inputList) {
        List<String> inputListClone = new ArrayList<>()
        for(String s : inputList) {
            inputListClone.add(s);
        }

        List<String> uniqueList = inputList.unique() // Unique is a mutative method
        List<Integer> uniqueOccurrenceList = new ArrayList<>();
        for (String s : uniqueList) {
            Log.debug("Occurrence for brand '" + s + ':"')
            int frequency = Collections.frequency(inputListClone, s)
            uniqueOccurrenceList.add(frequency)
            Log.debug(frequency.toString())
        }
        return Collections.max(uniqueOccurrenceList)
    }

    private static calculateFinalScore(productAnalysis) {
        return 0
    }
}
