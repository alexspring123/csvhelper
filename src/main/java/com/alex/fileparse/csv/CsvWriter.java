package com.alex.fileparse.csv;

import com.alex.fileparse.csv.annotation.Column;
import com.alex.fileparse.csv.annotation.Pattern;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-7-23
 * Time: 下午12:55
 * To change this template use File | Settings | File Templates.
 */
public class CsvWriter<T extends CsvBean> {
    private static final String TITLE_PROCESSRESULT = "处理结果";
    private static final String TITLE_MESSAGE = "原因";

    private static final String PROCESS_SUCCESS = "成功";
    private static final String PROCESS_FAILED = "失败";
    private static final String PROCESS_IGNORE = "忽略";

    private String targetFile;
    private List<String> titles;
    private BufferedWriter writer;

    public CsvWriter(String targetFile, Class<T> beanClass) {
        this.targetFile = targetFile;
        initTitle(beanClass);
    }

    private void initTitle(Class<T> beanClass) {
        titles = new ArrayList<String>();
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields) {
            String title = getTitle(field);
            if (title != null)
                titles.add(title);
        }
    }

    private String getTitle(Field field) {
        Annotation annotation = field.getAnnotation(Column.class);
        if (annotation == null)
            return null;
        return ((Column) annotation).name();
    }

    /**
     * 开始写入
     */
    public void start() throws IOException {
        writer = new BufferedWriter(new FileWriter(targetFile));
        writer.append(getTitleLine());
    }

    private String getTitleLine() {
        StringBuilder sb = new StringBuilder();
        for (String title : titles) {
            sb.append(getCellStr(title)).append(",");
        }
        sb.append(TITLE_PROCESSRESULT).append(",");
        sb.append(TITLE_MESSAGE).append("\n");
        return sb.toString();
    }

    /**
     * 取得csv元素字符串<p>
     * RFC 4180：http://tools.ietf.org/html/rfc4180
     * wiki：http://zh.wikipedia.org/wiki/%E9%80%97%E5%8F%B7%E5%88%86%E9%9A%94%E5%80%BC
     * </p>
     *
     * @param object
     * @return
     */
    private String getCellStr(String object) {
        if (object == null)
            return "\"\"";
        StringBuilder sb = new StringBuilder();
        sb.append("\"").append(object.toString().replaceAll("\"", "\"\"")).append("\"");
        return sb.toString();
    }

    /**
     * 追加内容
     */
    public void append(List<T> beans) throws CsvWriteException {
        try {
            for (T bean : beans) {
                writer.append(getBeanLine(bean));
            }
        } catch (Exception e) {
            throw new CsvWriteException("将对象写入文件出错。", e);
        }
    }

    private String getBeanLine(T bean) throws CsvWriteException {
        StringBuilder sb = new StringBuilder();
        for (String title : titles) {
            sb.append(getCellStr(getFieldValue(bean, title))).append(",");
        }
        if (ProcessResult.success.equals(bean.getResult()))
            sb.append(PROCESS_SUCCESS).append(",");
        else if (ProcessResult.ignore.equals(bean.getResult()))
            sb.append(PROCESS_IGNORE).append(",");
        else if (ProcessResult.failed.equals(bean.getResult()))
            sb.append(PROCESS_FAILED).append(",");
        sb.append(getCellStr(bean.getMessage() == null ? "" : bean.getMessage()));
        sb.append("\n");
        return sb.toString();
    }

    private String getFieldValue(T bean, String title) throws CsvWriteException {
        try {
            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                String fieldTitle = getTitle(field);
                if (fieldTitle == null || fieldTitle.equals(title) == false)
                    continue;
                field.setAccessible(true);
                Object value = field.get(bean);
                Class type = field.getType();
                Pattern patternAnnotation = field.getAnnotation(Pattern.class);
                String fieldStringValue = getFieldStringValue(value, type, patternAnnotation == null ? null : patternAnnotation.value());
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

    private String getFieldStringValue(Object value, Class type, String pattern) throws CsvWriteException {
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


    /**
     * 写入结束
     */
    public void end() throws IOException {
        writer.flush();
        writer.close();
    }
}
