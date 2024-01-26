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


public class SelectDropDownList extends ProcessDetectElement {

  Map<String, List<String>> mapStoreNormalizeTextAndChoices = new HashMap<>();
  Vector<Element> selectElements = new Vector<>();
  Map<Pair<String,String>, Pair<Element,Element>> mapStoreTextAndChoiceToSelectAndOptionElement = new HashMap<>();
  Map<Pair<String, String>, Pair<String, String >> mapStoreLocatorOfSelectElementAndValueForPairTextAndChoice = new HashMap<>();

  Map<Pair<String, String>, Pair<String, String>> storeNormalizePairTextAndChoiceAndIt = new HashMap<>();

  public static void main(String[] args) {
  }
  public Map<Pair<String, String>, Pair<String, String>> processDetectDropdownList(Map<String, List<String>> mapTextAndChoices, String linkHtml) {
    normalizeInputMap(mapStoreNormalizeTextAndChoices, mapTextAndChoices, storeNormalizePairTextAndChoiceAndIt);
    String htmlContent = getHtmlContent(linkHtml);
    Document domTree = getDomTree(htmlContent);
    Elements childRoot = domTree.children();

    for (Element child : childRoot) {
      traversalDomTree(child);
    }

    return mapStoreLocatorOfSelectElementAndValueForPairTextAndChoice;

  }


  public void traversalDomTree(Element e) {
    String text = e.ownText();
    String normalizeText = normalize(text);
    if (!normalizeText.isEmpty() && mapStoreNormalizeTextAndChoices.containsKey(normalizeText)) {
      traversalSubtree(e.parent(), normalizeText);
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
    String normalizeTextInCurrentElement = normalize(textInCurrentElement);
    if (tagNameCurrentElement.equals("option") && mapStoreNormalizeTextAndChoices.get(text).contains(normalizeTextInCurrentElement)) {
      Pair<String, String> normalizePairTextAndChoice = new Pair<>(text, normalizeTextInCurrentElement);
      Pair<String, String> pairTextAndChoice = storeNormalizePairTextAndChoiceAndIt.get(normalizePairTextAndChoice);
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


