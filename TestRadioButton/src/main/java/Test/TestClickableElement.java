package Test;

import Elements.ClickableElement;
import java.util.Vector;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TestClickableElement {

  public static void main(String[] args) {
    ClickableElement ce = new ClickableElement();
    String linkHtml = "https://form.jotform.com/233591762291461";
    Vector<String> input = new Vector<>();
    input.add("submit");
    Vector<String> result = ce.processDetectClickableElement(input, linkHtml);
    for (String x : result) {
      System.out.println(x);
    }

  }
}
