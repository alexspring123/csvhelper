package com.alex.csvhelper;

import com.alex.csvhelper.annotation.Column;
import com.alex.csvhelper.util.CsvUtil;

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
public class CsvWriter<T> {
  private static final String TITLE_PROCESSRESULT = "处理结果";
  private static final String TITLE_MESSAGE = "原因";

  private static final String PROCESS_SUCCESS = "成功";
  private static final String PROCESS_FAILED = "失败";
  private static final String PROCESS_IGNORE = "忽略";

  private String targetFile;
  private List<String> titles;
  private BufferedWriter writer;
  private Class<T> beanClass;

  public CsvWriter(String targetFile, Class<T> beanClass) {
    this.targetFile = targetFile;
    this.beanClass = beanClass;
    initTitle();
  }

  private void initTitle() {
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
    for (int index = 0; index < titles.size(); index++) {
      if (index == 0)
        sb.append(CsvUtil.getCellStr(titles.get(index)));
      else
        sb.append(",").append(CsvUtil.getCellStr(titles.get(index)));
    }
    if (CsvBean.class.isAssignableFrom(beanClass)) {
      sb.append(TITLE_PROCESSRESULT).append(",").append(TITLE_MESSAGE);
    }
    sb.append("\n");
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
    for (int index = 0; index < titles.size(); index++) {
      if (index == 0)
        sb.append(CsvUtil.getCellStr(CsvUtil.getFieldValue(bean, titles.get(index))));
      else
        sb.append(",").append(CsvUtil.getCellStr(CsvUtil.getFieldValue(bean, titles.get(index))));
    }
    if (CsvBean.class.isAssignableFrom(beanClass)) {
      CsvBean csvBean = (CsvBean) bean;
      if (ProcessResult.success.equals(csvBean.getResult()))
        sb.append(PROCESS_SUCCESS).append(",");
      else if (ProcessResult.ignore.equals(csvBean.getResult()))
        sb.append(PROCESS_IGNORE).append(",");
      else if (ProcessResult.failed.equals(csvBean.getResult()))
        sb.append(PROCESS_FAILED).append(",");
      sb.append(CsvUtil.getCellStr(csvBean.getMessage() == null ? "" : csvBean.getMessage()));
    }
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
