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
        int finalScore = 0;

        def hasBudget = { it[]}

        // Predicates to be evaluated
        finalScore += evaluate('HAS_BUDGET', BUDGET_IN_DOLLARS > p['analysis_2.5xInventoryCost'], 1, 0)
        finalScore += evaluate('TOP_10_ITEMS_AT_LEAST_3_SUB_3K_BSR', p['analysis_top10Sub3000BsrCount'] >= 3, 1, 0)
        finalScore += evaluate('TOP_10_ITEMS_AT_LEAST_3_SUB_10K_BSR', p['analysis_top10Sub10000BsrCount'] >= 3, 1, 0)

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
            final int successScore,
            final int failureScore
    ) {
        int score = condition ? successScore : failureScore
        Log.debug(String.format(conditionName + " " + condition ? 'succeeded' : 'failed' + ". Points applied: " + score.toString()))
        return score
    }
}
