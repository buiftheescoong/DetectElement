package Detect;

import java.util.Arrays;
import java.util.List;

public interface Setting {
    List<String> EXCEPT_ATTRS = Arrays.asList("autocorrect", "spellcheck", "tabindex", "style", "pattern", "aria-hidden", "maxlength", "minlength", "max", "min", "height", "width", "size", "step");
    List<String> STOP_WORDS = Arrays.asList("for", "the", "do", "did", "does", "this", "to", "of", "with", "and", "or", "have", "has", "as", "is", "in");
    List<String> HEURISTIC_STOP_WORDS = Arrays.asList(
            "btn",
            "link",
            "form",
            "svg",
            "www",
            "https",
            "http",
            "com",
            "js",
            "css",
            "true",
            "false",
            "checked");
}
