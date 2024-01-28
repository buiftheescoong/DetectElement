package Detect;

import java.util.List;

public class CalculateWeight {
    public static int weightBetweenTwoString(String source, String target) {
        List<String> wordsInSource = HandleString.separateWordsInString(source);
        HandleString.lowercaseWordsInList(wordsInSource);
        List<String> wordsInTarget = HandleString.separateWordsInString(target);
        HandleString.lowercaseWordsInList(wordsInTarget);
        int res = 0;
        for (String w : wordsInSource) {
            if (wordsInTarget.contains(w)) {
                res++;
            }
        }
        return res;
    }

//    public static double percentBetweenTwoString(String source, String target) {
//
//    }
}
