package com.alex.fileparse.csv.validator;

import com.alex.fileparse.csv.annotation.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-6-3
 * Time: 下午3:00
 * To change this template use File | Settings | File Templates.
 */
public class PatternValidator implements Validator<Pattern> {
    @Override
    public String validate(Pattern annotation, Object value) {
        if (annotation == null || value == null)
            return null;

        if (value instanceof String) {
            if (!java.util.regex.Pattern.matches(annotation.value(), (String) value)) {
                if (StringUtils.isBlank(annotation.description()))
                    return "不符合规范，期望\"" + annotation.value() + "\"，实际\"。";
                else
                    return "不符合规范，期望\"" + annotation.description() + "\"。";

            }
        }
        return null;
    }
}
