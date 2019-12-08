package com.viral.automation.analysis

import com.viral.automation.main.Log

class ViralMarketIntelligenceProductScorer {

    private static final int BUDGET_IN_DOLLARS = 40_000

    /**
     * *Adds scores to an input product analysis.
     * Uses a predicate-based scoring system. Every predicate has a score associated.
     * @param i stands for input. We will return a new map using information from i.
     */
    static addFinalScoreAndReasons(final LinkedHashMap i, final String productName) {
        double finalScore = 0

        LinkedHashMap o = [:]  // o stands for output, this is the map we're returning.

        o['Product Name'] = productName
        Log.info("===== SCORE CALCULATION for ${productName} =====")

        o['Score'] = 0  // init with 0 as a placeholder for now, since LinkedHashMap keeps insertion order.

        // Predicates to be evaluated
        finalScore += evaluateCondition(o, "Has Budget (${BUDGET_IN_DOLLARS})", BUDGET_IN_DOLLARS > i['analysis_2.5xInventoryCost'], 1, 0)
        
        finalScore += evaluateCondition(o, 'Top 10 Has 3+ Listings < 10K BSR', i['analysis_top10Sub10000BsrCount'] >= 3, 1, 0)
        finalScore += evaluateCondition(o, 'Exact Search 3K+', i['analysis_exactSearches'] >= 3000, 1, 0)
        finalScore += evaluateCondition(o, 'Top 10 Avg Selling 300+/month', i['analysis_top10AverageMonthlySales'] >= 300, 1, 0)

        finalScore += evaluateCondition(o, 'Unit Margin $5+', i['analysis_unitMargin'] >= 5, 1, 0)
        finalScore += applyPoints(o, "Profit Margin is ${i['analysis_unitMarginPercentage'].round(2)} (+1 per +10% above 30%)", calculateMarginPercentagePoints(i['analysis_unitMarginPercentage']))

        // This condition might be too harsh. 5K profit for 25th percentile is a lot
        finalScore += evaluateCondition(o, 'Most (75%) on page 1 - Profit $1500+', i['analysis_25PercentileMonthlyProfit'] >= 1500, 1, 0)
        finalScore += evaluateCondition(o, 'Half on page 1 - Profit $3000+', i['analysis_medianMonthlyProfit'] >= 3000, 1, 0)

        finalScore += evaluateCondition(o, 'Top 10 Reviews 300-', i['analysis_top10AverageAverageReviewCount'] <= 300, 1, 0)
        finalScore += evaluateCondition(o, 'Dominating Brand Listings 3-', i['analysis_dominatingBrandListingCount'] <= 3, 1, 0)
        finalScore += evaluateCondition(o, 'Page 1 AMZN Listings 3-', i['analysis_amznListingCount'] <= 2, 1, 0)

        finalScore += evaluateCondition(o, 'Non Seasonal / Non Trendy', !i['analysis_isSeasonal'] , 1, 0)

        finalScore += evaluateCondition(o, 'Compulsive Price Range - over $15', i['analysis_averagePrice'] >= 15, 0.5, 0)
        finalScore += evaluateCondition(o, 'Compulsive Price Range - under $50', i['analysis_averagePrice'] <= 50, 0.5, 0)

        finalScore += applyPoints(o, 'The smallness of the product (max 3 points)', calculateSummedVolumePoints(i['analysis_productSummedVolume']))

        o['Score'] = finalScore

        for (Map.Entry entry : i) {
            o[entry.key] = entry.value
        }

        Log.success("===== FINAL SCORE: ${finalScore} =====")

        return o
    }

    /**
     * Convenience method to wrap ternary predicates to execute additional logic
     * @param conditionName name of the condition to be logged
     * @param score should already be evaluated by ternary statement
     * @return the same score as the input
     */
    private static int evaluateCondition(
            final LinkedHashMap o,
            final String conditionName,
            final boolean condition,
            final double successScore,
            final double failureScore
    ) {
        double score = condition ? successScore : failureScore
        Log.info("${condition ? '[X]' : '[ ]'} ${conditionName.padRight(50)} Points applied: ${score.toString()}")
        o[conditionName] = condition ? 'True' : 'False'
        return score
    }

    /**
     * Convenience method to add/subtract points based on input
     * @param conditionName name of the condition to be logged
     * @param score should already be evaluated by ternary statement
     * @return the same score as the input
     */
    private static double applyPoints(
            final LinkedHashMap o,
            final String description,
            final double score
    ) {
        Log.info("${score > 0 ? '[+]' : '[-]'} ${description.padRight(50)} Points applied: ${score.toString()}")
        o[description] = score
        return score
    }

    /**
     * TODO CALIBRATE
     * Calculates a score to represent how optimal the product is, according to the margin percentage
     */
    private static double calculateMarginPercentagePoints(final double marginPercentage) {

        // Calibration here is according to the 9 commandments. 30% is normal and results in 0 points
        // Anything less than 30% results in proportionally negative points. Eg) 10% => -2 points
        // Anything more than 30% results in proportionally positive points. Eg) 50% => 2 points

        if (marginPercentage == 30) {
            return 0d.round(2)
        }

        final double adjustedPoint = (marginPercentage - 30d) / 10d
        return adjustedPoint.round(2)
    }

    /**
     * TODO CALIBRATE
     * Calculate a score based on the dimensions of the product. The smaller the better.
     * The max dimensions of largeStandardSize (18in + 14in + 8in) will return a score of 0
     * Any less than the largeStandardSize will incur negative points
     * Max points is 3
     * @param rawProductInfo
     * @return
     */
    private static double calculateSummedVolumePoints(final Double summedVolume) {

        final Double LARGEST_STANDARD_SIZE = 40d

        Double result
        if (summedVolume == LARGEST_STANDARD_SIZE) {
            result = 0d.round(2)
        } else {
            result =  (LARGEST_STANDARD_SIZE - summedVolume) / LARGEST_STANDARD_SIZE * 3
        }

        return result.round(2)
    }
}
