package Detect;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class Checkbox extends ProcessDetectElement {
  Vector<Element> visitedCheckboxElement = new Vector<>();
  Map<String, List<String>> mapStoreNormalizeTextAndChoices = new HashMap<>();

  Map<Pair<String,String>, Element> mapStoreCheckboxElementForPairTextAndChoice = new HashMap<>();
  Map<Pair<String, String>, String> mapStoreLocatorElementForPairTextAndChoice = new HashMap<>();
  Map<Pair<String, String>, Pair<String, String>> storeNormalizePairTextAndChoiceAndIt = new HashMap<>();


  /** Hàm trả về kết quả là 1 map giữa cặp câu hỏi và lựa chọn với xpath của phần tử checkbox tương ứng. */
  public Map<Pair<String, String>, String> processDetectCheckboxElement(Map<String, List<String>> mapTextAndChoices, String linkHtml) {
    normalizeInputMap(mapStoreNormalizeTextAndChoices, mapTextAndChoices, storeNormalizePairTextAndChoiceAndIt);
    String htmlContent = getHtmlContent(linkHtml);
    Document domTree = getDomTree(htmlContent);
    Elements childRoot = domTree.children();
    for (Element child : childRoot) {
      traversalDomTree(child);
    }
    return mapStoreLocatorElementForPairTextAndChoice;

  }
  @Override
  public void traversalDomTree(Element e) {
    String text = e.ownText();
    String normalizeText = normalize(text);
    if (!text.isEmpty() && mapStoreNormalizeTextAndChoices.containsKey(normalizeText)) {
      List<Element> listSiblingElements = e.siblingElements();
      if (listSiblingElements.isEmpty()) {
        Element fatherCurrentElement = e.parent();
        searchChoiceCorrespondingToText(fatherCurrentElement.parent(), normalizeText);
      } else {
        searchChoiceCorrespondingToText(e.parent(), normalizeText);
      }
    }

    if (isCheckboxElement(e) && !visitedCheckboxElement.contains(e)) {
      visitedCheckboxElement.add(e);
      searchChoiceForOrphansCheckboxElement(e.parent(), e, "");
    }

    for (Element child : e.children()) {
      traversalDomTree(child);
    }
  }

  public static boolean isCheckboxElement(Element e) {
    return e.tagName().equals("input") && e.hasAttr("type")
        && e.attr("type").equals("checkbox");
  }

  /** Tìm lựa chọn ứng với câu hỏi trong cây con. */
  public void searchChoiceCorrespondingToText(Element root, String text) {
    if (isCheckboxElement(root)) {
      visitedCheckboxElement.add(root);
    }
    String textInCurrentElement = root.ownText();
    String normalizeTextInCurrentElement = normalize(textInCurrentElement);
    if (checkChoiceInListChoicesOfText(text, normalizeTextInCurrentElement)) {
      searchCheckboxElementCorrespondingToTextAndChoice(root.parent(), text, normalizeTextInCurrentElement);
    }
    for (Element child : root.children()) {
      searchChoiceCorrespondingToText(child, text);
    }
  }

  /** Tìm phần tử checkbox ứng với câu hỏi và lựa chọn đang có. */
  public void searchCheckboxElementCorrespondingToTextAndChoice(Element root, String text, String choice) {
    if (isCheckboxElement(root)) {
      Pair<String, String> normalizePairTextAndChoice = new Pair<>(text, choice);
      Pair<String, String>  pairTextAndChoice = storeNormalizePairTextAndChoiceAndIt.get(normalizePairTextAndChoice);
      mapStoreCheckboxElementForPairTextAndChoice.put(pairTextAndChoice, root);
      detectLocatorOfElementCorrespondingToTextAndChoice(pairTextAndChoice, root);
      return;
    }
    for (Element child : root.children()) {
      searchCheckboxElementCorrespondingToTextAndChoice(child, text, choice);
    }
  }

  /** Kiểm tra xem câu hỏi có lựa chọn hiện tại hay không. */
  public boolean checkChoiceInListChoicesOfText(String text, String choice) {
    List<String> list = mapStoreNormalizeTextAndChoices.get(text);
    return list.contains(choice);
  }

  public String getXpath(Element e) {
    int attributes_size = e.attributesSize();
    String xpath = "";
    if (attributes_size > 0) {
      Attributes attr = e.attributes();
      xpath += "//" + e.tagName() + "[";
      boolean havingPreviousAttribute = false;
      for (Attribute temp : attr) {
        if (temp.getKey().equals("pattern")) {
          continue;
        } else {
          if (havingPreviousAttribute) {
            xpath += " and " + "@" + temp.getKey() + "=" + "'" + temp.getValue() + "'";
          } else {
            xpath += "@" + temp.getKey() + "=" + "'" + temp.getValue() + "'";
            havingPreviousAttribute = true;
          }

        }
      }
      xpath += "]";
    }
    return xpath;
  }

  /** Tìm xpath của phần tử checkbox ứng với câu hỏi và lựa chọn đang có. */
  public void detectLocatorOfElementCorrespondingToTextAndChoice(Pair<String, String> pairTextAndChoice, Element e) {
    String locator = getXpath(e);
    mapStoreLocatorElementForPairTextAndChoice.put(pairTextAndChoice, locator);
  }

  /** Tìm lựa chọn ứng với phần tử checkbox cô đơn. */
  public void searchChoiceForOrphansCheckboxElement(Element root, Element checkbox, String text) {
    String textInCurrentElement = root.ownText();
    String normalizeTextInCurrentElement = normalize(textInCurrentElement);
    if (checkChoiceInListChoicesOfText(text, normalizeTextInCurrentElement)) {
      Pair<String, String> normalizePairTextAndChoice = new Pair<>(text, normalizeTextInCurrentElement);
      Pair<String, String> pairTextAndChoice = storeNormalizePairTextAndChoiceAndIt.get(normalizePairTextAndChoice);
      mapStoreCheckboxElementForPairTextAndChoice.put(pairTextAndChoice, checkbox);
      detectLocatorOfElementCorrespondingToTextAndChoice(pairTextAndChoice, checkbox);
      return;
    }
    for (Element child : root.children()) {
      searchChoiceForOrphansCheckboxElement(child, checkbox, text);
    }
  }
}
