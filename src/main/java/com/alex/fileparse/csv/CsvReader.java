package com.alex.fileparse.csv;

import com.alex.fileparse.csv.util.CsvUtil;
import com.alex.fileparse.csv.util.ReflectUtil;
import com.alex.fileparse.csv.validator.FieldValidator;
import org.apache.log4j.Logger;

import java.io.*;
import java.lang.reflect.Field;
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
 */
public class CsvReader<T extends CsvBean> {
    private static final String DEFAULT_CHARACTER = "GBK";
    private final Logger logger = Logger.getLogger(this.getClass());

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
        this.count = CsvUtil.getRowCount(filePath, charsetName);

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
            String titleLine = reader.readLine();
            if (titleLine == null)
                throw new CsvReadException("指定的文件不存在标题行。");
            titleMap = CsvUtil.getTitleFields(titleLine, beanClass);
        } catch (IOException e) {
            throw new CsvReadException("解析标题行出错。", e);
        }
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
        T bean = ReflectUtil.reflectBean(titleMap, beanClass, line, validator);
        bean.setLineNumber(lineNumber);
        return bean;
    }
}
