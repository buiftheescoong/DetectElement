package Test;

import Detect.Pair;
import Detect.RadioButton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRadioButton {
  public static void main(String[] args) {
    RadioButton radio = new RadioButton();
    Map<String, List<String>> mapTextAndChoices = new HashMap<>();
    Map<String, List<String>> mapStoreNormalizeTextAndChoices = new HashMap<>();
    Map<Pair<String, String>, Pair<String, String>> storeNormalizePairTextAndChoiceAndIt = new HashMap<>();

    mapTextAndChoices.put("are  You currently taking any medication ", List.of(new String[]{" yes"}));
    mapTextAndChoices.put("do you  have any medication   Allergies?", List.of(new String[]{" nOt Sure "}));
    mapTextAndChoices.put("How often do you consume alcohol?", List.of(new String[]{"Monthly"}));
//    mapTextAndChoices.put("Do you like the site?", List.of(new String[]{"Yes"}));
//    mapTextAndChoices.put("Do you like the header?", List.of(new String[]{"Impressive"}));
//    mapTextAndChoices.put("Do you like our buttons?", List.of(new String[]{"Impressive"}));
//    mapTextAndChoices.put("Please select your favorite Web language:", List.of(new String[]{"HTML"}));
//    mapTextAndChoices.put("Please select your age:", List.of(new String[]{"61 - 100"}));
    String linkHtml = "https://form.jotform.com/233591762291461";


//    String linkHtml = "http://127.0.0.1:5500/demo_web_html/radioButton.html";
    Map<Pair<String, String>, Pair<String, String>> result = radio.processDetectRadioButtonElement(mapTextAndChoices, linkHtml);
    for (Map.Entry<Pair<String, String>,Pair<String, String >> entry : result.entrySet()) {
      Pair<String, String> pairTextAndChoice = entry.getKey();
      Pair<String, String> pairGroupNameAndValue = entry.getValue();
      System.out.println(pairTextAndChoice.getFirst() + " " + pairTextAndChoice.getSecond() + " " + pairGroupNameAndValue.getFirst() + " " + pairGroupNameAndValue.getSecond());
    }
  }

}
