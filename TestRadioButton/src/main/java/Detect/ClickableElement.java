package Detect;

import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ClickableElement extends ProcessDetectElement {
  Vector<String> result_locator = new Vector<>();
  Map<String, String> mapStoreNormalizeAndVariableLocator = new HashMap<>();
  Map<String, String> mapStoreVariableLocatorAndValueLocator = new HashMap<>();

  /** Trả về 1 vector value locator theo thứ tự ứng với các variable locator từ input. */
  public Vector<String> processDetectClickableElement(Vector<String> input_variable_locator, String linkHtml) {
     for (String s : input_variable_locator) {
       String normalize_s = normalize(s);
       mapStoreNormalizeAndVariableLocator.put(normalize_s, s);
     }
     String htmlContent = getHtmlContent(linkHtml);
     Document domTree = getDomTree(htmlContent);
     Elements childRoot = domTree.children();
     for (Element e : childRoot) {
       traversalDomTree(e);
     }
     for (String variable_locator : input_variable_locator) {
       result_locator.add(mapStoreVariableLocatorAndValueLocator.get(variable_locator));
     }
     return result_locator;
  }

  @Override
  public void traversalDomTree(Element e) {
    if (isDefaultClickableInput(e)) {
      if (e.hasAttr("value")) {
        String value = e.attr("value");
        String normalizeValue = normalize(value);
        if (mapStoreNormalizeAndVariableLocator.containsKey(normalizeValue)) {
          String variable_locator = mapStoreNormalizeAndVariableLocator.get(normalizeValue);
          String value_locator = getXpath(e);
          mapStoreVariableLocatorAndValueLocator.put(variable_locator, value_locator);
        }
      }
    } else {
      String textInCurrentElement = e.ownText();
      System.out.println(textInCurrentElement);
      System.out.println(e);
      if (!textInCurrentElement.isEmpty()) {
        String normalizeTextInCurrentElement = normalize(textInCurrentElement);
        if (mapStoreNormalizeAndVariableLocator.containsKey(normalizeTextInCurrentElement)) {
          if (isDefaultClickable(e)) {
              String variable_locator = mapStoreNormalizeAndVariableLocator.get(normalizeTextInCurrentElement);
              String value_locator = getXpath(e);
              mapStoreVariableLocatorAndValueLocator.put(variable_locator, value_locator);
          } else {
            Elements siblings = e.siblingElements();
            if (siblings == null || siblings.isEmpty() || isDefaultClickable(e.parent())) {
              Element parent = e.parent();
              String variable_locator = mapStoreNormalizeAndVariableLocator.get(normalizeTextInCurrentElement);
              String value_locator = getXpath(parent);
              mapStoreVariableLocatorAndValueLocator.put(variable_locator, value_locator);
            } else {
              String variable_locator = mapStoreNormalizeAndVariableLocator.get(normalizeTextInCurrentElement);
              String value_locator = getXpath(e);
              mapStoreVariableLocatorAndValueLocator.put(variable_locator, value_locator);
            }
          }
        }
      }

    }

    for (Element child : e.children()) {
      traversalDomTree(child);
    }
  }

//  public String getXpath(Element e) {
//    int attributes_size = e.attributesSize();
//    String xpath = "";
//    if (attributes_size > 0) {
//      Attributes attr = e.attributes();
//      xpath += e.tagName() + "[";
//      boolean havingPreviousAttribute = false;
//      for (Attribute temp : attr) {
//        if (temp.getKey().equals("pattern")) {
//          continue;
//        } else {
//          if (havingPreviousAttribute) {
//            xpath += " and " + "@" + temp.getKey() + "=" + "'" + temp.getValue() + "'";
//          } else {
//            xpath += "@" + temp.getKey() + "=" + "'" + temp.getValue() + "'";
//            havingPreviousAttribute = true;
//          }
//
//        }
//      }
//      String textOfElement = e.ownText();
//
//      if (havingPreviousAttribute && !textOfElement.matches("\\s*")) {
//        xpath += " and " + "normalize-space()=" + "'" + textOfElement + "'";
//      } else {
//        if (!textOfElement.matches("\\s*")) {
//          xpath += "normalize-space()=" + "'" + textOfElement + "'";
//        }
//      }
//
//      Elements child = e.children();
//      int count_child = child.size();
//      while (count_child > 0) {
//        count_child--;
//        xpath += " and " + getXpath(child.get(count_child));
//      }
//
//      xpath += "]";
//    }
//    return xpath;
//  }

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

  /** Kiểm tra phần tử có chứa những thuộc tính cơ bản như id, class hay không. */
  public boolean hasBasicAttribute(Element e) {
    return e.hasAttr("id") || e.hasAttr("class");
  }

  /** Tìm tổ tiên gần nhất của phần tử hiện tại mà chỉ có đúng 1 phần tử con. */
  public Element findNearestAncestor(Element e) {
    Element parent = e.parent();
    if (hasBasicAttribute(parent)) {
      return parent;
    }
    findNearestAncestor(parent);
    return null;
  }

  @Override
  public String normalize(String s) {
    BreakIterator boundary = BreakIterator.getWordInstance();
    String lowercase_s = s.toLowerCase();
    lowercase_s = lowercase_s.trim();
    boundary.setText(lowercase_s);

    int start = boundary.first();
    Vector<String> wordsAndPunctuation = new Vector<>();
    for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
      String wordOrPunctuation = lowercase_s.substring(start, end);
      if (!wordOrPunctuation.matches("\\s*")) {
        wordsAndPunctuation.add(wordOrPunctuation);
      }
    }
    String normalize_result = String.join(" ", wordsAndPunctuation);
    return normalize_result;
  }

  /** Các phần tử có tag name là input và click được, giá trị thuộc tính value chính là text trong phần tử. */
  public boolean isDefaultClickableInput(Element e) {
    if (e.tagName().equals("input")) {
      if (e.hasAttr("type")) {
        String type = e.attr("type");
        return type.equals("submit") || type.equals("button") || type.equals("reset");
      }
    }
    return false;
  }

  public boolean isDefaultClickable(Element e) {
    return e.tagName().equals("button") || e.tagName().equals("a") || (e.hasAttr("role") && e.attr("role").equals("button"));
  }

}
