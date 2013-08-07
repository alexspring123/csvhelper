package com.alex.fileparse.csv.util;

import com.alex.fileparse.csv.CsvBean;
import com.alex.fileparse.csv.CsvReadException;
import com.alex.fileparse.csv.ProcessResult;
import com.alex.fileparse.csv.Title;
import com.alex.fileparse.csv.validator.FieldValidator;
import com.alex.fileparse.csv.validator.ValidatorResult;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    public static void reflectField(Object bean, Field field, Object fieldValue) throws CsvReadException {
        String setterName = getSetter(field.getName());
        Method setMethod;
        try {
            setMethod = bean.getClass().getMethod(setterName, field.getType());
        } catch (NoSuchMethodException e) {
            throw new CsvReadException("类" + bean.getClass().getName() + "中不存在方法" + setterName + "。", e);
        }

        try {
            setMethod.invoke(bean, fieldValue);
        } catch (IllegalAccessException e) {
            throw new CsvReadException("调用类" + bean.getClass().getName() + "中方法" + setterName + "出错。", e);
        } catch (InvocationTargetException e) {
            throw new CsvReadException("调用类" + bean.getClass().getName() + "中方法" + setterName + "出错。", e);
        }
    }

    public static <T extends CsvBean> T reflectBean(Map<Title, Field> titleMap, Class<T> clazz, String line, FieldValidator validator) throws CsvReadException {
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
                reflectField(bean, field, result.getValue());
            } else {
                bean.setResult(ProcessResult.failed);
                bean.addMessage(title.getCaption() + ":" + result.getErrorMsg());
            }
        }
        return bean;
    }

    private static <T extends CsvBean> T createNewBean(Class<T> clazz) throws CsvReadException {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new CsvReadException("反射" + clazz.getName() + "失败。", e);
        }
    }

    private static String getSetter(String fieldName) {
        if (StringUtils.isBlank(fieldName))
            return null;
        String fieldFirstUpperChar = fieldName.substring(0, 1).toUpperCase();
        return "set" + fieldFirstUpperChar + fieldName.substring(1);
    }
}
