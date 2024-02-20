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
    public String type;
    public Weight(String source, String text, List<Element> list, Document document, String type) {
        this.source = source;
        this.text = text;
        this.doc = document;
        elementOfText = list;
        result = null;
        full = 0;
        weight = 0;
        this.type = type;
    }

    public int calculateWeight(String target) {

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
        List<String> distinctWordsInSource = HandleString.distinctWordsInString(wordsInSource);
        Calculator.calculatePercentBetweenTwoStrings(source, target, visitedWord);
        if (e.attributesSize() > 0) {
            Attributes attributes = e.attributes();
            for (Attribute attr : attributes) {
                String typeAttr = attr.getKey();
                if (!Setting.EXCEPT_ATTRS.contains(typeAttr)) {
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
        Attributes attributes = e.attributes();
        if (e.attributesSize() > 0) {
            for (Attribute attr : attributes) {
                String typeAttr = attr.getKey();
                if (!Setting.EXCEPT_ATTRS.contains(typeAttr)) {
                    String valueOfAttr = attr.getValue();
                    if (!valueOfAttr.isEmpty()) {
                        res += Calculator.weightBetweenTwoString(source, valueOfAttr);
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
        List<String> distinctWordsInSource = HandleString.distinctWordsInString(wordsInSource);
        if (e.attributesSize() > 0) {
            Attributes attributes = e.attributes();
            for (Attribute attr : attributes) {
                String typeAttr = attr.getKey();
                if (!Setting.EXCEPT_ATTRS.contains(typeAttr)) {
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
    public Pair<Pair<Integer, Double>, Element> calculateWeightWithElementOfText(Element e) {
        int res = 0;
        double per = 0;
        Element tmp = null;
        if (type.equals("input")) {
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
                    Element elem = HandleElement.searchSelectElementInSubtree(text, e);
                    if (elem != null) {
                        res = calculateWeight(elem);
                        per = calculatePercent(elem);
                        tmp = elem;
                    }
                }
            }
        }
        if (type.equals("click")) {
            if (HandleElement.isDefaultClickableElement(e)) {
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
                        if (HandleElement.isDefaultClickableElement(elementAssociatedWithLabel)) {
                            res = calculateWeight(elementAssociatedWithLabel);
                            per = calculatePercent(elementAssociatedWithLabel);
                            tmp = elementAssociatedWithLabel;
                        }
                    }

                } else {
                    if (!HandleElement.isInteractableElement(e)) {
                        res = calculateWeight(e);
                        per = calculatePercent(e);
                        tmp = e;
                    }
                }
            }
        }
        if (type.equals("select")) {
                if (HandleElement.isLabelHasForAttr(e)) {
                    String id = e.attr("for");
                    Elements elements = doc.select("#"+ id);
                    if (elements.isEmpty()) {
                        res = 0;
                    } else {
                        Element elementAssociatedWithLabel = elements.get(0);
                        if (HandleElement.isSelectElement(elementAssociatedWithLabel)) {
                            res = calculateWeight(elementAssociatedWithLabel);
                            per = calculatePercent(elementAssociatedWithLabel);
                            tmp = elementAssociatedWithLabel;
                        }
                    }

                } else {
                    Element ele = HandleElement.searchSelectElementInSubtree(text, e);
                    res = calculateWeight(ele);
                    per = calculatePercent(ele);
                    tmp = ele;
                }

        }

        Integer object_res = res;
        Double object_per = per;
        return new Pair<>(new Pair<>(object_res, object_per), tmp);
    }
    public int getMaxWeightInListElementOfText() {
        int max_res = -1;
        double max_per = -1;
        Element tmp_result = null;
        for (Element e : elementOfText) {
             Pair<Pair<Integer, Double>, Element> p = calculateWeightWithElementOfText(e);
             Pair<Integer, Double> first = p.getFirst();
             Integer res = first.getFirst();
             Double per = first.getSecond();
             Element second = p.getSecond();
             if (second == null) {
                 continue;
             }
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
        int weightBetweenTextAndSource = calculateWeight(text);
        weight = weightBetweenTextAndSource + getMaxWeightInListElementOfText();
        if (type.equals("input") && elementOfText.size() == 1 && HandleElement.isInputElement(elementOfText.get(0)) && elementOfText.get(0).ownText().isEmpty()) {
            weight -= weightBetweenTextAndSource;
        }
        if (type.equals("click") && HandleElement.isClickElementTagInput(result)) {
            weight -= weightBetweenTextAndSource;
        }
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
