package Test;

import Elements.Checkbox;
import Elements.Checkbox.Pair;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDetectCheckboxElement {
  public static void main(String[] args) {
    Checkbox checkbox = new Checkbox();
    Map<String, List<String>> mapTextAndChoices = new HashMap<>();
    mapTextAndChoices.put("Check the conditions that apply to you or any member of your immediate relatives:", List.of(new String[]{"Asthma", "Cancer"}));
    mapTextAndChoices.put("Check the symptoms that you' re currently experiencing:", List.of(new String[]{"Weight gain", "Chest pain"}));
//    mapTextAndChoices.put("Hobbies", List.of(new String[]{"Reading"}));
//    mapTextAndChoices.put("Favorite", List.of(new String[]{"Sports"}));
//    mapTextAndChoices.put("Nơi Bán", List.of(new String[]{"Hải Phòng", "Bắc Ninh"}));
//    mapTextAndChoices.put("", List.of(new String[] {"Sports", "Remember Me"}));
    String linkHtml = "https://form.jotform.com/233591762291461";
    Map<Pair<String, String>, String> result = new HashMap<>();
    result = checkbox.processDetectCheckboxElement(mapTextAndChoices, linkHtml);
    for (Map.Entry<Pair<String, String>,String> entry : result.entrySet()) {
      Pair<String, String> pairTextAndChoice = entry.getKey();
      String xpath = entry.getValue();
      System.out.println(pairTextAndChoice.getFirst() + " " + pairTextAndChoice.getSecond() + " " + xpath);
    }
  }
}
