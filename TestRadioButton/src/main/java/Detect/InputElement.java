package Detect;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.*;

public class InputElement {


        static Map<String, List<Element>> textAndElement = new HashMap<>();
        static List<String> listText = new ArrayList<>();

        public static void getAllTextForInput(Element e) {
            if (e == null || (HandleElement.isInteractableElement(e) && !HandleElement.isInputElement(e))) {
                return;
            }
            String text = e.ownText();
            if (text.isEmpty() && HandleElement.isInputElement(e)) {
                String placeholder = e.attr("placeholder");
                if (!placeholder.isEmpty()) {
                    if (!listText.contains(placeholder)) {
                        listText.add(placeholder);
                    }
                    if (textAndElement.containsKey(placeholder)) {
                        List<Element> list = textAndElement.get(placeholder);
                        if (!list.contains(e)) {
                            list.add(e);
                        }
                    } else {
                        List<Element> list = new ArrayList<>();
                        list.add(e);
                        textAndElement.put(placeholder, list);
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
                getAllTextForInput(child);
            }
        }


        public static Map<String, String> detectInputElement(List<String> input, Document document) {
            Element body = document.body();
            getAllTextForInput(body);
            System.out.println(listText);
            List<Weight> listWeight = new ArrayList<>();
            for (String s : input) {
                for (String text : listText) {
                    if (Calculator.weightBetweenTwoString(s,text) > 0) {
                        List<Element> list = textAndElement.get(text);
                        Weight w = new Weight(s, text, list, document, "input");
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
            String linkHtml = "https://form.jotform.com/233591551157458?fbclid=IwAR1ggczzG7OoN6Dgb2SDWtNyznCAAJNW-G8-_3gnejJwPFunwwBuN_NCvh0";
            String htmlContent = Process.getHtmlContent(linkHtml);
            Document document = Process.getDomTree(htmlContent);
            List<String> input = new ArrayList<>();
        input.add("First-name_in_passenger");
        input.add("last_name in passenger");
        input.add("first_name in contact_person");
        input.add("last-Name In contact_person");
        input.add("title contact person");
        input.add("Title[in-passenger_name");
        input.add("e-mail");
        input.add("area code");
        input.add("phone");
        input.add("street address");
        input.add("street address 2");
        input.add("city in address");
        input.add("zip");
        input.add("state or province");
        List<String> click = Arrays.asList("next", "back", "submit");
        Map<String, String> res = detectInputElement(input, document);
        System.out.println(res);
        Map<String, String> res_click = ClickElement.detectClickElement(click, document);
        System.out.println(res_click);
        List<String> select = Arrays.asList("departing", "Destination", "airline", "Fare", "country in address", "month", "day", "year");
        Map<String, String> res_select = Select.detectSelectElement(select, document);
        System.out.println(res_select);
        }
}
