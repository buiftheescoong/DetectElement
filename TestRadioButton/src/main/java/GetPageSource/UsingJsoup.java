package GetPageSource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class UsingJsoup extends ProcessGetPageSource{
    public static String getHtmlContent(String linkHtml) throws IOException {
        Document document = Jsoup.connect(linkHtml).get();
        String htmlContent = document.html();
        return htmlContent;
    }

    public static void main(String[] args) {
        String linkHtml = "https://form.jotform.com/233591282365460";
        String htmlContent = null;
        try {
            htmlContent = getHtmlContent(linkHtml);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writeHtmlContentToFile(htmlContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
