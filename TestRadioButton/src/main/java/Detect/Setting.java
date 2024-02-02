package Detect;

import java.util.Arrays;
import java.util.List;

public interface Setting {
    List<String> except_attrs = Arrays.asList("autocorrect", "spellcheck", "tabindex", "style", "pattern", "aria-hidden", "maxlength", "minlength", "max", "min", "height", "width", "size", "step");

}
