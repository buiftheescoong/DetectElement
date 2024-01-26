package org.example;

import java.text.BreakIterator;
import java.util.Vector;


public class Main {

  public static void main(String[] args) {
    String input = "user_name Login_Button";
    // "What is your gender?"

    BreakIterator boundary = BreakIterator.getWordInstance();
    boundary.setText(input);

    int start = boundary.first();
    Vector<String> wordsAndPunctuation = new Vector<>();
    for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
      String wordOrPunctuation = input.substring(start, end);
      if (!wordOrPunctuation.matches("\\s*") && !wordOrPunctuation.matches("\\p{Punct}")) {
        wordsAndPunctuation.add(wordOrPunctuation);
      }
    }
    String normalize_result = String.join(" ", wordsAndPunctuation);
    System.out.println(normalize_result);

    }

}