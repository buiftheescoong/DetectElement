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


public class SelectDropDownList extends ProcessDetectElement {

  Map<String, List<String>> mapStoreTextAndChoices = new HashMap<>();
  Vector<Element> selectElements = new Vector<>();
  Map<Pair<String,String>, Pair<Element,Element>> mapStoreTextAndChoiceToSelectAndOptionElement = new HashMap<>();
  Map<Pair<String, String>, Pair<String, String >> mapStoreLocatorOfSelectElementAndValueForPairTextAndChoice = new HashMap<>();


  public static void main(String[] args) {
  }
  public Map<Pair<String, String>, Pair<String, String>> processDetectDropdownList(Map<String, List<String>> mapTextAndChoices, String linkHtml) {
    mapStoreTextAndChoices = mapTextAndChoices;
    String htmlContent = getHtmlContent(linkHtml);
    Document domTree = getDomTree(htmlContent);
    Elements childRoot = domTree.children();

    for (Element child : childRoot) {
      traversalDomTree(child);
    }

    return mapStoreLocatorOfSelectElementAndValueForPairTextAndChoice;

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

  public void traversalDomTree(Element e) {
    if (!e.ownText().isEmpty() && mapStoreTextAndChoices.containsKey(e.ownText())) {
      String text = e.ownText();
      traversalSubtree(e.parent(), text);
    }
    if (isSelectElement(e) && !selectElements.contains(e)) {
      storeMapTextAndChoiceToSelectAndOptionElement(e,"",e);
    }
    for (Element child : e.children()) {
      traversalDomTree(child);
    }
  }

  public boolean isSelectElement(Element e) {
    return e.tagName().equals("select");
  }

  private void storeMapTextAndChoiceToSelectAndOptionElement(Element root,String text, Element selectElement) {
    String tagNameCurrentElement = root.tagName();
    String textInCurrentElement = root.ownText();
    if (tagNameCurrentElement.equals("option") && mapStoreTextAndChoices.get(text).contains(textInCurrentElement)) {
      Pair<String, String> pairTextAndChoice = new Pair<>(text, textInCurrentElement);
      Pair<Element, Element> pairSelectAndOptionElement = new Pair<>(selectElement, root);
      mapStoreTextAndChoiceToSelectAndOptionElement.put(pairTextAndChoice, pairSelectAndOptionElement);
      detectLocatorOfSelectElementAndValueOptionCorrespondingToTextAndChoice(pairTextAndChoice, selectElement, root);
    }
    for (Element child :root.children()) {
      storeMapTextAndChoiceToSelectAndOptionElement(child, text, selectElement);
    }
  }


  public void traversalSubtree(Element root, String text) {
    if (isSelectElement(root)) {
      selectElements.add(root);
      storeMapTextAndChoiceToSelectAndOptionElement(root, text, root);
    } else {
      for (Element child : root.children()) {
        traversalSubtree(child, text);
      }
    }
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

  public void detectLocatorOfSelectElementAndValueOptionCorrespondingToTextAndChoice(Pair<String, String> pairTextAndChoice, Element select, Element option) {
    Pair<String, String> pairLocatorOfSelectElementAndVaLue = new Pair<>(getXpath(select),
        option.attr("value"));
    mapStoreLocatorOfSelectElementAndValueForPairTextAndChoice.put(pairTextAndChoice, pairLocatorOfSelectElementAndVaLue);
  }
}


