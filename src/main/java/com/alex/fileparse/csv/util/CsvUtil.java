package com.alex.fileparse.csv.util;

import com.alex.fileparse.csv.CsvBean;
import com.alex.fileparse.csv.CsvReadException;
import com.alex.fileparse.csv.CsvWriteException;
import com.alex.fileparse.csv.Title;
import com.alex.fileparse.csv.annotation.Column;
import com.alex.fileparse.csv.annotation.Pattern;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * CSV工具类
 * User: alex
 * Date: 13-8-7
 * Time: 上午11:39
 */
public class CsvUtil {
    private static final char CHAR_QUOTE = '"';
    private static final char CHAR_COMMA = ',';

    /**
     * 取得csv元素字符串<p>
     * RFC 4180：http://tools.ietf.org/html/rfc4180
     * wiki：http://zh.wikipedia.org/wiki/%E9%80%97%E5%8F%B7%E5%88%86%E9%9A%94%E5%80%BC
     * </p>
     *
     * @param object
     * @return
     */
    public static String getCellStr(String object) {
        if (object == null)
            return "\"\"";
        StringBuilder sb = new StringBuilder();
        sb.append("\"").append(object.toString().replaceAll("\"", "\"\"")).append("\"");
        return sb.toString();
    }

    public static int getRowCount(String filePath, String charsetName) throws CsvReadException {
        int rows = 0;
        try {
            InputStreamReader input = new InputStreamReader(new FileInputStream(filePath), charsetName);
            BufferedReader countReader = new BufferedReader(input);
            countReader.readLine();// 跳过标题行
            while (countReader.readLine() != null) {
                rows++;
            }
        } catch (IOException e) {
            throw new CsvReadException("查询总记录条数出错。", e);
        }
        return rows;
    }

    public static List<String> decodeLine(String line) {
        List<String> row = decodeRawLine(line);
        return decodeRawWords(row);
    }

    public static List<String> decodeRawLine(String rowLine) {
        assert rowLine != null;

        List<String> words = new ArrayList<String>();

        boolean quoteOn = false;
        StringBuffer sb = new StringBuffer();
        for (char c : rowLine.toCharArray()) {
            if (c == CHAR_COMMA) {
                if (quoteOn) {
                    sb.append(c);
                } else {
                    words.add(sb.toString().trim());
                    sb = new StringBuffer();
                }
            } else if (c == CHAR_QUOTE) {
                quoteOn = !quoteOn;
                sb.append(c);
            } else {
                sb.append(c);
            }
        }
        words.add(sb.toString().trim());
        return words;
    }

    public static Map<Title, Field> getTitleFields(String titleLine, Class clazz) {
        Map<Title, Field> titleMap = new HashMap<Title, Field>();
        List<String> captions = decodeLine(titleLine);
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < captions.size(); i++) {
            Title title = new Title(captions.get(i), i);
            titleMap.put(title, getFieldByCaption(fields, title.getCaption()));
        }
        return titleMap;
    }

    private static Field getFieldByCaption(Field[] fields, String caption) {
        for (Field field : fields) {
            String fieldCaption = getFieldCaption(field);
            if (caption.equals(fieldCaption)) {
                return field;
            }
        }
        return null;
    }

    private static String getFieldCaption(Field field) {
        Annotation annotation = field.getAnnotation(Column.class);
        if (annotation == null)
            return null;
        return ((Column) annotation).name();
    }

    private static List<String> decodeRawWords(List<String> rawWords) {
        assert rawWords != null;
        List<String> words = new ArrayList<String>();
        for (String rawWord : rawWords) {
            words.add(decodeRawWord(rawWord));
        }
        return words;
    }

    private static String decodeRawWord(String rawWord) {
        if (rawWord == null || "".equals(rawWord)) {
            return rawWord;
        }

        char[] cs = rawWord.toCharArray();

        boolean quoted = cs[0] == CHAR_QUOTE && cs[cs.length - 1] == CHAR_QUOTE;
        if (!quoted) {
            return rawWord;
        }

        boolean quoteOn = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < cs.length - 1; i++) {
            char c = cs[i];
            if (c == CHAR_QUOTE) {
                if (quoteOn) {
                    sb.append(c);
                }
                quoteOn = !quoteOn;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static <T extends CsvBean> String getFieldValue(T bean, String title) throws CsvWriteException {
        try {
            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                String fieldTitle = getFieldCaption(field);
                if (fieldTitle == null || fieldTitle.equals(title) == false)
                    continue;
                field.setAccessible(true);
                Object value = field.get(bean);
                Class type = field.getType();
                Pattern patternAnnotation = field.getAnnotation(Pattern.class);
                String fieldStringValue = CsvUtil.getFieldStringValue(value, type, patternAnnotation == null ? null : patternAnnotation.value());
                boolean needTab = field.getAnnotation(Column.class).needTab();
                if (needTab)
                    fieldStringValue = "\t" + fieldStringValue;
                return fieldStringValue;
            }
        } catch (Exception e) {
            throw new CsvWriteException("取对象值出错。", e);
        }
        return "";
    }

    private static String getFieldStringValue(Object value, Class type, String pattern) throws CsvWriteException {
        if (value == null)
            return "";

        if (type == String.class) {
            return value.toString();
        } else if (type == Date.class) {
            if (pattern == null)
                pattern = "yyyy-MM-dd";
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.format(value);
        } else if (type == BigDecimal.class) {
            return ((BigDecimal) value).toPlainString();
        } else if (type == Integer.class) {
            return String.valueOf(value);
        } else if (type == int.class) {
            return String.valueOf(value);
        } else if (type == long.class) {
            return String.valueOf(value);
        } else if (type == Long.class) {
            return String.valueOf(value);
        } else if (type == float.class) {
            return String.valueOf(value);
        } else if (type == Float.class) {
            return String.valueOf(value);
        } else if (type == double.class) {
            return String.valueOf(value);
        } else if (type == Double.class) {
            return String.valueOf(value);
        } else if (type == short.class) {
            return String.valueOf(value);
        } else if (type == Short.class) {
            return String.valueOf(value);
        } else if (type == boolean.class) {
            return String.valueOf(value);
        } else if (type == Boolean.class) {
            return String.valueOf(value);
        } else if (type == char.class) {
            return String.valueOf(value);
        }
        throw new CsvWriteException("不支持的数据类型" + type.getName());
    }
}
