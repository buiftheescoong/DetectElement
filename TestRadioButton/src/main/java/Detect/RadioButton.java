package Detect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class RadioButton extends ProcessDetectElement {
  Map<Pair<String, String>, Pair<String, String>> mapStorePairGroupNameAndValueForPairTextAndChoice = new HashMap<>();
  Map<String, Element> mapIdAttributeAndRadioButtonElement = new HashMap<>();
  Map<Pair<String,String>, Element> mapStoreRadioButtonElementForPairTextAndChoice = new HashMap<>();
  Map<String, List<String>> mapStoreNormalizeTextAndChoices = new HashMap<>();

  Map<Pair<String, String>, Pair<String, String>> storeNormalizePairTextAndChoiceAndIt = new HashMap<>();

  public static void main(String[] args) {

  }


  public Map<Pair<String, String>, Pair<String, String>> processDetectRadioButtonElement(Map<String, List<String>> mapTextAndChoices, String linkHtml) {
    normalizeInputMap(mapStoreNormalizeTextAndChoices, mapTextAndChoices, storeNormalizePairTextAndChoiceAndIt);
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
    String normalizeText = normalize(text);
    if (!text.isEmpty() && mapStoreNormalizeTextAndChoices.containsKey(normalizeText)) {
        searchChoiceCorrespondingToText(e.parent(), normalizeText);
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
    String normalizeTextInCurrentElement = normalize(textInCurrentElement);
    if (checkChoiceInListChoicesOfText(text, normalizeTextInCurrentElement)) {
      String forAttribute = root.attr("for");
      Element radioButtonElement = mapIdAttributeAndRadioButtonElement.get(forAttribute);
      Pair<String, String> normalizePairTextAndChoice = new Pair<>(text, normalizeTextInCurrentElement);
      Pair<String, String> pairTextAndChoice = storeNormalizePairTextAndChoiceAndIt.get(normalizePairTextAndChoice);
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
    List<String> list = mapStoreNormalizeTextAndChoices.get(text);
    return list.contains(choice);
  }

  /** Tìm group name và thuộc tính value ứng với cặp câu hỏi và lựa chọn đang có. */
  public void detectGroupNameAndValueCorrespondingToTextAndChoice(Pair<String,String> pairTextAndChoice, Element e) {
    Pair<String, String> pairGroupNameAndValue = new Pair<>(e.attr("name"), e.attr("value"));
    mapStorePairGroupNameAndValueForPairTextAndChoice.put(pairTextAndChoice, pairGroupNameAndValue);
  }



}
