package com.alex.fileparse.csv;

import com.alex.fileparse.csv.annotation.Column;
import com.alex.fileparse.csv.annotation.Pattern;
import com.alex.fileparse.csv.validator.Validator;
import org.apache.log4j.Logger;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * CSV文件解析器
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-5-26
 * Time: 下午6:08
 * To change this template use File | Settings | File Templates.
 */
public class CsvReader<T extends CsvBean> {
    private final Logger logger = Logger.getLogger(this.getClass());

    private static final char CHAR_QUOTE = '"';
    private static final char CHAR_COMMA = ',';
    private static final String DEFAULT_CHARACTER = "GBK";

    private BufferedReader reader;
    private List<String> titles;
    private int count = 0;
    private int rowIndex = 0;
    private String beanClassName;

    public CsvReader(String filePath, String charsetName, Class<T> targetBean) throws CsvReadException {
        if (filePath == null)
            throw new IllegalArgumentException("参数filePath不能为NULL。");
        if (charsetName == null)
            throw new IllegalArgumentException("参数charsetName不能为NULL。");
        if (targetBean == null)
            throw new IllegalArgumentException("参数targetBean不能为NULL。");

        long sysTime = 0;
        if (logger.isDebugEnabled()) {
            sysTime = System.currentTimeMillis();
        }
        initReader(filePath, charsetName);
        initTitle();
        initRowCount(filePath, charsetName);
        beanClassName = targetBean.getName();
        if (logger.isDebugEnabled())
            logger.debug("创建CsvReader耗时：" + (System.currentTimeMillis() - sysTime) + "ms。");
    }

    public CsvReader(String filePath, Class<T> targetBean) throws CsvReadException {
        this(filePath, DEFAULT_CHARACTER, targetBean);
    }

    private void initReader(String filePath, String charsetName) throws CsvReadException {
        try {
            InputStreamReader input = new InputStreamReader(new FileInputStream(filePath), charsetName);
            this.reader = new BufferedReader(input);
        } catch (FileNotFoundException e) {
            throw new CsvReadException("文件" + filePath + "不存在。", e);
        } catch (UnsupportedEncodingException e) {
            throw new CsvReadException("字符集" + charsetName + "不存在。", e);
        }
    }

    private void initTitle() throws CsvReadException {
        try {
            String line = reader.readLine();
            if (line == null)
                throw new CsvReadException("指定的文件不存在标题行。");
            titles = decodeLine(line);
        } catch (IOException e) {
            throw new CsvReadException("解析标题行出错。", e);
        }
    }

    private void initRowCount(String filePath, String charsetName) throws CsvReadException {
        long sysTime = 0;
        if (logger.isDebugEnabled()) {
            sysTime = System.currentTimeMillis();
        }
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
        this.count = rows;

        if (logger.isDebugEnabled())
            logger.debug("initRowCount耗时：" + (System.currentTimeMillis() - sysTime) + "ms。");
    }

    private T reflectBean(String beanClassName) throws CsvReadException {
        try {
            Class<T> rootClass = (Class<T>) Class.forName(beanClassName);
            return rootClass.newInstance();
        } catch (Exception e) {
            throw new CsvReadException("创建" + beanClassName + "失败。");
        }
    }

    private List<String> decodeLine(String line) {
        List<String> row = decodeRawLine(line);
        return decodeRawWords(row);
    }

