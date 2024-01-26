package Test;

import Detect.Input;
import java.util.Vector;

public class TestInputElement {

  public static void main(String[] args) {
    Input ip = new Input();
    Vector<String> loc = new Vector<>();
//    loc.add("first  name");
//    loc.add("Last name");
//    loc.add("Contact number  ");
//    loc.add("  email address");
//    String linkHtml = "https://form.jotform.com/233591762291461";
//    loc.add("first  name");
//    loc.add("Last name");
//    loc.add("Contact number  ");
//    loc.add("  email address");
//
//    String linkHtml = "https://form.jotform.com/233591762291461";

    loc.add("username");
    loc.add("password");
    String linkHtml = "https://www.saucedemo.com/";
    Vector<String> result = ip.processDetectInputElement(loc, linkHtml);
    System.out.println(result);
  }
}
