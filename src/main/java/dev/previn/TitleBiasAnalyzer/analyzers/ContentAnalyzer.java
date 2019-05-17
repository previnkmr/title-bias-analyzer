package dev.previn.TitleBiasAnalyzer.analyzers;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.Properties;

public class ContentAnalyzer {

    public static int getContentSentimentRating(final String content) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        //StanfordCoreNLP
        int totalRate = 0;
        String[] contentArr = content.split("\\.");
        for (String sentence : contentArr) {
            if (sentence != null) {
                Annotation annotation = pipeline.process(sentence);
                for (CoreMap sentenceAnnotation : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                    Tree tree = sentenceAnnotation.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                    int score = RNNCoreAnnotations.getPredictedClass(tree);
                    totalRate += score;
                }
            }
        }
        return totalRate / contentArr.length;
    }
}
