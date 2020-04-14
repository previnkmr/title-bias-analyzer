package dev.previn.titlebiasanalyzer.utils;

import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class NLPUtils {
    //Supreme Court Deals Blow to Apple in Antitrust Case.
    private enum PipelineType { SENTIMENT, KBP }
    // https://stanfordnlp.github.io/CoreNLP/annotators.html
    // https://stanfordnlp.github.io/CoreNLP/sentiment.html
    private static final StanfordCoreNLP SENTIMENT_ANALYSIS_PIPELINE
            = new StanfordCoreNLP(getPipelineProperties(PipelineType.SENTIMENT));
    // https://stanfordnlp.github.io/CoreNLP/kbp.html
    private static final StanfordCoreNLP KBP_ANALYSIS_PIPELINE
            = new StanfordCoreNLP(getPipelineProperties(PipelineType.KBP));

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

    public static String getSubject(final String startSentence) {
        final CoreDocument document = new CoreDocument(startSentence);
        KBP_ANALYSIS_PIPELINE.annotate(document);
        final CoreSentence sentence = document.sentences().get(0);
//        printEverything(document, sentence);
        final List<RelationTriple> relationTripleList = document.sentences().get(0).relations();
        if (relationTripleList == null || relationTripleList.size() == 0) {
            return null;
        }
        final List<CoreLabel> subjects = relationTripleList.get(0).subject;
        if (subjects == null || subjects.size() == 0) {
            return null;
        }
        System.out.println(subjects.get(0));
        return subjects.get(0).lemma();
    }

    private static void printEverything(final CoreDocument document, final CoreSentence sentence) {
        // list of the part-of-speech tags for the second sentence
        List<String> posTags = sentence.posTags();
        System.out.println("Example: pos tags");
        System.out.println(posTags);
        System.out.println();

        // list of the ner tags for the second sentence
        List<String> nerTags = sentence.nerTags();
        System.out.println("Example: ner tags");
        System.out.println(nerTags);
        System.out.println();

        // constituency parse for the second sentence
        Tree constituencyParse = sentence.constituencyParse();
        System.out.println("Example: constituency parse");
        System.out.println(constituencyParse);
        System.out.println();

        // dependency parse for the second sentence
        SemanticGraph dependencyParse = sentence.dependencyParse();
        System.out.println("Example: dependency parse");
        System.out.println(dependencyParse);
        System.out.println();

        // kbp relations found in fifth sentence
//        List<RelationTriple> relations =
//        sentence.relations();
//        System.out.println("Example: relation");
//        System.out.println(relations.get(0));
//        System.out.println();

        // entity mentions in the second sentence
        List<CoreEntityMention> entityMentions = sentence.entityMentions();
        System.out.println("Example: entity mentions");
        System.out.println(entityMentions);
        System.out.println();

        // coreference between entity mentions
        CoreEntityMention originalEntityMention = sentence.entityMentions().get(1);
        System.out.println("Example: original entity mention");
        System.out.println(originalEntityMention);
        System.out.println("Example: canonical entity mention");
        System.out.println(originalEntityMention.canonicalEntityMention().get());
        System.out.println();

        // get document wide coref info
        Map<Integer, CorefChain> corefChains = document.corefChains();
        System.out.println("Example: coref chains for document");
        System.out.println(corefChains);
        System.out.println();
    }

    private static Properties getPipelineProperties(final PipelineType type ) {
        final Properties props = new Properties();
        switch (type) {
            case SENTIMENT:
                props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
                break;
            case KBP:
                props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, depparse, coref, kbp");
                props.setProperty("coref.algorithm", "neural");
                break;
            default:
                // empty props
        }
        return props;
    }
}
