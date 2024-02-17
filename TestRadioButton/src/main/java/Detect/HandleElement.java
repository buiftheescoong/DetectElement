package Detect;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.List;

public class HandleElement {


    public static boolean isInputElement(Element e) {
        if (e == null) {
            return false;
        }
        List<String> list = Arrays.asList("date", "datetime-local", "email", "month", "number", "password", "search", "tel", "text", "time", "url", "week", "");
        String tag = e.tagName();
        if (tag.equals("textarea")) {
            return true;
        }
        if (tag.equals("input")) {
            if (e.hasAttr("type")) {
                String type = e.attr("type");
                return list.contains(type);
            } else {
                return true;
            }
        }
        return false;
    }

    public static Element searchInputElementInSubtree(String text, Element e) {
        Elements input = HandleElement.selectInteractableElementsInSubtree(e);
        if (input.size() > 1 || (input.size() == 1 && !isInputElement(input.get(0))) || !e.text().equals(text)) {
            return null;
        }
        if (input.size() == 1 && isInputElement(input.get(0)) && e.text().equals(text)) {
            return input.get(0);
        }
        return searchInputElementInSubtree(text, e.parent());
    }

    public static Element searchSelectElementInSubtree(String text, Element e) {
       Elements elems = e.select("*");
       int cnt_interact = 0;
       Element select = null;
       for (Element ele : elems) {
           if (isInteractableElement(ele) && !isSelectElement(ele)) {
               return null;
           }
           if (ele.tagName().equals("option")) {
               continue;
           }
           String t = e.ownText();
           if (!t.isEmpty() && !t.equals(text)) {
               return null;
           }
           if (isSelectElement(ele)) {
               select = ele;
           }
       }
       if (select == null) {
           return searchSelectElementInSubtree(text, e.parent());
       }
       return select;


    }


    public static boolean isDefaultClickableElement(Element e) {
        if (e == null) {
            return false;
        }
        String tag = e.tagName();
        if (tag.equals("button") || tag.equals("img") || tag.equals("a")) {
            return true;
        }
        return isClickElementTagInput(e);
    }

    public static boolean isClickElementTagInput(Element e) {
        if (e == null) {
            return false;
        }
        String tag = e.tagName();
        if (tag.equals("input")) {
            if (e.hasAttr("type") && (e.attr("type").equals("button") || e.attr("type").equals("reset") || e.attr("type").equals("submit") || e.attr("type").equals("image"))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRadioElement(Element e) {
        if (e == null) {
            return false;
        }
        if (e.tagName().equals("input") && e.attr("type").equals("radio")) {
            return true;
        }
        return false;
    }

    public static boolean isCheckboxElement(Element e) {
        if (e == null) {
            return false;
        }
        if (e.tagName().equals("input") && e.attr("type").equals("checkbox")) {
            return true;
        }
        return false;
    }
    public static boolean isSelectElement(Element e) {
        if (e == null) {
            return false;
        }
        if (e.tagName().equals("select")) {
            return true;
        }
        return false;
    }

    public static boolean isInteractableElement(Element e) {
        if (e == null) {
            return false;
        }
        List<String> list = Arrays.asList("input", "button", "img", "a", "select", "textarea");
        String tag = e.tagName();
        return list.contains(tag);
    }
    public static Elements selectInteractableElementsInSubtree(Element e) {
        Elements res = new Elements();
        Elements textarea_tag = e.getElementsByTag("textarea");
        Elements input_tag = e.getElementsByTag("input");
        Elements select_tag = e.getElementsByTag("select");
        Elements a_tag = e.getElementsByTag("a");
        Elements img_tag = e.getElementsByTag("img");
        Elements button = e.getElementsByTag("button");
        if (textarea_tag != null) {
            res.addAll(textarea_tag);
        }
        if (input_tag != null) {
            res.addAll(input_tag);
        }
        if (select_tag != null) {
            res.addAll(select_tag);
        }
        if (a_tag != null) {
            res.addAll(a_tag);
        }
        if (img_tag != null) {
            res.addAll(img_tag);
        }
        if (button != null) {
            res.addAll(button);
        }
        return res;
    }

    public static boolean isLabelHasForAttr(Element e) {
        if (e == null) {
            return false;
        }
        if (e.tagName().equals("label") && e.hasAttr("for") && !e.attr("for").isEmpty()) {
            return true;
        }
        return false;
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
        Element e = document.getElementsContainingOwnText("Standard multi select").get(0);
        Element res = searchSelectElementInSubtree("Standard multi select", e);
        System.out.println(res);
        System.out.println(Process.getXpath(res));
    }
}
