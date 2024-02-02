package Detect;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HandleElement {
    public static Element searchInputElementInSubtree(String text, Element e) {
        Elements input = inputElementsInSubtree(e);
        if (input.size() > 1 || !e.text().equals(text)) {
            return null;
        }
        if (input.size() == 1 && e.text().equals(text)) {
            return input.get(0);
        }
        return searchInputElementInSubtree(text, e.parent());
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

    public static Elements inputElementsInSubtree(Element e) {
        Elements res = new Elements();
        Elements textarea_tag = e.getElementsByTag("textarea");
        Elements input_tag = e.getElementsByTag("input");
        if (textarea_tag != null && textarea_tag.size() > 0) {
            res.addAll(textarea_tag);
        }
        if (input_tag != null) {
            for (Element elem : input_tag) {
                if (isInputElement(elem)) {
                    res.add(elem);
                }
            }
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
        NewApproachDetect n = new NewApproachDetect();
        String linkHtml = "https://demoqa.com/login";
        String htmlContent = n.getHtmlContent(linkHtml);
        Document document = n.getDomTree(htmlContent);
        Element body = document.body();
        Element label = body.getElementById("userName-label");
        System.out.println(searchInputElementInSubtree("UserName :", label));
    }
}
