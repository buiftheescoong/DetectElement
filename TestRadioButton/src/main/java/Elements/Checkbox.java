package Elements;


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
  Map<String, List<String>> mapStoreTextAndChoices = new HashMap<>();

  Map<Pair<String,String>, Element> mapStoreCheckboxElementForPairTextAndChoice = new HashMap<>();
  Map<Pair<String, String>, String> mapStoreLocatorElementForPairTextAndChoice = new HashMap<>();

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

  /** Hàm trả về kết quả là 1 map giữa cặp câu hỏi và lựa chọn với xpath của phần tử checkbox tương ứng. */
  public Map<Pair<String, String>, String> processDetectCheckboxElement(Map<String, List<String>> mapTextAndChoices, String linkHtml) {
    mapStoreTextAndChoices = mapTextAndChoices;
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
    if (!text.isEmpty() && mapStoreTextAndChoices.containsKey(text)) {
      List<Element> listSiblingElements = e.siblingElements();
      if (listSiblingElements.isEmpty()) {
        Element fatherCurrentElement = e.parent();
        searchChoiceCorrespondingToText(fatherCurrentElement.parent(), text);
      } else {
        searchChoiceCorrespondingToText(e.parent(), text);
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
    if (checkChoiceInListChoicesOfText(text, textInCurrentElement)) {
      searchCheckboxElementCorrespondingToTextAndChoice(root.parent(), text, textInCurrentElement);
    }
    for (Element child : root.children()) {
      searchChoiceCorrespondingToText(child, text);
    }
  }

  /** Tìm phần tử checkbox ứng với câu hỏi và lựa chọn đang có. */
  public void searchCheckboxElementCorrespondingToTextAndChoice(Element root, String text, String choice) {
    if (isCheckboxElement(root)) {
      Pair<String, String> pairTextAndChoice = new Pair<>(text, choice);
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
    List<String> list = mapStoreTextAndChoices.get(text);
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
    if (checkChoiceInListChoicesOfText(text, textInCurrentElement)) {
      Pair<String, String> pairTextAndChoice = new Pair<>(text, textInCurrentElement);
      mapStoreCheckboxElementForPairTextAndChoice.put(pairTextAndChoice, checkbox);
      detectLocatorOfElementCorrespondingToTextAndChoice(pairTextAndChoice, checkbox);
      return;
    }
    for (Element child : root.children()) {
      searchChoiceForOrphansCheckboxElement(child, checkbox, text);
    }
  }
}
