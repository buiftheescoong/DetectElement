package Test;

import Detect.SelectDropDownList;
import Detect.Pair;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestSelectDropDownList {

  public static void main(String[] args) {
    SelectDropDownList select = new SelectDropDownList();
    Map<String, List<String>> mapTextAndChoices = new HashMap<>();
    mapTextAndChoices.put(" what Is your gender ", List.of(new String[]{"maLe "}));
    mapTextAndChoices.put("do you use any kind of tobacco or have  you ever  used  them", List.of(new String[]{" yes"}));
    mapTextAndChoices.put("Do you use any kind of illegal drugs or have you ever used them?", List.of(new String[]{"No"}));


//    mapTextAndChoices.put("Language", List.of(new String[]{"PHP", "Python"}));
//    mapTextAndChoices.put("", List.of(new String[]{"Price (low to high)"}));
//    mapTextAndChoices.put("Dropdown List", List.of(new String[] {"Option 1"}));
//    String linkHtml = "http://127.0.0.1:5500/demo_web_html/selectDropdownList.html";
    String linkHtml = "https://form.jotform.com/233591762291461";
    Map<Pair<String, String>, Pair<String, String>> result = select.processDetectDropdownList(mapTextAndChoices, linkHtml);
    for (Map.Entry<Pair<String,String>, Pair<String,String>> entry : result.entrySet()) {
      Pair<String,String> pairTextAndChoice = entry.getKey();
      Pair<String, String> pairLocatorOfSelectElementAndValue = entry.getValue();
      System.out.println(pairTextAndChoice.getFirst() + " " + pairTextAndChoice.getSecond() + " " + pairLocatorOfSelectElementAndValue.getFirst() + " "
          + pairLocatorOfSelectElementAndValue.getSecond());
    }
  }
}
