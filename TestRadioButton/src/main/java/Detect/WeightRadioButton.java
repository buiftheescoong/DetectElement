package Detect;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WeightRadioButton implements Comparable<WeightRadioButton> {
    public String choice;
    public String source;
    public String text;
    public List<Element> elementOfText;
    public Element result;
    public double full;
    public int weight;


    public WeightRadioButton(String source, String text, List<Element> list, String choice) {
        this.source = source;
        this.text = text;
        elementOfText = list;
        result = null;
        full = 0;
        weight = 0;
        this.choice = choice;

    }

    public Element findRadioButtonCorrespondingToText() {
        for (Element e : elementOfText) {
            Element radioBtn = HandleElement.searchRadioButtonInSubtree(e, choice);
            if (radioBtn != null) {
                return radioBtn;
            }
        }
        return null;
    }

    public int getWeight() {
        result = findRadioButtonCorrespondingToText();
        if (result != null) {
            weight = Calculator.weightBetweenTwoString(source, text);
            Set<String> visitedWords = new HashSet<>();
            Calculator.calculatePercentBetweenTwoStrings(source, text, visitedWords);
            List<String> wordsInSource = HandleString.separateWordsInString(source);
            HandleString.lowercaseWordsInList(wordsInSource);
            List<String> distinctWordsInSource = HandleString.distinctWordsInString(wordsInSource);
            full = 1.0 * visitedWords.size() / distinctWordsInSource.size();
        } else {
            weight = 0;
            full = 0;
        }
        return weight;
    }

    @Override
    public int compareTo(WeightRadioButton o) {
        int w1 = getWeight();
        int w2 = o.getWeight();
        if (full > o.full) {
            return 1;
        } else {
            if (full == o.full) {
                return w1 - w2;
            } else {
                return -1;
            }
        }
    }


}
