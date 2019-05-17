package dev.previn.TitleBiasAnalyzer.utils;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class NLPUtils {


    private static final StanfordCoreNLP SENTIMENT_ANALYSIS_PIPELINE = new StanfordCoreNLP(getPipelineProperties());

    public static double getAverageSentenceSentiment(final String content) {
        Annotation annotation = SENTIMENT_ANALYSIS_PIPELINE.process(content);
        final List<CoreMap> sentenceAnnotations = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        final int[] sentiments = new int[sentenceAnnotations.size()];
        for (int sentenceIndex = 0; sentenceIndex < sentenceAnnotations.size(); sentenceIndex++) {
            CoreMap sentenceAnnotation = sentenceAnnotations.get(sentenceIndex);
            Tree tree = sentenceAnnotation.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            sentiments[sentenceIndex] = RNNCoreAnnotations.getPredictedClass(tree);
        }
        return Arrays.stream(sentiments).average().orElse(0);
    }

    private static Properties getPipelineProperties() {
        final Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        return props;
    }
}
