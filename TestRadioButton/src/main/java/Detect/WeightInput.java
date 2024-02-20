package Detect;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

public class WeightInput extends Weight {
    public WeightInput(String source, String text, List<Element> list, Document document, String type) {
        super(source, text, list, document, type);
    }
}
