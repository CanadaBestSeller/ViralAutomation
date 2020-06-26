package com.viral.automation.analysis;

import static com.viral.automation.analysis.ViralMarketIntelligenceProductScorer.calculatePointsForTop10AverageReviewsVolume;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ViralMarketIntelligenceProductScorerTest {

    @Test
    public void calculatePointsForTop10AverageReviewsVolume_50_maxScore() {
        assertEquals(3d, calculatePointsForTop10AverageReviewsVolume(50d), 0.1d);
    }

    @Test
    public void calculatePointsForTop10AverageReviewsVolume_500_minScore() {
        assertEquals(0d, calculatePointsForTop10AverageReviewsVolume(500d), 0.1d);
    }

    @Test
    public void calculatePointsForTop10AverageReviewsVolume_200_minScore() {
        assertEquals(2d, calculatePointsForTop10AverageReviewsVolume(200d), 0.1d);
    }

    public ViralMarketIntelligenceProductScorerTest() {
        super();
    }
}