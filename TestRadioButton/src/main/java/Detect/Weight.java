package Detect;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Weight implements TypeElement, Comparable<Weight> {
    public String source;
    public String text;
    public List<Element> elementOfText;
    public Document doc;
    public Element result;
    public double full;
    public int weight;
    public Weight(String source, String text, List<Element> list, Document document) {
        this.source = source;
        this.text = text;
        this.doc = document;
        elementOfText = list;
        result = null;
        full = 0;
        weight = 0;
    }

    public int calculateWeight(String target) {
        return CalculateWeight.weightBetweenTwoString(source, target);
    }

    public double calculatePercent(String target,Element e) {
        double res = 0;
        if (target.isEmpty() || e == null) {
            return 0;
        }
        Set<String> visitedWord = new HashSet<>();
        List<String> wordsInSource = HandleString.separateWordsInString(source);
        HandleString.lowercaseWordsInList(wordsInSource);
        List<String> wordsInTarget = HandleString.separateWordsInString(target);
        HandleString.lowercaseWordsInList(wordsInTarget);
        for (String w : wordsInSource) {
            if (wordsInTarget.contains(w)) {
                visitedWord.add(w);
            }
        }
        if (e.attributesSize() > 0) {
            Attributes attributes = e.attributes();
            for (Attribute attr : attributes) {
                String valueOfAttr = attr.getValue();
                if (!valueOfAttr.isEmpty()) {
                    List<String> wordsInValue = HandleString.separateWordsInString(valueOfAttr);
                    HandleString.lowercaseWordsInList(wordsInValue);
                    for (String w : wordsInSource) {
                        if (wordsInValue.contains(w)) {
                            visitedWord.add(w);
                        }
                    }
                }
            }
        }

        int size = visitedWord.size();
        res = 1.0 * size / wordsInSource.size();
        return res;
    }


    public int calculateWeight(Element e) {
        int res = 0;
        if (e == null || e.attributes().size() == 0) {
            return 0;
        }
        Attributes attributes = e.attributes();
        for (Attribute attr : attributes) {
            String valueOfAttr = attr.getValue();
            if (!valueOfAttr.isEmpty()) {
                res += calculateWeight(valueOfAttr);
            }
        }
        return res;
    }

    public double calculatePercent(Element e) {
        double res = 0;
        if (e == null || e.attributes().size() == 0) {
            return 0;
        }
        Set<String> visitedWord = new HashSet<>();
        List<String> wordsInSource = HandleString.separateWordsInString(source);
        HandleString.lowercaseWordsInList(wordsInSource);
        if (e.attributesSize() > 0) {
            Attributes attributes = e.attributes();
            for (Attribute attr : attributes) {
                String valueOfAttr = attr.getValue();
                if (!valueOfAttr.isEmpty()) {
                    List<String> wordsInValue = HandleString.separateWordsInString(valueOfAttr);
                    HandleString.lowercaseWordsInList(wordsInValue);
                    for (String w : wordsInSource) {
                        if (wordsInValue.contains(w)) {
                            visitedWord.add(w);
                        }
                    }
                }
            }
        }

        int size = visitedWord.size();
        res = 1.0 * size / wordsInSource.size();
        return res;
    }
    public Pair<Pair<Integer, Double>, Element> calculateWeightWithElementOfText(Element e) {
        int res = 0;
        double per = 0;
        Element tmp = null;
        if (type(e)) {
            res = calculateWeight(e);
            per = calculatePercent(e);
            tmp = e;
        } else {
            if (isLabel(e)) {
                String id = e.attr("for");
                Elements elements = doc.select("#"+ id);
                if (elements.isEmpty()) {
                    res = 0;
                } else {
                    Element elementAssociatedWithLabel = elements.get(0);
                    if (type(elementAssociatedWithLabel)) {
                        res = calculateWeight(elementAssociatedWithLabel);
                        per = calculatePercent(elementAssociatedWithLabel);
                        tmp = elementAssociatedWithLabel;
                    }
                }

            } else {

            }
        }
        Integer object_res = res;
        Double object_per = per;
        return new Pair<>(new Pair<>(object_res, object_per), tmp);
    }
    public int getMaxWeightInListElementOfText() {
        int max_res = 0;
        double max_per = 0;
        Element tmp_result = null;
        for (Element e : elementOfText) {
             Pair<Pair<Integer, Double>, Element> p = calculateWeightWithElementOfText(e);
             Pair<Integer, Double> first = p.getFirst();
             Integer res = first.getFirst();
             Double per = first.getSecond();
             Element second = p.getSecond();
             if (per > max_per) {
                 max_per = per;
                 max_res = res;
                 tmp_result = second;
             } else {
                 if (per == max_per) {
                     if (res > max_res) {
                         max_res = res;
                         tmp_result = second;
                     }
                 }
             }

        }
        result = tmp_result;
        return max_res;
    }


    @Override
    public boolean type(Element e) {
        if (e == null) {
            return false;
        }
        if (e.tagName().equals("textarea") || (e.tagName().equals("input") && !e.attr("type").equals("submit") && !e.attr("type").equals("checkbox")
        && !e.attr("type").equals("radio") && !e.attr("type").equals("hidden"))) {
            return true;
        }
        return false;
    }

    public boolean isLabel(Element e) {
        if (e == null) {
            return false;
        }
        if (e.tagName().equals("label") && e.hasAttr("for") && !e.attr("for").isEmpty()) {
            return true;
        }
        return false;
    }

    public int getWeight() {
        weight = calculateWeight(text) + getMaxWeightInListElementOfText();
        full = calculatePercent(text, result);
        return weight;
    }

    @Override
    public int compareTo(Weight o) {
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
