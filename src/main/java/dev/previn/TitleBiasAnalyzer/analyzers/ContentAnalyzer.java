package dev.previn.TitleBiasAnalyzer.analyzers;

import dev.previn.TitleBiasAnalyzer.utils.NLPUtils;

public class ContentAnalyzer {

    public static double getContentSentimentRating(final String content) {
        double totalSentiment = 0;
        final String[] contentArr = content.split("\\.");
        for (String sentence : contentArr) {
            if (sentence != null) {
                totalSentiment += NLPUtils.getAverageSentenceSentiment(sentence);
            }
        }
        return totalSentiment / contentArr.length;
    }
}
