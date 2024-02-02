package Detect;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.List;
import java.util.Set;




public class Weight implements Comparable<Weight> {
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
        if (elementOfText.size() == 1 && HandleElement.isInputElement(elementOfText.get(0)) && elementOfText.get(0).ownText().isEmpty()) {
            return 0;
        }
        return Calculator.weightBetweenTwoString(source, target);
    }

    public double calculatePercent(String target,Element e) {
        double res = 0;
        if (target.isEmpty() || e == null) {
            return 0;
        }
        Set<String> visitedWord = new HashSet<>();
        List<String> wordsInSource = HandleString.separateWordsInString(source);
        HandleString.lowercaseWordsInList(wordsInSource);
        Set<String> distinctWordsInSource = new HashSet<>(wordsInSource);
        Calculator.calculatePercentBetweenTwoStrings(source, target, visitedWord);
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
        return res;
    }


    public int calculateWeight(Element e) {
        int res = 0;
        if (e == null || e.attributes().size() == 0) {
            return 0;
        }
//        List<String> wordsInSource = HandleString.separateWordsInString(source);
//        HandleString.lowercaseWordsInList(wordsInSource);
//        List<String> distinctWordsInSource = HandleString.distinctWordsInString(wordsInSource);
        Attributes attributes = e.attributes();
        if (e.attributesSize() > 0) {
            for (Attribute attr : attributes) {
                String typeAttr = attr.getKey();
                if (!Setting.except_attrs.contains(typeAttr)) {
                    String valueOfAttr = attr.getValue();
                    if (!valueOfAttr.isEmpty()) {
                        res += Calculator.weightBetweenTwoString(source, valueOfAttr);
//                        List<String> wordsInValue = HandleString.separateWordsInString(valueOfAttr);
//                        HandleString.lowercaseWordsInList(wordsInValue);
//                        String tmp = "";
//                        for (int i = 0; i < wordsInValue.size(); i++) {
//
//                            int idx= HandleString.calculateWeightOfAttributeAndTextWords(i, distinctWordsInSource, tmp, wordsInValue);
//                            if (idx != -1) {
//                                i = idx;
//                                res += 1;
//                            }
//                        }
                    }
                }
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
        Set<String> distinctWordsInSource = new HashSet<>(wordsInSource);
        if (e.attributesSize() > 0) {
            Attributes attributes = e.attributes();
            for (Attribute attr : attributes) {
                String typeAttr = attr.getKey();
                if (!Setting.except_attrs.contains(typeAttr)) {
                    String valueOfAttr = attr.getValue();
                    if (!valueOfAttr.isEmpty()) {
//                        List<String> wordsInValue = HandleString.separateWordsInString(valueOfAttr);
//                        HandleString.lowercaseWordsInList(wordsInValue);
//                        String tmp = "";
//                        for (int i = 0; i < wordsInValue.size(); i++) {
//
//                            int idx= HandleString.calculateWeightOfAttributeAndTextWords(i, wordsInSource, tmp, wordsInValue, visitedWord);
//                            if (idx != -1) {
//                                i = idx;
//                            }
//                        }
                        Calculator.calculatePercentBetweenTwoStrings(source, valueOfAttr, visitedWord);
                    }
                }
            }
        }

        int size = visitedWord.size();
        res = 1.0 * size / distinctWordsInSource.size();
        return res;
    }
    public Pair<Pair<Integer, Double>, Element> calculateWeightWithElementOfText(Element e) {
        int res = 0;
        double per = 0;
        Element tmp = null;
        if (HandleElement.isInputElement(e)) {
            res = calculateWeight(e);
            per = calculatePercent(e);
            tmp = e;
        } else {
            if (HandleElement.isLabelHasForAttr(e)) {
                String id = e.attr("for");
                Elements elements = doc.select("#"+ id);
                if (elements.isEmpty()) {
                    res = 0;
                } else {
                    Element elementAssociatedWithLabel = elements.get(0);
                    if (HandleElement.isInputElement(elementAssociatedWithLabel)) {
                        res = calculateWeight(elementAssociatedWithLabel);
                        per = calculatePercent(elementAssociatedWithLabel);
                        tmp = elementAssociatedWithLabel;
                    }
                }

            } else {
                Element elem = HandleElement.searchInputElementInSubtree(text, e);

                if (elem != null) {
                    res = calculateWeight(elem);
                    per = calculatePercent(elem);
                    tmp = elem;
                }
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
