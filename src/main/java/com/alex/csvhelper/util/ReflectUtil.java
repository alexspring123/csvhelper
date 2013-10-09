package com.alex.csvhelper.util;

import com.alex.csvhelper.CsvBean;
import com.alex.csvhelper.CsvReadException;
import com.alex.csvhelper.ProcessResult;
import com.alex.csvhelper.Title;
import com.alex.csvhelper.annotation.Column;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 反射工具类
 * User: alex
 * Date: 13-8-7
 * Time: 下午12:02
 * To change this template use File | Settings | File Templates.
 */
public class ReflectUtil {

  public static <T extends CsvBean> T reflectBean(Map<Title, Field> titleMap, Class<T> clazz, String line) throws CsvReadException {
    T bean = createNewBean(clazz);

    if (line == null || "".equals(line)) {
      bean.setResult(ProcessResult.ignore);
      bean.addMessage("空行。");
      return bean;
    }

    List<String> row = CsvUtil.decodeLine(line);
    int titleCount = titleMap.keySet().size();
    int rowCount = row.size();
    if (rowCount < titleCount) {
      bean.setResult(ProcessResult.failed);
      bean.addMessage("缺少列，标题有" + titleCount + "列，内容只有" + rowCount + "列 。");
      return bean;
    }

    Map<Field, String> values = new HashMap<Field, String>();
    for (Map.Entry<Title, Field> entry : titleMap.entrySet()) {
      Title title = entry.getKey();
      Field field = entry.getValue();
      if (field == null)
        continue;
      String content = row.get(title.getColumn());
      values.put(field, content);
    }

    CsvBeanInjector injector = new CsvBeanInjector();
    injector.inject(bean, values);
    return bean;
  }

  private static <T extends CsvBean> T createNewBean(Class<T> clazz) throws CsvReadException {
    try {
      return clazz.newInstance();
    } catch (Exception e) {
      throw new CsvReadException("反射" + clazz.getName() + "失败。", e);
    }
  }

  public static String getFieldCaption(Field field) {
    Column columnAnnotation = field.getAnnotation(Column.class);
    return columnAnnotation == null ? "" : columnAnnotation.name();
  }
}
