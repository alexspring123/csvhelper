package com.alex.fileparse.csv;

import com.alex.fileparse.csv.annotation.Column;
import com.alex.fileparse.csv.util.CsvUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
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
            sb.append(CsvUtil.getCellStr(title)).append(",");
        }
        sb.append(TITLE_PROCESSRESULT).append(",");
        sb.append(TITLE_MESSAGE).append("\n");
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
            sb.append(CsvUtil.getCellStr(CsvUtil.getFieldValue(bean, title))).append(",");
        }
        if (ProcessResult.success.equals(bean.getResult()))
            sb.append(PROCESS_SUCCESS).append(",");
        else if (ProcessResult.ignore.equals(bean.getResult()))
            sb.append(PROCESS_IGNORE).append(",");
        else if (ProcessResult.failed.equals(bean.getResult()))
            sb.append(PROCESS_FAILED).append(",");
        sb.append(CsvUtil.getCellStr(bean.getMessage() == null ? "" : bean.getMessage()));
        sb.append("\n");
        return sb.toString();
    }

    /**
     * 写入结束
     */
    public void end() throws IOException {
        writer.flush();
        writer.close();
    }
}
