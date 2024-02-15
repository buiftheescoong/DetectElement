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

//    public static Element searchClickableElementInSubtree(String text, Element e) {
//
//    }


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
        if (textarea_tag != null && textarea_tag.size() > 0) {
            res.addAll(textarea_tag);
        }
        if (input_tag != null) {
            res.addAll(input_tag);
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
        Process n = new Process();
        String linkHtml = "https://demoqa.com/login";
        String htmlContent = n.getHtmlContent(linkHtml);
        Document document = n.getDomTree(htmlContent);
        Element body = document.body();
        Element label = body.getElementById("userName-label");
        System.out.println(searchInputElementInSubtree("UserName :", label));
    }
}
