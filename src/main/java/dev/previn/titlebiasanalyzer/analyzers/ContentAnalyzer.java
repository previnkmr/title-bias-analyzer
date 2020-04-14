package dev.previn.titlebiasanalyzer.analyzers;

import dev.previn.titlebiasanalyzer.utils.NLPUtils;
import org.apache.commons.lang.StringUtils;

public class ContentAnalyzer {

    public static double getContentSentimentRating(final String content) {
        if (StringUtils.isEmpty(content)) {
            return 0;
        }
        double totalSentiment = 0;
        final String[] contentArr = content.split("\\.");
        for (String sentence : contentArr) {
            if (sentence != null) {
                totalSentiment += NLPUtils.getAverageSentenceSentiment(sentence);
            }
        }
        return contentArr.length > 0 ? totalSentiment / contentArr.length : 0;
    }
}
