package Detect;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WeightVer2 implements Comparable<WeightVer2> {
    public String source;
    public Element e;
    public String text;
    public double full;
    public int weight;
    public WeightVer2(String source, Element e, String text) {
        this.source = source;
        this.e = e;
        this.text = text;
        this.full = 0;
        this.weight = 0;
    }

    public int getWeight() {
        int res = 0;
        if (!text.isEmpty()) {
            res += Calculator.weightBetweenTwoString(source, text);
        }
        Attributes attributes = e.attributes();
        if (e.attributesSize() > 0) {
            for (Attribute attr : attributes) {
                String typeAttr = attr.getKey();
                if (!Setting.except_attrs.contains(typeAttr)) {
                    String valueOfAttr = attr.getValue();
                    if (!valueOfAttr.isEmpty()) {
                        res += Calculator.weightBetweenTwoString(source, valueOfAttr);
                    }
                }
            }
        }
        weight = res;
        return res;
    }

    public double getFull() {
        double res = 0;
        Set<String> visitedWord = new HashSet<>();
        List<String> wordsInSource = HandleString.separateWordsInString(source);
        HandleString.lowercaseWordsInList(wordsInSource);
        Set<String> distinctWordsInSource = new HashSet<>(wordsInSource);
        if (!text.isEmpty()) {
            Calculator.calculatePercentBetweenTwoStrings(source, text, visitedWord);
        }
        if (e.attributesSize() > 0) {
            Attributes attributes = e.attributes();
            for (Attribute attr : attributes) {
                String typeAttr = attr.getKey();
                if (!Setting.except_attrs.contains(typeAttr)) {
                    String valueOfAttr = attr.getValue();
                    if (!valueOfAttr.isEmpty()) {
                        Calculator.calculatePercentBetweenTwoStrings(source, valueOfAttr, visitedWord);
                    }
                }
            }
        }
        int size = visitedWord.size();
        res = 1.0 * size / distinctWordsInSource.size();
        full = res;
        return res;
    }


    public int compareTo(WeightVer2 o) {
        int w1 = getWeight();
        int w2 = o.getWeight();
        double f1 = getFull();
        double f2 = o.getFull();
        if (f1 > f2) {
            return 1;
        } else {
            if (f1 == f2) {
                return w1 - w2;
            } else {
                return -1;
            }
        }
    }
}
