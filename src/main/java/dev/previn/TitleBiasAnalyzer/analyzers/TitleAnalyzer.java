package dev.previn.TitleBiasAnalyzer.analyzers;

import dev.previn.TitleBiasAnalyzer.utils.NLPUtils;
import org.apache.commons.lang.StringUtils;

public class TitleAnalyzer {

    public static double getTitleSentimentRating(final String title) {
        return !StringUtils.isEmpty(title) ? NLPUtils.getAverageSentenceSentiment(title) : 0;
    }

    public static String getSubject(final String title) {
        return !StringUtils.isEmpty(title) ? NLPUtils.getSubject(title) : null;
    }
}
