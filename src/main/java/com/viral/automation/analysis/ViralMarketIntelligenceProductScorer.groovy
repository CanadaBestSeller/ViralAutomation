package com.viral.automation.analysis

import com.viral.automation.main.Log

class ViralMarketIntelligenceProductScorer {

    private static final int BUDGET_IN_DOLLARS = 40_000

    /**
     * *Adds scores to an input product analysis.
     * Uses a predicate-based scoring system. Every predicate has a score associated.
     * @param p input productAnalysis. *This method will mutate the productAnalysis to add entries for score*
     */
    private static void calculateFinalScore(p) {
        double finalScore = 0;

        // Predicates to be evaluated
        finalScore += evaluate('HAS_BUDGET', BUDGET_IN_DOLLARS > p['analysis_2.5xInventoryCost'], 1, 0)
        
        finalScore += evaluate('TOP_10_AT_LEAST_3_SUB_3K_BSR', p['analysis_top10Sub3000BsrCount'] >= 3, 1, 0)
        finalScore += evaluate('TOP_10_AT_LEAST_3_SUB_10K_BSR', p['analysis_top10Sub10000BsrCount'] >= 3, 1, 0)
        finalScore += evaluate('EXACT_SEARCH_AT_LEAST_3K', p['analysis_exactSearches'] >= 3000, 1, 0)
        finalScore += evaluate('TOP_10_AVG_MONTLY_SALES_AT_LEAST_300', p['analysis_top10AverageMonthlySales'] >= 300, 1, 0)
        
        finalScore += evaluate('TOP_10_AVG_REVIEW_LESS_THAN_250', p['analysis_top10AverageAverageReviewCount'] < 250, 1, 0)
        finalScore += evaluate('PAGE_1_LESS_THAN_3_AMZN', p['analysis_amznListingCount'] < 3, 1, 0)
        finalScore += evaluate('PAGE_1_DOMINATING_BRAND_LESS_THAN_4_LISTINGS', p['analysis_dominatingBrandListingCount'] < 4, 1, 0)

        // This condition might be too harsh. 5K profit for 25th percentile is a lot
        finalScore += evaluate('PAGE_1_75_PERCENT_PROFIT_OVER_5K', p['analysis_25PercentileMonthlyProfit'] >= 3, 1, 0)

        finalScore += evaluate('NON_SEASONAL_NON_TRENDY', !p['analysis_isSeasonal'] , 1, 0)

        finalScore += evaluate('COMPULSIVE_PRICE_RANGE_BETWEEN_15_AND_50', p['analysis_averagePrice'] >= 15, 0.5, 0)
        finalScore += evaluate('COMPULSIVE_PRICE_RANGE_BETWEEN_15_AND_50', p['analysis_averagePrice'] <= 50, 0.5, 0)

        finalScore += evaluate('UNIT_MARGIN_AT_LEAST_8_DOLLARS', p['analysis_unitMargin'] >= 8, 1, 0)
        finalScore += evaluate('UNIT_MARGIN_PERCENTAGE_AT_LEAST_30', p['analysis_unitMarginPercentage'] >= 30, 1, 0)


        p['finalScore'] = finalScore
    }

    /**
     * Convenience method to wrap ternary predicates to execute addtional logic
     * @param conditionName name of the condition to be logged
     * @param score should already be evaluated by ternary statement
     * @return the same score as the input
     */
    private static int evaluate(
            final String conditionName,
            final boolean condition,
            final double successScore,
            final double failureScore
    ) {
        double score = condition ? successScore : failureScore
        Log.info("${condition ? '[X] ' : '[ ]'} ${conditionName.padLeft(60)} Points applied: ${score.toString()}.")
        return score
    }
}
