package Detect;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.*;

public class CheckboxElement {
    static Map<String, List<Element>> textAndElement = new HashMap<>();
    static List<String> listText = new ArrayList<>();
    static List<Element> listCheckbox = new ArrayList<>();
    public static void getAllTextForCheckbox(Element e) {
        if (e == null || HandleElement.isInteractableElement(e)) {
            return;
        }
        if (HandleElement.isCheckboxElement(e)) {
            listCheckbox.add(e);
        }
        String text = e.ownText();
        if (!text.isEmpty() && !listText.contains(text)) {
            listText.add(text);
        }
        if (!text.isEmpty()) {
            if (textAndElement.containsKey(text)) {
                List<Element> list = textAndElement.get(text);
                if (!list.contains(e)) {
                    list.add(e);
                }
            } else {
                List<Element> list = new ArrayList<>();
                list.add(e);
                textAndElement.put(text, list);
            }
        }
        for (Element child : e.children()) {
            getAllTextForCheckbox(child);
        }
    }


    public static Map<Pair<String, String>, String> detectCheckboxElement(Map<String, List<String>> map, Document document) {
        Map<Pair<String, String>, String> res = new HashMap<>();
        Element body = document.body();
        getAllTextForCheckbox(body);
        List<String> choicesHasNoCorrespondingQuestion = new ArrayList<>();
        if (map.containsKey("")) {
            choicesHasNoCorrespondingQuestion = map.get("");
        }
        List<WeightCheckbox> listWeight = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String question = entry.getKey();
            if (question.isEmpty()) {
                continue;
            }
            List<String> choices = entry.getValue();
            for (String text : listText) {
                if (Calculator.weightBetweenTwoString(question,text) > 0) {
                    List<Element> list = textAndElement.get(text);
                    WeightCheckbox w = new WeightCheckbox(question, text, list, choices);
                    listWeight.add(w);
                }
            }
        }

        if (listWeight.size() == 1) {
            listWeight.get(0).getWeight();
        } else {
            Collections.sort(listWeight);
        }


        List<String> visitedQuestions = new ArrayList<>();
        List<Element> visitedCheckbox = new ArrayList<>();
        for (int i = listWeight.size() - 1; i >= 0; i--) {
            String source = listWeight.get(i).source;
            Map<String, Element> result = listWeight.get(i).result;
            if (result != null && !visitedQuestions.contains(source)) {
                visitedQuestions.add(source);
                for (Map.Entry<String, Element> entry : result.entrySet()) {
                    String choice = entry.getKey();
                    Element checkbox = entry.getValue();
                    visitedCheckbox.add(checkbox);
                    res.put(new Pair<>(source, choice), Process.getXpath(checkbox));
                }
            }
        }
        if (choicesHasNoCorrespondingQuestion.size() > 0) {
            List<String> visitedChoices = new ArrayList<>();
            for (Element checkbox : listCheckbox) {
                if (!visitedCheckbox.contains(checkbox)) {
                    String text = HandleElement.getTextForCheckbox(checkbox);
                    if (!visitedChoices.contains(text) && choicesHasNoCorrespondingQuestion.contains(text)) {
                        res.put(new Pair<>("", text), Process.getXpath(checkbox));
                        visitedCheckbox.add(checkbox);
                        visitedChoices.add(text);
                    }
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        String linkHtml = "https://form.jotform.com/233591762291461";
        String htmlContent = Process.getHtmlContent(linkHtml);
        Document document = Process.getDomTree(htmlContent);
        Map<String, List<String>> map = new HashMap<>();
        map.put("check conditions", Arrays.asList("Cancer", "Asthma", "Other"));
        map.put("check symptoms",  Arrays.asList("Chest pain", "Other"));

        Map<Pair<String, String>, String> res = detectCheckboxElement(map, document);
        for (Map.Entry<Pair<String, String>, String> entry : res.entrySet()) {
            Pair<String, String> pair = entry.getKey();
            String loc = entry.getValue();
            System.out.println(pair.getFirst() + " " + pair.getSecond() + " " + loc);
        }
    }
}
