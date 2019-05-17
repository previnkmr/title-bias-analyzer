package dev.previn.TitleBiasAnalyzer;

import dev.previn.TitleBiasAnalyzer.analyzers.ContentAnalyzer;
import dev.previn.TitleBiasAnalyzer.analyzers.TitleAnalyzer;

import java.util.Scanner;

public class TitleBiasAnalyzerApplication {

    public static void main(final String[] args) {
        final Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the title of the article: ");
        final String title = scanner.nextLine();
        System.out.println("Enter the content of the article: ");
        final String content = scanner.nextLine();
        final int titleRating = TitleAnalyzer.getTitleSentimentRating(title);
        final int contentRating = ContentAnalyzer.getContentSentimentRating(content);

        System.out.println("Title Rating: " + titleRating);
        System.out.println("Content Rating: " + contentRating);
        System.out.println(titleBiasText(titleRating, contentRating));
    }

    private static String titleBiasText(final int titleRating, final int contentRating) {
        final int diff = titleRating - contentRating;
        if (diff > 0) {
            return "The title appears positively biased compared to the content";
        } else if (diff < 0) {
            return "The title appears negatively biased compared to the content";
        } else {
            return "The title does not appear biased compared to the content";
        }
    }
}

/// get subject of title, get lemma of subject
/// get sentiment of title
/// split content into sentences filter sentences without lemma
/// average sentiment of those sentences
/// if no subject then compare title sentiment to content sentiment