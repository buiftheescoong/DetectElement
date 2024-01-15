package GetPageSource;

import java.io.FileWriter;
import java.io.IOException;

public class ProcessGetPageSource {
    public static void writeHtmlContentToFile(String content) throws IOException {
        FileWriter fileWriter = new FileWriter("E:\\LAB UI\\TestFindAndSelectElement\\TestRadioButton\\src\\main\\resources\\index.html");
        fileWriter.write(content);
        fileWriter.close();
    }
}
