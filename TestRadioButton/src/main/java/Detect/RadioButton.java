package Detect;

import java.util.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


//public class RadioButton extends ProcessDetectElement {
//  Map<Pair<String, String>, Pair<String, String>> mapStorePairGroupNameAndValueForPairTextAndChoice = new HashMap<>();
//  Map<String, Element> mapIdAttributeAndRadioButtonElement = new HashMap<>();
//  Map<Pair<String,String>, Element> mapStoreRadioButtonElementForPairTextAndChoice = new HashMap<>();
//  Map<String, List<String>> mapStoreNormalizeTextAndChoices = new HashMap<>();
//
//  Map<Pair<String, String>, Pair<String, String>> storeNormalizePairTextAndChoiceAndIt = new HashMap<>();
//
//  public static void main(String[] args) {
//
//  }
//
//
//  public Map<Pair<String, String>, Pair<String, String>> processDetectRadioButtonElement(Map<String, List<String>> mapTextAndChoices, String linkHtml) {
//    normalizeInputMap(mapStoreNormalizeTextAndChoices, mapTextAndChoices, storeNormalizePairTextAndChoiceAndIt);
//    String htmlContent = getHtmlContent(linkHtml);
//    Document domTree = getDomTree(htmlContent);
//    Elements childRoot = domTree.children();
//    for (Element child : childRoot) {
//      traversalDomTree(child);
//    }
//    return mapStorePairGroupNameAndValueForPairTextAndChoice;
//  }
//
//  public void traversalDomTree(Element e) {
//    String text = e.ownText();
//    String normalizeText = normalize(text);
//    if (!text.isEmpty() && mapStoreNormalizeTextAndChoices.containsKey(normalizeText)) {
//        searchChoiceCorrespondingToText(e.parent(), normalizeText);
//    }
//    for (Element child : e.children()) {
//      traversalDomTree(child);
//    }
//  }
//
//    /** Tìm lựa chọn ứng với câu hỏi trong cây con. */
//  public void searchChoiceCorrespondingToText(Element root, String text) {
//    if (isRadioButtonElement(root)) {
//      mapIdAttributeAndRadioButtonElement.put(root.attr("id"), root);
//    }
//    String textInCurrentElement = root.ownText();
//    String normalizeTextInCurrentElement = normalize(textInCurrentElement);
//    if (checkChoiceInListChoicesOfText(text, normalizeTextInCurrentElement)) {
//      String forAttribute = root.attr("for");
//      Element radioButtonElement = mapIdAttributeAndRadioButtonElement.get(forAttribute);
//      Pair<String, String> normalizePairTextAndChoice = new Pair<>(text, normalizeTextInCurrentElement);
//      Pair<String, String> pairTextAndChoice = storeNormalizePairTextAndChoiceAndIt.get(normalizePairTextAndChoice);
//      mapStoreRadioButtonElementForPairTextAndChoice.put(pairTextAndChoice, radioButtonElement);
//      detectGroupNameAndValueCorrespondingToTextAndChoice(pairTextAndChoice, radioButtonElement);
//    }
//    for (Element child : root.children()) {
//      searchChoiceCorrespondingToText(child, text);
//    }
//  }
//  public boolean isRadioButtonElement(Element e) {
//    return e.tagName().equals("input") && e.hasAttr("type")
//        && e.attr("type").equals("radio");
//  }
//
//  /** Kiểm tra xem câu hỏi có lựa chọn hiện tại hay không. */
//  public boolean checkChoiceInListChoicesOfText(String text, String choice) {
//    List<String> list = mapStoreNormalizeTextAndChoices.get(text);
//    return list.contains(choice);
//  }
//
//  /** Tìm group name và thuộc tính value ứng với cặp câu hỏi và lựa chọn đang có. */
//  public void detectGroupNameAndValueCorrespondingToTextAndChoice(Pair<String,String> pairTextAndChoice, Element e) {
//    Pair<String, String> pairGroupNameAndValue = new Pair<>(e.attr("name"), e.attr("value"));
//    mapStorePairGroupNameAndValueForPairTextAndChoice.put(pairTextAndChoice, pairGroupNameAndValue);
//  }
//
//
//
//}

public class RadioButton {
  static Map<String, List<Element>> textAndElement = new HashMap<>();
  static List<String> listText = new ArrayList<>();

  public static void getAllTextForRadioButton(Element e) {
    if (e == null || HandleElement.isInteractableElement(e)) {
      return;
    }
    String text = e.ownText();
    if (!text.isEmpty() && !listText.contains(text)) {
      listText.add(text);
    }
    if (!text.isEmpty()) {
      if (textAndElement.containsKey(text)) {
        List<Element> list = textAndElement.get(text);
        if (!list.contains(e)) {
          list.add(e);
        }
      } else {
        List<Element> list = new ArrayList<>();
        list.add(e);
        textAndElement.put(text, list);
      }
    }
    for (Element child : e.children()) {
      getAllTextForRadioButton(child);
    }
  }


  public static Map<String, String> detectRadioButtonElement(Map<String, String> map, Document document) {
    Element body = document.body();
    getAllTextForRadioButton(body);
    List<WeightRadioButton> listWeight = new ArrayList<>();
    for (Map.Entry<String, String> entry : map.entrySet()) {
      String question = entry.getKey();
      String choice = entry.getValue();
      for (String text : listText) {
        if (Calculator.weightBetweenTwoString(question,text) > 0) {
          List<Element> list = textAndElement.get(text);
          WeightRadioButton w = new WeightRadioButton(question, text, list, choice);
          listWeight.add(w);
        }
      }
    }
    if (listWeight.size() == 1) {
      listWeight.get(0).getWeight();
    } else {
      Collections.sort(listWeight);
    }


    Map<String, String> storeInputAndLocator = new HashMap<>();
    Map<String, Element> storeInputAndElement = new HashMap<>();
    for (int i = listWeight.size() - 1; i >= 0; i--) {
      String source = listWeight.get(i).source;
      Element result = listWeight.get(i).result;
      if (result != null) {
        if (!storeInputAndElement.containsKey(source) && !storeInputAndElement.containsValue(result)) {
          storeInputAndElement.put(source, result);
          String loc = Process.getXpath(result);
          storeInputAndLocator.put(source, loc);
          System.out.println(1 + " " + source + " " +  listWeight.get(i).text + " " + result + " " + listWeight.get(i).weight + " " + listWeight.get(i).full) ;
        }
      }
    }
    return storeInputAndLocator;
  }

  public static void main(String[] args) {
    String linkHtml = "https://form.jotform.com/233591762291461";
    String htmlContent = Process.getHtmlContent(linkHtml);
    Document document = Process.getDomTree(htmlContent);
    Map<String, String> map = new HashMap<>();
    map.put("Are you currently taking any medication?", "No");
    map.put("Do you have any medication allergies?", "Yes");
    map.put(" consume alcohol", "Never");
    Map<String, String> res = detectRadioButtonElement(map, document);
  }
}
