package GetPageSource;
// Java program to read and download
// webpage in html file
import java.io.*;
import java.net.URL;
import java.net.MalformedURLException;

public class ver1 {

    public static void DownloadWebPage(String webpage)
    {
        try {

            // Create URL object
            URL url = new URL(webpage);
            BufferedReader readr =
                    new BufferedReader(new InputStreamReader(url.openStream()));

            // Enter filename in which you want to download
            BufferedWriter writer =
                    new BufferedWriter(new FileWriter("E:\\LAB UI\\TestFindAndSelectElement\\TestRadioButton\\src\\main\\resources\\index.html"));

            // read each line from stream till end
            String line;
            while ((line = readr.readLine()) != null) {
                writer.write(line);
            }

            readr.close();
            writer.close();
            System.out.println("Successfully Downloaded.");
        }

        // Exceptions
        catch (MalformedURLException mue) {
            System.out.println("Malformed URL Exception raised");
        }
        catch (IOException ie) {
            System.out.println("IOException raised");
        }
    }
    public static void main(String args[])
            throws IOException
    {
        String url = "https://form.jotform.com/233591282365460";
        DownloadWebPage(url);
    }
}
