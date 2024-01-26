package Detect;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Input extends ProcessDetectElement {
  Map<String, Element> mapStoreLocatorAndElement = new HashMap<>();
  Vector<Element> visitedInputElements = new Vector<>();
  Vector<String> locator_input = new Vector<>();
  int countDetectedElements = 0;

  public Vector<String> processDetectInputElement(Vector<String> input, String linkHtml) {
    Vector<String> result = new Vector<>();
    for (String s : input) {
      String normalize_s = normalize(s);
      locator_input.add(normalize_s);
    }
    String htmlContent = getHtmlContent(linkHtml);
    Document domTree = getDomTree(htmlContent);
    Elements childRoot = domTree.children();
    for (Element child : childRoot) {
      traversalDomTree(child);
    }
    if (countDetectedElements < locator_input.size()) {
        for (Element e : visitedInputElements) {
          if (!mapStoreLocatorAndElement.containsValue(e) && locator_input.contains(normalize(e.attr("placeholder")))) {
            mapStoreLocatorAndElement.put(normalize(e.attr("placeholder")), e);
          }
        }

    }
    for (String locator : locator_input) {
      Element e = mapStoreLocatorAndElement.get(locator);
      String locator_value = getXpath(e);
      result.add(locator_value);
    }
    return result;
  }



  @Override
  public void traversalDomTree(Element e) {
    if (isInputElement(e) && !visitedInputElements.contains(e) && !mapStoreLocatorAndElement.containsValue(e)) {
      visitedInputElements.add(e);
    }
    String text = e.ownText();
    String normalize_text = normalize(text);
    if (normalize_text != null && !normalize_text.matches("\\s*")) {
      if (locator_input.size() == 0) {
        return;
      }
      if (locator_input.contains(normalize_text)) {
        String valueOfForAttribute = "";
        if (e.hasAttr("for") && !e.attr("for").matches("\\s*")) {
          valueOfForAttribute = e.attr("for");
        }
        findElementCorrespondToText(e, normalize_text, valueOfForAttribute);
      }
    }

    for (Element child : e.children()) {
      traversalDomTree(child);
    }
  }

  /** Tìm phần tử input từ cây con với gốc là phần tử hiện tại hoặc tổ tiên của nó. */
  public void findElementCorrespondToText(Element e, String text, String valueOfForAttribute) {
    int temp = countDetectedElements;
    findElementInSubtree(e, text, valueOfForAttribute);
    if (temp == countDetectedElements) {
      findElementCorrespondToText(e.parent(), text, valueOfForAttribute);
    }
  }

  /** Tìm phần tử input trong cây con. */
  public void findElementInSubtree(Element root, String text, String valueOfForAttribute) {
    if (isInputElement(root) && !mapStoreLocatorAndElement.containsValue(root)) {
      if (!visitedInputElements.contains(root)) {
        visitedInputElements.add(root);
      }
      if (valueOfForAttribute.isEmpty()) {
        mapStoreLocatorAndElement.put(text, root);
        countDetectedElements++;
        visitedInputElements.remove(root);
        return;
      } else {
        String id = root.attr("id");
         if (id.equals(valueOfForAttribute)) {
           mapStoreLocatorAndElement.put(text, root);
           countDetectedElements++;
           visitedInputElements.remove(root);
           return;
         }
      }
    }
    for (Element child : root.children()) {
      findElementInSubtree(child, text, valueOfForAttribute);
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
      String textOfElement = e.ownText();
      if (havingPreviousAttribute && !textOfElement.matches("\\s*")) {
        xpath += " and " + "normalize-space()=" + "'" + textOfElement + "'";
      } else {
        if (!textOfElement.matches("\\s*")) {
          xpath += "normalize-space()=" + "'" + textOfElement + "'";
        }
      }
      xpath += "]";
    }
    return xpath;
  }

  public boolean isInputElement(Element e) {
    if (e.tagName().equals("input")) {
      if (e.hasAttr("type") && (e.attr("type").equals("submit") || e.attr("type").equals("hidden"))) {
        return false;
      }
      return true;
    }
    if (e.tagName().equals("textarea")) {
      return true;
    }
    return false;
  }

}
