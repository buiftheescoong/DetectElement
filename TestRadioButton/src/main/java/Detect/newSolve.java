package Detect;



import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.*;
import java.util.Map.Entry;

public class newSolve {

  static Vector<Element> interactableElements = new Vector<>();
  static Set<String> variable_list = new HashSet<>();
  static Set<String> visited = new HashSet<>();
  static Map<Pair<String, Element>, Double> storeVarEleAndWeight = new HashMap<>();
  static Map<Pair<String, Element>, Double> sortedMapStoreVarEleAndWeight = new LinkedHashMap<>();
  static Map<Pair<String,Element>, Vector<Attribute>> storeVarEleAndAttributesContainsVar = new HashMap<>();
  static Vector<String> result = new Vector<>();
  static final double weightAttributeId = 0.3;
  static final double weightAttributeName = 0.2;
  static final double weightOfText = 0.3;
  static final double weightAnotherAttribute = 0.01;


  public static Vector<String> getLocator(String[] input) {
    int length = input.length;

    for (int i = 0; i < length; i++) {
      if (input[i].contains(":")) {
        String linkHtml = input[i];
        int j;
        for (j = i + 1; j < length; j++) {
          if (!input[j].contains(":")) {
            variable_list.add(input[j]);
          } else {
            break;
          }
        }
        processGetLocator(linkHtml);
        setUp();
        i = j - 1;
        if (i == length - 1) {
          break;
        }
      }
    }
    return result;
  }
  public static void processGetLocator(String linkHtml) {
    String htmlContent = getHtmlContent(linkHtml);
    Document domTree = getDomTree(htmlContent);
    setStoreVarEleAndWeight();
    List<Entry<Pair<String, Element>, Double>> list = new ArrayList<>(storeVarEleAndWeight.entrySet());
    Collections.sort(list, new Comparator<Entry<Pair<String, Element>, Double>>() {
      @Override
      public int compare(Entry<Pair<String, Element>, Double> o1, Entry<Pair<String, Element>, Double> o2) {
        return o2.getValue().compareTo(o1.getValue());
      }
    });


    for (Entry<Pair<String, Element>, Double> entry : list) {
      sortedMapStoreVarEleAndWeight.put(entry.getKey(), entry.getValue());
    }
    matchVarAndELe();
  }

  public static void setUp() {
    interactableElements = new Vector<>();
    variable_list = new HashSet<>();
    visited = new HashSet<>();
    storeVarEleAndWeight = new HashMap<>();
    sortedMapStoreVarEleAndWeight = new LinkedHashMap<>();
    storeVarEleAndAttributesContainsVar = new HashMap<>();
  }

  public static String getHtmlContent(String linkHtml) {
//    System.setProperty("Webdriver.chrome.driver", "C:\\webdriver\\chromedriver.exe");
    WebDriver driver = new ChromeDriver();
    driver.get(linkHtml);
    String htmlContent = driver.getPageSource();
    driver.quit();
    return htmlContent;
  }

  public static Document getDomTree(String htmlContent) {
    Document domTree = Jsoup.parse(htmlContent);
    return domTree;
  }


  public static Elements searchInputElements(Document doc) {
    Elements res = new Elements();
    Elements ip = doc.getElementsByTag("input");
    Elements textarea = doc.getElementsByTag("textarea");
    res.addAll(ip);
    res.addAll(textarea);
    return res;
  }

  public static String getXpath(String variable, Element interactableElement, Vector<Attribute> storeAttributesContainsVar) {
    int attributes_size = storeAttributesContainsVar.size();
    String xpath = "";
    boolean havingPreviousAttribute = false;
    if (attributes_size > 0) {
      xpath += "//" + interactableElement.tagName() + "[";
      for (Attribute temp : storeAttributesContainsVar) {
        if (havingPreviousAttribute) {
          xpath += " and " + "@" + temp.getKey() + "=" + "'" + temp.getValue() + "'";
        } else {
          xpath += "@" + temp.getKey() + "=" + "'" + temp.getValue() + "'";
          havingPreviousAttribute = true;
        }
      }
    }
    Vector<String> wordsOfVariable = word_analysis(variable);
    String textOfElement = interactableElement.ownText();
    String analysisTextOfElement = beautifulString(textOfElement);
    boolean textOfElementHasVariable = false;
    for (String word : wordsOfVariable) {
      if (analysisTextOfElement.contains(word)) {
        textOfElementHasVariable = true;
      }
    }
    if (textOfElementHasVariable) {
      if (havingPreviousAttribute) {
        xpath += " and " + "normalize-space()=" + "'" + textOfElement + "'";
      } else {
        xpath += "normalize-space()=" + "'" + textOfElement + "'";
      }
    }
    xpath += "]";
    return xpath;
  }

  /** Chuyển các kí tự thành in thường, chỉ lấy kí tự số và a-z. */
  public static String beautifulString(String x) {
    String temp = "";
    String x_lower = x.toLowerCase();
    for (int i = 0; i < x_lower.length(); i++) {
      if ((x_lower.charAt(i) >= 'a' && x_lower.charAt(i) <= 'z') || (x_lower.charAt(i) >= '0'
          && x_lower.charAt(i) <= '9')) {
        temp += x_lower.charAt(i);
      }
    }
    return temp;
  }


