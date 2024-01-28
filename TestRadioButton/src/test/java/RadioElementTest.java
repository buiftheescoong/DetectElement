import Detect.Pair;
import Detect.RadioButton;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Test
public class RadioElementTest {

    @Test
    public void testRadioButton_test1() {
        String linkHtml = "https://form.jotform.com/233591762291461";

        RadioButton radio = new RadioButton();
        Map<String, List<String>> mapTextAndChoices = new HashMap<>();
        Map<String, List<String>> mapStoreNormalizeTextAndChoices = new HashMap<>();
        Map<Pair<String, String>, Pair<String, String>> storeNormalizePairTextAndChoiceAndIt = new HashMap<>();

        mapTextAndChoices.put("are  You currently taking any medication ", List.of(new String[]{" yes"}));
        mapTextAndChoices.put("do you  have any medication   Allergies?", List.of(new String[]{" nOt Sure "}));
        mapTextAndChoices.put("How often do you consume alcohol?", List.of(new String[]{"Monthly"}));

        Map<Pair<String, String>, Pair<String, String>> result = radio.processDetectRadioButtonElement(mapTextAndChoices, linkHtml);

        List<Pair<String, String>> expectedOutput = new ArrayList<>();
        Pair <String, String> question_1 = new Pair<>("q7_areYou","Yes");
        Pair <String, String> question_2 = new Pair<>("q20_typeA","Not Sure");
        Pair <String, String> question_3 = new Pair<>("q12_howOften","Monthly");

        expectedOutput.add(question_3);
        expectedOutput.add(question_1);
        expectedOutput.add(question_2);

        int i = 0;

        for (Map.Entry<Pair<String, String>,Pair<String, String >> entry : result.entrySet()) {
            Pair<String, String> pairTextAndChoice = entry.getKey();
            Pair<String, String> pairGroupNameAndValue = entry.getValue();
            assertEquals(pairGroupNameAndValue, expectedOutput.get(i));
            i++;
        }
    }
}
