package dev.previn.TitleBiasAnalyzer.analyzers;

import dev.previn.TitleBiasAnalyzer.utils.NLPUtils;

public class TitleAnalyzer {

    public static double getTitleSentimentRating(final String title) {
        return NLPUtils.getAverageSentenceSentiment(title);
    }

    public static String getSubject(final String title) {
        return null;
    }
}
