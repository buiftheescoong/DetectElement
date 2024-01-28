package org.example;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
  public static void main(String[] args) {
    String chuoi = "q32_passengerName-Person _AAA-[prefix]";
    Pattern pattern = Pattern.compile("([A-Z]?[a-z]+|[A-Z]+|[0-9]+)");
    Matcher matcher = pattern.matcher(chuoi);

    java.util.List<String> tu_rieng_biet = new ArrayList<>();
    while (matcher.find()) {
      tu_rieng_biet.add(matcher.group());
    }

    System.out.println(tu_rieng_biet);
  }

}
