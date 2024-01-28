import Detect.Checkbox;
import Detect.Pair;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.testng.Assert.assertEquals;

@Test
public class CheckboxTest {
    private Checkbox checkbox;
    private Map<String, List<String>> mapTextAndChoices;
    private String linkHtml;
    private Map<Pair<String, String>, String> result;

    @BeforeMethod
    public void beforeClass() {
        checkbox = new Checkbox();
        mapTextAndChoices = new HashMap<>();
        result = new HashMap<>();
    }

    @Test
    public void testProcessDetectCheckboxElement_test1() {
        linkHtml = "https://form.jotform.com/233591762291461";
        mapTextAndChoices.put("check the conditions that apply to you or any member of your immediate relatives  ", List.of(new String[]{"Cancer"}));
        mapTextAndChoices.put("Check the symptoms that you' re currently experiencing:", List.of(new String[]{"Chest pain"}));
        result = checkbox.processDetectCheckboxElement(mapTextAndChoices, linkHtml);
        for (Map.Entry<Pair<String, String>,String> entry : result.entrySet()) {
            Pair<String, String> pairTextAndChoice = entry.getKey();
            String xpath = entry.getValue();
            assertEquals(xpath, "//input[@type='checkbox' and @aria-describedby='label_6' and @class='form-checkbox' and @id='input_6_0' and @name='q6_name6[]' and @value='Chest pain']");
            break;
        }
    }
}








