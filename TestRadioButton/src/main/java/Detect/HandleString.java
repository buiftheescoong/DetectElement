package Detect;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandleString {
    public static String lowercaseString(String s){
        return s.toLowerCase();
    }

    public static List<String> separateWordsInString(String s) {
        Pattern pattern = Pattern.compile("([A-Z]?[a-z]+|[A-Z]+|[0-9]+)");
        Matcher matcher = pattern.matcher(s);

        List<String> res = new ArrayList<>();
        while (matcher.find()) {
            res.add(matcher.group());
        }
        return res;
    }

    public static void lowercaseWordsInList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            list.set(i, lowercaseString(list.get(i)));
        }
    }
}
