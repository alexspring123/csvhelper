package com.alex.fileparse.csv;

import com.alex.fileparse.csv.annotation.Column;
import com.alex.fileparse.csv.validator.FieldValidator;
import com.alex.fileparse.csv.validator.ValidatorResult;
import org.apache.log4j.Logger;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<Title, Field> titleMap = new HashMap<Title, Field>();
    private FieldValidator validator = new FieldValidator();
    private int count = 0;
    private int rowIndex = 0;
    private Class<T> beanClass;


    public CsvReader(String filePath, String charsetName, Class<T> beanClass) throws CsvReadException {
        if (filePath == null)
            throw new IllegalArgumentException("参数filePath不能为NULL。");
        if (charsetName == null)
            throw new IllegalArgumentException("参数charsetName不能为NULL。");
        if (beanClass == null)
            throw new IllegalArgumentException("参数targetBean不能为NULL。");

        long sysTime = 0;
        if (logger.isDebugEnabled())
            sysTime = System.currentTimeMillis();

        this.beanClass = beanClass;
        initReader(filePath, charsetName);
        initTitle();
        this.count = getRowCount(filePath, charsetName);

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
            List<String> captions = decodeLine(line);

            Field[] fields = beanClass.getDeclaredFields();
            for (int i = 0; i < captions.size(); i++) {
                Title title = new Title(captions.get(i), i);
                titleMap.put(title, getFieldByCaption(fields, title.getCaption()));
            }
        } catch (IOException e) {
            throw new CsvReadException("解析标题行出错。", e);
        }
    }

    private Field getFieldByCaption(Field[] fields, String caption) {
        for (Field field : fields) {
            String fieldCaption = getFieldCaption(field);
            if (caption.equals(fieldCaption)) {
                return field;
            }
        }
        return null;
    }

    private int getRowCount(String filePath, String charsetName) throws CsvReadException {
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

    public List<T> getAllBeans() throws CsvReadException, IOException {
        return getBeans(count);
    }

    public List<T> getBeans(int pageSize) throws CsvReadException, IOException {
        long sysTime = 0;
        if (logger.isDebugEnabled()) {
            sysTime = System.currentTimeMillis();
        }
        List<T> beans = new ArrayList<T>();
        String line;
        int readCount = 0;
        while ((line = reader.readLine()) != null) {
            readCount++;
            beans.add(convert(++rowIndex, line));
            if (readCount >= pageSize)
                break;
        }
        if (logger.isDebugEnabled())
            logger.debug("getBeans耗时：" + (System.currentTimeMillis() - sysTime) + "ms。");
        return beans;
    }

    private T convert(int lineNumber, String line) throws CsvReadException {
        T bean = createNewBean();
        bean.setLineNumber(lineNumber);
        if (line == null || "".equals(line)) {
            bean.setResult(ProcessResult.ignore);
            bean.addMessage("空行。");
            return bean;
        }

        List<String> row = decodeLine(line);
        int titleCount = titleMap.keySet().size();
        int rowCount = row.size();
        if (rowCount < titleCount) {
            bean.setResult(ProcessResult.failed);
            bean.addMessage("缺少列，期望" + titleMap.keySet().size() + "列，实际" + row.size() + "列 。");
            return bean;
        }

        for (Title title : titleMap.keySet()) {
            Field field = titleMap.get(title);
            if (field == null)
                continue;
            String content = row.get(title.getColumn());
            ValidatorResult result = validator.validate(field, content);
            if (result.isIsvalid()) {
                convertField(bean, field, result.getValue());
            } else {
                bean.setResult(ProcessResult.failed);
                bean.addMessage(title.getCaption() + ":" + result.getErrorMsg());
            }
        }
        return bean;
    }

    private T createNewBean() throws CsvReadException {
        try {
            return beanClass.newInstance();
        } catch (Exception e) {
            throw new CsvReadException("反射" + beanClass.getName() + "失败。", e);
        }
    }

    private String getFieldCaption(Field field) {
        Annotation annotation = field.getAnnotation(Column.class);
        if (annotation == null)
            return null;
        return ((Column) annotation).name();
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

    private String getSetter(String fieldName) {
        assert fieldName != null;
        String fieldFirstUpperChar = fieldName.substring(0, 1).toUpperCase();
        return "set" + fieldFirstUpperChar + fieldName.substring(1);
    }

}
