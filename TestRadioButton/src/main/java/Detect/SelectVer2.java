package Detect;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class SelectVer2 {
    public static String getText(Element e, Document document) {
        String res = "";
        if (e.hasAttr("id") && !e.attr("id").isEmpty()) {
            res = getAssociatedLabel(e.attr("id"), document);
            if (!res.isEmpty()) {
                return res;
            }
        }
        res = getTextForSelectElementInSubtree(e.parent());
        return res;
    }

    public static String getAssociatedLabel(String id, Document document) {
        String query = "label[for='" + id + "']";
        Elements label = document.select(query);
        if (label.isEmpty()) {
            return "";
        } else {
            return label.get(0).ownText();
        }
    }

    public static String getTextForSelectElementInSubtree(Element e) {
        Elements elements = HandleElement.selectInteractableElementsInSubtree(e);
        if (elements.size() > 1) {
            return "";
        }
        Elements elems = e.select("*");
        String text = e.selectFirst("select").text();
        int cnt = 0;
        String tmp = "";
        for (Element ele : elems) {
            String tag = ele.tagName();
            if (!tag.equals("select") && !tag.equals("option")) {
                String t = ele.ownText();
                if (!t.isEmpty()) {
                    cnt++;
                    tmp = t;
                    if (cnt > 1 || text.contains(t)) {
                        return "";
                    }
                }
            }
        }
        if (cnt == 1) {
            return tmp;
        }
        return getTextForSelectElementInSubtree(e.parent());
    }

    public static Elements getSelectElements(Document document) {
        return document.getElementsByTag("select");
    }

    public static Map<String, String> detectSelectElement(List<String> input, Document document) {
        Elements selectElements = getSelectElements(document);
        List<WeightVer2> list = new ArrayList<>();
        for (String s : input) {
            for (Element e : selectElements) {
                String text = getText(e, document);
//                System.out.println(e.attr("id") + " " + text);
                WeightVer2 w = new WeightVer2(s, e, text);
                list.add(w);
            }
        }
        Map<String, String> res = new HashMap<>();
        if (list.size() == 0) {
            WeightVer2 w = list.get(0);
            String source = w.source;
            Element e = w.e;
            System.out.println(source + " " + e + " " + w.getFull() + " " + w.getWeight());
            res.put(w.source, Process.getXpath(e));
        } else {
            Collections.sort(list);
            Map<String, Element> visited = new HashMap<>();
            List<String> visitedInput = new ArrayList<>();
            for (int i = list.size() - 1; i >= 0; i--) {
                String source = list.get(i).source;
                Element e = list.get(i).e;
                if (!visited.containsValue(e) && !visited.containsKey(source)) {
                    visited.put(source, e);
                    visitedInput.add(source);
                    res.put(source, Process.getXpath(e));
                    System.out.println(source + " " + Process.getXpath(e) + " " + list.get(i).text  + " " + list.get(i).getFull() + " " + list.get(i).getWeight());
                }
                if (visitedInput.size() == input.size()) {
                    break;
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        String linkHtml = "https://demoqa.com/select-menu";
//        String linkHtml = "https://form.jotform.com/233591551157458?fbclid=IwAR1ggczzG7OoN6Dgb2SDWtNyznCAAJNW-G8-_3gnejJwPFunwwBuN_NCvh0";
        String htmlContent = Process.getHtmlContent(linkHtml);
        Document document = Process.getDomTree(htmlContent);
//        List<String> input = new ArrayList<>();
//        input.add("departing");
//        input.add("Destination");
//        input.add("airline");
//        input.add("Fare");
//        input.add("country in address");
//        input.add("month");
//        input.add("day");
//        input.add("year");
//        Map<String, String> res = detectSelectElement(input, document);
        Element e = document.getElementById("cars");
        System.out.printf(getText(e, document));
    }
}
