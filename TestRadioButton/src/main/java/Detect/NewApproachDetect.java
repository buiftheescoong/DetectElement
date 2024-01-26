package Detect;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.*;

public class NewApproachDetect {
    static Map<String, List<Element>> textAndElement = new HashMap<>();
    static List<String> listText = new ArrayList<>();
    public static String getHtmlContent(String linkHtml) {
        System.setProperty("Webdriver.chrome.driver", "C:\\chromedriver-win64-120\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get(linkHtml);
        String htmlContent = driver.getPageSource();
        driver.quit();
        return htmlContent;
    }

    public static Document getDomTree(String htmlContent) {
        Document domTree = Jsoup.parse(htmlContent);
        return domTree;
    }


    public static void getAllText(Element e) {
        if (e == null) {
            return;
        }
        String text = e.ownText();
        if (text.isEmpty() && isInputElement(e)) {
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
            getAllText(child);
        }
    }

    public static boolean isInputElement(Element e) {
        if (e == null) {
            return false;
        }
        if (e.tagName().equals("textarea") || (e.tagName().equals("input") && !e.attr("type").equals("submit") && !e.attr("type").equals("checkbox")
                && !e.attr("type").equals("radio") && !e.attr("type").equals("hidden"))) {
            return true;
        }
        return false;
    }
    public static Map<String, String> process(List<String> input, String linkHtml) {
        String htmlContent = getHtmlContent(linkHtml);
        Document document = getDomTree(htmlContent);
        Element body = document.body();
        getAllText(body);
        List<Weight> listWeight = new ArrayList<>();
        for (String s : input) {
            for (String text : listText) {
                if (calculateWeight(s,text) > 0) {
                    List<Element> list = textAndElement.get(text);
                    Weight w = new Weight(s, text, list, document);
                    listWeight.add(w);
                }
            }
        }
        Collections.sort(listWeight);
        Map<String, String> storeInputAndLocator = new HashMap<>();
        Map<String, Element> storeInputAndElement = new HashMap<>();
        for (int i = listWeight.size() - 1; i >= 0; i--) {
            String source = listWeight.get(i).source;
            Element result = listWeight.get(i).result;
            if (result != null) {
                if (!storeInputAndElement.containsKey(source) && !storeInputAndElement.containsValue(result)) {
                    storeInputAndElement.put(source, result);
                    String loc = getXpath(result);
                    storeInputAndLocator.put(source, loc);
                }
            }
        }
        return storeInputAndLocator;
    }

    public static String getXpath(Element e) {
        int attributes_size = e.attributesSize();
        String xpath = "";
        if (attributes_size > 0) {
            Attributes attr = e.attributes();
            xpath += "//" + e.tagName() + "[";
            boolean havingPreviousAttribute = false;
            if (e.hasAttr("id") && !e.attr("id").isEmpty()) {
                xpath += "@id=" + "'" + e.attr("id") + "']";
                return xpath;
            }
            if (attributes_size == 1 && e.hasAttr("class") && !e.attr("class").isEmpty()) {
                xpath += "@class=" + "'" + e.attr("class");
                havingPreviousAttribute = true;
            } else {
                for (Attribute temp : attr) {
                    if (temp.getKey().equals("pattern") || temp.getKey().equals("class")) {
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

    public static int calculateWeight(String source, String target) {
        int res = 0;
        String lowercase_target = target.toLowerCase();
        String[] arr = source.split(" ");
        for (int i = 0; i < arr.length; i++) {
            if (lowercase_target.contains(arr[i])) {
                res++;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        String linkHtml = "https://form.jotform.com/233591551157458?fbclid=IwAR1ggczzG7OoN6Dgb2SDWtNyznCAAJNW-G8-_3gnejJwPFunwwBuN_NCvh0";
        List<String> input = new ArrayList<>();
        input.add("first name in passenger");
        input.add("last name in passenger");
        input.add("first name in contact person");
        input.add("last name in contact person");
        input.add("title in contact person");
        input.add("title in passenger name");
        input.add("e mail");
        input.add("area code");
        input.add("phone");
        input.add("street address");
        input.add("street address 2");
        input.add("city in address");
        input.add("postal or zip code");
        input.add("state or province");

        Map<String, String> res = process(input, linkHtml);
        System.out.println(res);

    }

}

