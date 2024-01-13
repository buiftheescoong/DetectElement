package Elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
public class RadioButton extends ProcessDetectElement {
  Map<Pair<String, String>, Pair<String, String>> mapStorePairGroupNameAndValueForPairTextAndChoice = new HashMap<>();
  Map<String, Element> mapIdAttributeAndRadioButtonElement = new HashMap<>();
  Map<Pair<String,String>, Element> mapStoreRadioButtonElementForPairTextAndChoice = new HashMap<>();
  Map<String, List<String>> mapStoreTextAndChoices = new HashMap<>();


  public static void main(String[] args) {

  }

  public class Pair<F, S> {

    private  F first;
    private  S second;

    public Pair() {

    }
    public Pair(F first, S second) {
      this.first = first;
      this.second = second;
    }

    public F getFirst() {
      return first;
    }

    public S getSecond() {
      return second;
    }
  }

  public Map<Pair<String, String>, Pair<String, String>> processDetectRadioButtonElement(Map<String, List<String>> mapTextAndChoices, String linkHtml) {
    mapStoreTextAndChoices = mapTextAndChoices;
    String htmlContent = getHtmlContent(linkHtml);
    Document domTree = getDomTree(htmlContent);
    Elements childRoot = domTree.children();
    for (Element child : childRoot) {
      traversalDomTree(child);
    }
    return mapStorePairGroupNameAndValueForPairTextAndChoice;
  }

  public void traversalDomTree(Element e) {
    String text = e.ownText();
    if (!text.isEmpty() && mapStoreTextAndChoices.containsKey(text)) {
        searchChoiceCorrespondingToText(e.parent(), text);
    }
    for (Element child : e.children()) {
      traversalDomTree(child);
    }
  }

    /** Tìm lựa chọn ứng với câu hỏi trong cây con. */
  public void searchChoiceCorrespondingToText(Element root, String text) {
    if (isRadioButtonElement(root)) {
      mapIdAttributeAndRadioButtonElement.put(root.attr("id"), root);
    }
    String textInCurrentElement = root.ownText();
    if (checkChoiceInListChoicesOfText(text, textInCurrentElement)) {
      String forAttribute = root.attr("for");
      Element radioButtonElement = mapIdAttributeAndRadioButtonElement.get(forAttribute);
      Pair<String, String> pairTextAndChoice = new Pair<>(text, textInCurrentElement);
      mapStoreRadioButtonElementForPairTextAndChoice.put(pairTextAndChoice, radioButtonElement);
      detectGroupNameAndValueCorrespondingToTextAndChoice(pairTextAndChoice, radioButtonElement);
    }
    for (Element child : root.children()) {
      searchChoiceCorrespondingToText(child, text);
    }
  }
  public boolean isRadioButtonElement(Element e) {
    return e.tagName().equals("input") && e.hasAttr("type")
        && e.attr("type").equals("radio");
  }

  /** Kiểm tra xem câu hỏi có lựa chọn hiện tại hay không. */
  public boolean checkChoiceInListChoicesOfText(String text, String choice) {
    List<String> list = mapStoreTextAndChoices.get(text);
    return list.contains(choice);
  }

  /** Tìm group name và thuộc tính value ứng với cặp câu hỏi và lựa chọn đang có. */
  public void detectGroupNameAndValueCorrespondingToTextAndChoice(Pair<String,String> pairTextAndChoice, Element e) {
    Pair<String, String> pairGroupNameAndValue = new Pair<>(e.attr("name"), e.attr("value"));
    mapStorePairGroupNameAndValueForPairTextAndChoice.put(pairTextAndChoice, pairGroupNameAndValue);
  }

}
