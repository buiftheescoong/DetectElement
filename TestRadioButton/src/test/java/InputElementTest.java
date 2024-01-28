import Detect.Input;
import org.testng.annotations.Test;

import java.util.Vector;
import static org.testng.Assert.assertEquals;

@Test
public class InputElementTest {

    @Test
    public void testInputElement_test1() {
        Input ip = new Input();
        Vector<String> loc = new Vector<>();
        loc.add("username");
        loc.add("password");
        String linkHtml = "https://www.saucedemo.com/";
        Vector<String> result = ip.processDetectInputElement(loc, linkHtml);
        Vector<String> expectedOutput = new Vector<>();

        expectedOutput.add("//input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']");
        expectedOutput.add("//input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']");

        for (int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i), expectedOutput.get(i));
        }
    }


    @Test
    public void testInputElement_test2() {
        Input ip = new Input();
        Vector<String> loc = new Vector<>();
        loc.add("First Name");
        loc.add("Last Name");
        loc.add("Street Address");
        String linkHtml = "https://form.jotform.com/240242329354451";
        Vector<String> result = ip.processDetectInputElement(loc, linkHtml);
        Vector<String> expectedOutput = new Vector<>();
        expectedOutput.add("//input[@type='text' and @id='first_3' and @name='q3_fullName3[first]' and @class='form-textbox validate[required]' and @data-defaultvalue='' and @autocomplete='section-input_3 given-name' and @size='10' and @data-component='first' and @aria-labelledby='label_3 sublabel_3_first' and @required='' and @value='']");
        expectedOutput.add("//input[@type='text' and @id='last_3' and @name='q3_fullName3[last]' and @class='form-textbox validate[required]' and @data-defaultvalue='' and @autocomplete='section-input_3 family-name' and @size='15' and @data-component='last' and @aria-labelledby='label_3 sublabel_3_last' and @required='' and @value='']");
        expectedOutput.add("//input[@type='text' and @id='input_4_addr_line1' and @name='q4_address4[addr_line1]' and @class='form-textbox validate[required] form-address-line' and @data-defaultvalue='' and @autocomplete='section-input_4 address-line1' and @data-component='address_line_1' and @aria-labelledby='label_4 sublabel_4_addr_line1' and @required='' and @value='' and @maxlength='100']");

        for (int i = 0; i < result.size(); i++) {
            //System.out.println(result.get(i));
            assertEquals(result.get(i), expectedOutput.get(i));
        }
    }


    @Test
    /* Detect sai phần tử */
    public void testInputElement_test3() {
        Input ip = new Input();
        Vector<String> loc = new Vector<>();
        loc.add("Title");
        loc.add("First Name");
        loc.add("Last Name");
        String linkHtml = "https://form.jotform.com/233591551157458";
        Vector<String> result = ip.processDetectInputElement(loc, linkHtml);
        Vector<String> expectedOutput = new Vector<>();
        expectedOutput.add("//input[@type='text' and @id='prefix_3' and @name='q3_passengerName[prefix]' and @class='form-textbox' and @data-defaultvalue='' and @autocomplete='section-input_3 honorific-prefix' and @size='4' and @data-component='prefix' and @aria-labelledby='label_3 sublabel_3_prefix' and @value='']");
        expectedOutput.add("//input[@type='text' and @id='first_3' and @name='q3_passengerName[first]' and @class='form-textbox' and @data-defaultvalue='' and @autocomplete='section-input_3 given-name' and @size='10' and @data-component='first' and @aria-labelledby='label_3 sublabel_3_first' and @value='']");
        expectedOutput.add("//input[@type='text' and @id='last_3' and @name='q3_passengerName[last]' and @class='form-textbox' and @data-defaultvalue='' and @autocomplete='section-input_3 family-name' and @size='15' and @data-component='last' and @aria-labelledby='label_3 sublabel_3_last' and @value='']");

        for (int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i), expectedOutput.get(i));
        }
        //System.out.println(result.get(0));
        // Output: input[@type='text' and @id='prefix_6' and @name='q6_contactPerson[prefix]' and @class='form-textbox' and @data-defaultvalue='' and @autocomplete='section-input_6 honorific-prefix' and @size='4' and @data-component='prefix' and @aria-labelledby='label_6 sublabel_6_prefix' and @value='']
    }



    @Test
    /* Trường hợp không chạy được khi có sự khác nhau giữa First Name và First_name */
    public void testInputElement_test4() {
        Input ip = new Input();
        Vector<String> loc = new Vector<>();
        loc.add("First_name");
        loc.add("Last_name");
        loc.add("Street_address");
        String linkHtml = "https://form.jotform.com/240242329354451";
        Vector<String> result = ip.processDetectInputElement(loc, linkHtml);
        Vector<String> expectedOutput = new Vector<>();
        expectedOutput.add("//input[@type='text' and @id='first_3' and @name='q3_fullName3[first]' and @class='form-textbox validate[required]' and @data-defaultvalue='' and @autocomplete='section-input_3 given-name' and @size='10' and @data-component='first' and @aria-labelledby='label_3 sublabel_3_first' and @required='' and @value='']");
        expectedOutput.add("//input[@type='text' and @id='last_3' and @name='q3_fullName3[last]' and @class='form-textbox validate[required]' and @data-defaultvalue='' and @autocomplete='section-input_3 family-name' and @size='15' and @data-component='last' and @aria-labelledby='label_3 sublabel_3_last' and @required='' and @value='']");
        expectedOutput.add("//input[@type='text' and @id='input_4_addr_line1' and @name='q4_address4[addr_line1]' and @class='form-textbox validate[required] form-address-line' and @data-defaultvalue='' and @autocomplete='section-input_4 address-line1' and @data-component='address_line_1' and @aria-labelledby='label_4 sublabel_4_addr_line1' and @required='' and @value='' and @maxlength='100']");

        for (int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i), expectedOutput.get(i));
        }
    }
}


