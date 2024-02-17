package Detect;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.*;

public class ClickElement {
    static Map<String, List<Element>> textAndElement = new HashMap<>();
    static List<String> listText = new ArrayList<>();

    public static void getAllTextForClick(Element e) {
        if (e == null || (HandleElement.isInteractableElement(e) && !HandleElement.isDefaultClickableElement(e))) {
            return;
        }
        String text = e.ownText();
        if (text.isEmpty() && HandleElement.isClickElementTagInput(e)) {
            if (e.hasAttr("value")) {
                String valueAttr = e.attr("value");
                if (!valueAttr.isEmpty()) {
                    if (!listText.contains(valueAttr)) {
                        listText.add(valueAttr);
                    }
                    if (textAndElement.containsKey(valueAttr)) {
                        List<Element> list = textAndElement.get(valueAttr);
                        if (!list.contains(e)) {
                            list.add(e);
                        }
                    } else {
                        List<Element> list = new ArrayList<>();
                        list.add(e);
                        textAndElement.put(valueAttr, list);
                    }
                }
            } else {
                if (e.attr("type").equals("submit")) {
                    String defaultValue = "Submit";
                    if (!listText.contains(defaultValue)) {
                        listText.add(defaultValue);
                    }
                    if (textAndElement.containsKey(defaultValue)) {
                        List<Element> list = textAndElement.get(defaultValue);
                        if (!list.contains(e)) {
                            list.add(e);
                        }
                    } else {
                        List<Element> list = new ArrayList<>();
                        list.add(e);
                        textAndElement.put(defaultValue, list);
                    }
                }
                if (e.attr("type").equals("reset")) {
                    String defaultValue = "Reset";
                    if (!listText.contains(defaultValue)) {
                        listText.add(defaultValue);
                    }
                    if (textAndElement.containsKey(defaultValue)) {
                        List<Element> list = textAndElement.get(defaultValue);
                        if (!list.contains(e)) {
                            list.add(e);
                        }
                    } else {
                        List<Element> list = new ArrayList<>();
                        list.add(e);
                        textAndElement.put(defaultValue, list);
                    }
                }
            }
        }
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
            getAllTextForClick(child);
        }
    }


    public static Map<String, String> detectClickElement(List<String> input, Document document) {
        Element body = document.body();
        getAllTextForClick(body);
        List<Weight> listWeight = new ArrayList<>();
        for (String s : input) {
            for (String text : listText) {
                if (Calculator.weightBetweenTwoString(s,text) > 0) {
                    List<Element> list = textAndElement.get(text);
                    Weight w = new Weight(s, text, list, document, "click");
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
                    System.out.println(source + " " + result + " " + listWeight.get(i).weight + " " + listWeight.get(i).full) ;
                }
            }
        }
        return storeInputAndLocator;
    }

}