  public static Vector<String> word_analysis(String variable) {
    Vector<String> single_word = new Vector<>();
    String[] words = variable.split("_");
    for (String x : words) {
      String x_value = "";
      for (int i = 0; i < x.length(); i++) {
        if ((x.charAt(i) >= 'a' && x.charAt(i) <= 'z') || (x.charAt(i) >= '0'
            && x.charAt(i) <= '9') || (x.charAt(i) >= 'A'
            && x.charAt(i) <= 'Z')) {
          x_value += x.charAt(i);
        }
      }
      boolean type = true;
      if (x_value.charAt(0) >= '0' && x_value.charAt(0) <= '9') {
        type = false;
      }
      String temp = "";
      temp += x_value.charAt(0);
      for (int i = 1; i < x_value.length(); i++) {
        if (x_value.charAt(i) >= 'A' && x_value.charAt(i) <= 'Z') {
          single_word.add(temp.toLowerCase());
          type = true;
          temp = "";
          temp += x_value.charAt(i);
        } else if (x_value.charAt(i) >= '0' && x_value.charAt(i) <= '9') {
          if (type) {
            single_word.add(temp.toLowerCase());
            temp = "";
            temp += x_value.charAt(i);
            type = false;
          } else {
            temp += x_value.charAt(i);
          }
        } else {
          temp += x_value.charAt(i);
        }
        if (i == x_value.length() - 1) {
          single_word.add(temp.toLowerCase());
        }
      }
    }

    return single_word;
  }

  public static double calculateWeight(String variable, Element e, Vector<Attribute> storeAttributesContainsVar) {
    Vector<String> wordsOfVariable = word_analysis(variable);
    int size = wordsOfVariable.size();
    double weight = 0;
    Attributes attributes = e.attributes();
    for (Attribute attr : attributes) {
      if (attr.getKey().equals("id")) {
        String analysis_id = beautifulString(attr.getValue());
        boolean hasContainsVar = false;
        for (String word : wordsOfVariable) {
          if (analysis_id.contains(word)) {
            weight += weightAttributeId / size;
            hasContainsVar = true;
          }
        }
        if (hasContainsVar) {
          storeAttributesContainsVar.add(attr);
        }
      } else if (attr.getKey().equals("name")) {
        String analysis_name = beautifulString(attr.getValue());
        boolean hasContainsVar = false;
        for (String word : wordsOfVariable) {
          if (analysis_name.contains(word)) {
            weight += weightAttributeName / size;
            hasContainsVar = true;
          }
        }
        if (hasContainsVar) {
          storeAttributesContainsVar.add(attr);
        }
      } else {
        String analysis_another_attribute = beautifulString(attr.getValue());
        boolean hasContainsVar = false;
        for (String word : wordsOfVariable) {
          if (analysis_another_attribute.contains(word)) {
            weight += weightAnotherAttribute / size;
            hasContainsVar = true;
          }
        }
        if (hasContainsVar) {
          storeAttributesContainsVar.add(attr);
        }
      }
    }
    String textOfElement = e.ownText();
    String analysisTextOfElement = beautifulString(textOfElement);
    for (String word : wordsOfVariable) {
      if (analysisTextOfElement.contains(word)) {
        weight += weightOfText / size;
      }
    }
    return weight;
  }

  public static void setStoreVarEleAndWeight() {
    for (String variable : variable_list) {
      for (Element e : interactableElements) {
        Pair<String, Element> mapVarEle = new Pair<>(variable, e);
        Vector<Attribute> storeAttributesContainsVar = new Vector<>();
        double weight = calculateWeight(variable, e, storeAttributesContainsVar);
        storeVarEleAndWeight.put(mapVarEle, weight);
        storeVarEleAndAttributesContainsVar.put(mapVarEle, storeAttributesContainsVar);
      }
    }
  }

  public static void matchVarAndELe () {
    for (Entry<Pair<String, Element>, Double> entry : sortedMapStoreVarEleAndWeight.entrySet()) {
      Pair<String, Element> pairVarAndEle = entry.getKey();
      String variable = pairVarAndEle.getFirst();
      Element e = pairVarAndEle.getSecond();

      if (variable_list.contains(variable) && interactableElements.contains(e) && entry.getValue() != 0) {
        Vector<Attribute> storeAttributesContainsVar = storeVarEleAndAttributesContainsVar.get(pairVarAndEle);
        System.out.println(entry.getValue());
        result.add(variable + ": xpath=" + getXpath(variable,e, storeAttributesContainsVar) + "\n");
        variable_list.remove(variable);
        interactableElements.remove(e);
      }
      if (variable_list.size() == 0 || interactableElements.size() == 0) {
        return;
      }
    }
  }







  public static void main(String[] args) {
    System.out.println(beautifulString("user_Name"));

  }
}