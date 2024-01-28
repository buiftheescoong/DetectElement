import Detect.Pair;
import Detect.SelectDropDownList;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;

@Test
public class SelectDropdownListTest {

    @Test
    public void testSelectDropDownList_test1() {
        SelectDropDownList select = new SelectDropDownList();
        Map<String, List<String>> mapTextAndChoices = new HashMap<>();
        mapTextAndChoices.put(" what Is your gender ", List.of(new String[]{"maLe "}));
        mapTextAndChoices.put("do you use any kind of tobacco or have  you ever  used  them", List.of(new String[]{" yes"}));
        mapTextAndChoices.put("Do you use any kind of illegal drugs or have you ever used them?", List.of(new String[]{"No"}));

        String linkHtml = "https://form.jotform.com/233591762291461";
        Map<Pair<String, String>, Pair<String, String>> result = select.processDetectDropdownList(mapTextAndChoices, linkHtml);

        for (Map.Entry<Pair<String,String>, Pair<String,String>> entry : result.entrySet()) {
            Pair<String,String> pairTextAndChoice = entry.getKey();
            Pair<String, String> pairLocatorOfSelectElementAndValue = entry.getValue();
            System.out.println(pairTextAndChoice.getFirst() + " "
                    + pairTextAndChoice.getSecond() + " "
                    + pairLocatorOfSelectElementAndValue.getFirst() + " "
                    + pairLocatorOfSelectElementAndValue.getSecond());
        }

        List<Pair<String, String>> expectedOutput = new ArrayList<>();
        Pair <String, String> question_1 = new Pair<>("//select[@class='form-dropdown' and @id='input_14' and @name='q14_whatIs14' and @style='width:310px' and @data-component='dropdown' and @aria-label='What is your gender?']","Male");
        Pair <String, String> question_2 = new Pair<>("//select[@class='form-dropdown is-active' and @id='input_10' and @name='q10_doYou10' and @style='width:310px' and @data-component='dropdown' and @aria-label='Do you use any kind of tobacco or have you ever used them?']","Yes");
        Pair <String, String> question_3 = new Pair<>("//select[@class='form-dropdown is-active' and @id='input_11' and @name='q11_doYou11' and @style='width:310px' and @data-component='dropdown' and @aria-label='Do you use any kind of illegal drugs or have you ever used them?']","No");

        expectedOutput.add(question_3);
        expectedOutput.add(question_2);
        expectedOutput.add(question_1);

        int i = 0;

        for (Map.Entry<Pair<String, String>,Pair<String, String >> entry : result.entrySet()) {
            Pair<String,String> pairTextAndChoice = entry.getKey();
            Pair<String, String> pairLocatorOfSelectElementAndValue = entry.getValue();
            assertEquals(pairLocatorOfSelectElementAndValue, expectedOutput.get(i));
            i++;
        }


    }

}
