package Detect;

import org.jsoup.nodes.Element;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WeightCheckbox implements Comparable<WeightCheckbox>{
    public List<String> choices;
    public String source;
    public String text;
    public List<Element> elementOfText;
    public Map<String, Element> result;
    public double full;
    public int weight;


    public WeightCheckbox(String source, String text, List<Element> list, List<String> choices) {
        this.source = source;
        this.text = text;
        elementOfText = list;
        result = null;
        full = 0;
        weight = 0;
        this.choices = choices;

    }

    public Map<String, Element> findCheckboxCorrespondingToText() {
        for (Element e : elementOfText) {
            Map<String, Element> listCheckbox = HandleElement.searchCheckboxInSubtree(e, choices);
            if (listCheckbox != null) {
                return listCheckbox;
            }
        }
        return null;
    }

    public int getWeight() {
        result = findCheckboxCorrespondingToText();
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
    public int compareTo(WeightCheckbox o) {
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
