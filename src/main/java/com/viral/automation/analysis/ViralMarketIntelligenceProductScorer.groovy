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
        finalScore += evaluate(o, "Has Budget (${BUDGET_IN_DOLLARS})", BUDGET_IN_DOLLARS > i['analysis_2.5xInventoryCost'], 1, 0)
        
        finalScore += evaluate(o, 'Top 10 Has 3+ Listings < 10K BSR', i['analysis_top10Sub10000BsrCount'] >= 3, 1, 0)
        finalScore += evaluate(o, 'Exact Search 3K+', i['analysis_exactSearches'] >= 3000, 1, 0)
        finalScore += evaluate(o, 'Top 10 Avg Selling 300+/month', i['analysis_top10AverageMonthlySales'] >= 300, 1, 0)

        finalScore += evaluate(o, 'Unit Margin $5+', i['analysis_unitMargin'] >= 5, 1, 0)
        finalScore += evaluate(o, 'Profit Margin 40%+', i['analysis_unitMarginPercentage'] >= 40, 1, 0)

        // This condition might be too harsh. 5K profit for 25th percentile is a lot
        finalScore += evaluate(o, 'Most (75%) on page 1 - Profit $1500+', i['analysis_25PercentileMonthlyProfit'] >= 1500, 1, 0)
        finalScore += evaluate(o, 'Half on page 1 - Profit $3000+', i['analysis_medianMonthlyProfit'] >= 3000, 1, 0)

        finalScore += evaluate(o, 'Top 10 Reviews 300-', i['analysis_top10AverageAverageReviewCount'] <= 300, 1, 0)
        finalScore += evaluate(o, 'Dominating Brand Listings 3-', i['analysis_dominatingBrandListingCount'] <= 3, 1, 0)
        finalScore += evaluate(o, 'Page 1 AMZN Listings 3-', i['analysis_amznListingCount'] <= 2, 1, 0)

        finalScore += evaluate(o, 'Non Seasonal / Non Trendy', !i['analysis_isSeasonal'] , 1, 0)

        finalScore += evaluate(o, 'Compulsive Price Range - over $15', i['analysis_averagePrice'] >= 15, 0.5, 0)
        finalScore += evaluate(o, 'Compulsive Price Range - under $50', i['analysis_averagePrice'] <= 50, 0.5, 0)

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
    private static int evaluate(
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
}
