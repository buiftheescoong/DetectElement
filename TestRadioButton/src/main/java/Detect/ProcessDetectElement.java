package Detect;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public abstract class ProcessDetectElement {

  public String getHtmlContent(String linkHtml) {
    System.setProperty("Webdriver.chrome.driver", "C:\\chromedriver-win64-120\\chromedriver.exe");
    WebDriver driver = new ChromeDriver();
    driver.get(linkHtml);
    String htmlContent = driver.getPageSource();
    driver.quit();
    return htmlContent;
  }

  public Document getDomTree(String htmlContent) {
    Document domTree = Jsoup.parse(htmlContent);
    return domTree;
  }

  /** Lowercase văn bản, tách văn bản thành các từ và dấu câu, xong đó nối lại với nhau mỗi từ hoặc dấu câu cách nhau 1 khoảng trắng. */
  public String normalize(String s) {
    BreakIterator boundary = BreakIterator.getWordInstance();
    String lowercase_s = s.toLowerCase();
    lowercase_s = lowercase_s.trim();
    boundary.setText(lowercase_s);

    int start = boundary.first();
    Vector<String> wordsAndPunctuation = new Vector<>();
    for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
      String wordOrPunctuation = lowercase_s.substring(start, end);
      if (!wordOrPunctuation.matches("\\s*") && !wordOrPunctuation.matches("\\p{Punct}")) {
        wordsAndPunctuation.add(wordOrPunctuation);
      }
    }
    String normalize_result = String.join(" ", wordsAndPunctuation);
    return normalize_result;
  }
  public abstract void traversalDomTree(Element e);

  public void normalizeInputMap(Map<String, List<String>> normalize, Map<String, List<String>> input, Map<Pair<String, String>, Pair<String, String>> storeNormalizePairTextAndChoiceAndIt) {
    for (Map.Entry<String, List<String>> entry : input.entrySet()) {
      String text = entry.getKey();
      String normalizeText = normalize(text);
      List<String> choices = entry.getValue();
      List<String> normalizeChoices = new ArrayList<>();

      for (String choice : choices) {
        String normalizeChoice = normalize(choice);
        normalizeChoices.add(normalizeChoice);
        Pair<String, String> normalizePair = new Pair<>(normalizeText, normalizeChoice);
        Pair<String, String> inputPair = new Pair<>(text, choice);
        storeNormalizePairTextAndChoiceAndIt.put(normalizePair, inputPair);
      }
      normalize.put(normalizeText, normalizeChoices);
    }
  }
}