    private List<String> decodeRawLine(String rowLine) {
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

    private List<String> decodeRawWords(List<String> rawWords) {
        assert rawWords != null;
        List<String> words = new ArrayList<String>();
        for (String rawWord : rawWords) {
            words.add(decodeRawWord(rawWord));
        }
        return words;
    }

    private String decodeRawWord(String rawWord) {
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

    public boolean hasMore() throws IOException {
        return rowIndex < count;
    }

    public int getCount() {
        return count;
    }

    public List<T> getBeans(int rowsCount) throws CsvReadException, IOException {
        long sysTime = 0;
        if (logger.isDebugEnabled()) {
            sysTime = System.currentTimeMillis();
        }
        boolean limitRows = rowsCount > 0;
        List<T> beans = new ArrayList<T>();
        String line;
        int hasReadCount = 0;
        while ((line = reader.readLine()) != null) {
            hasReadCount++;
            beans.add(convert(++rowIndex, line));
            if (limitRows && hasReadCount >= rowsCount)
                break;
        }
        if (logger.isDebugEnabled())
            logger.debug("getBeans耗时：" + (System.currentTimeMillis() - sysTime) + "ms。");
        return beans;
    }

    private T convert(int lineNumber, String line) throws CsvReadException {
        List<String> row = decodeLine(line);
        T bean = reflectBean(beanClassName);
        bean.setLineNumber(lineNumber);
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            String title = getTitle(field);
            if (title == null)
                continue;
            Object fieldValue = getFieldValue(row, field, title);
            bean.addErrorMsg(validate(title, field, fieldValue));
            convertField(bean, field, fieldValue);
        }
        return bean;
    }

    private String getTitle(Field field) {
        Annotation annotation = field.getAnnotation(Column.class);
        if (annotation == null)
            return null;
        return ((Column) annotation).name();
    }

    private Object getFieldValue(List<String> row, Field field, String title) throws CsvReadException {
        String content = getFieldContent(row, title);
        Pattern patternAnnotation = field.getAnnotation(Pattern.class);
        return parserFieldValue(field.getType(), content, patternAnnotation == null ? null : patternAnnotation.value());
    }

    private String getFieldContent(List<String> row, String title) throws CsvReadException {
        int titleIndex = titles.indexOf(title);
        if (titleIndex == -1) {
            throw new CsvReadException("标题\"" + title + "\"不存在。");
        }
        return row.get(titleIndex);
    }

    private Object parserFieldValue(Class fieldClass, Object value, String pattern) throws CsvReadException {
        if (value == null || "".equals(value.toString()))
            return null;

        if (fieldClass == String.class) {
            return value;
        } else if (fieldClass == Date.class) {
            if (pattern == null)
                pattern = "yyyy-MM-dd";
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            Date date = null;
            try {
                date = dateFormat.parse(value.toString());
            } catch (ParseException e) {
            }
            return date;
        } else if (fieldClass == BigDecimal.class) {
            return new BigDecimal(value.toString());
        } else if (fieldClass == Integer.class) {
            return Integer.valueOf(value.toString());
        } else if (fieldClass == int.class) {
            return Integer.valueOf(value.toString()).intValue();
        } else if (fieldClass == long.class) {
            return Long.valueOf(value.toString()).longValue();
        } else if (fieldClass == Long.class) {
            return Long.valueOf(value.toString());
        } else if (fieldClass == float.class) {
            return Float.valueOf(value.toString()).floatValue();
        } else if (fieldClass == Float.class) {
            return Float.valueOf(value.toString());
        } else if (fieldClass == double.class) {
            return Double.valueOf(value.toString()).doubleValue();
        } else if (fieldClass == Double.class) {
            return Double.valueOf(value.toString());
        } else if (fieldClass == short.class) {
            return Short.valueOf(value.toString()).shortValue();
        } else if (fieldClass == Short.class) {
            return Short.valueOf(value.toString());
        } else if (fieldClass == boolean.class) {
            return Boolean.valueOf(value.toString()).booleanValue();
        } else if (fieldClass == Boolean.class) {
            return Boolean.valueOf(value.toString());
        } else if (fieldClass == char.class) {
            return value.toString().charAt(0);
        }
        throw new CsvReadException("不支持的数据类型" + fieldClass.getName());
    }

    private void convertField(T bean, Field field, Object fieldValue) throws CsvReadException {
        assert field != null;
        String setterName = getSetter(field.getName());
        Method setMethod;
        try {
            setMethod = bean.getClass().getMethod(setterName, field.getType());
        } catch (NoSuchMethodException e) {
            throw new CsvReadException("类" + bean.getClass().getName() + "中不存在方法" + setterName + "。", e);
        }

        try {
            setMethod.invoke(bean, new Object[]{fieldValue});
        } catch (IllegalAccessException e) {
            throw new CsvReadException("调用类" + bean.getClass().getName() + "中方法" + setterName + "出错。", e);
        } catch (InvocationTargetException e) {
            throw new CsvReadException("调用类" + bean.getClass().getName() + "中方法" + setterName + "出错。", e);
        }
    }

    private String validate(String title, Field field, Object value) {
        StringBuilder sb = new StringBuilder();
        for (Annotation annotation : field.getAnnotations()) {
            String errorMsg = Validator.getInstance(annotation).validate(value);
            if (errorMsg != null)
                sb.append(errorMsg);
        }
        if (sb.length() > 0) {
            return title + sb.toString();
        } else
            return null;
    }

    private String getSetter(String fieldName) {
        assert fieldName != null;
        String fieldFirstUpperChar = fieldName.substring(0, 1).toUpperCase();
        return "set" + fieldFirstUpperChar + fieldName.substring(1);
    }

}
