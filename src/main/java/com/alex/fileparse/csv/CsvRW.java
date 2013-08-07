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
 * CSV文件读写器
 * User: alex
 * Date: 13-8-7
 * Time: 下午12:08
 * To change this template use File | Settings | File Templates.
 */
public class CsvRW<T extends CsvBean> {
    private static final String TITLE_PROCESSRESULT = "处理结果";
    private static final String TITLE_MESSAGE = "原因";

    private static final String PROCESS_SUCCESS = "成功";
    private static final String PROCESS_FAILED = "失败";
    private static final String PROCESS_IGNORE = "忽略";

    private static final String DEFAULT_CHARACTER = "GBK";
    private final Logger logger = Logger.getLogger(this.getClass());

    private Map<Title, Field> titleMap = new HashMap<Title, Field>();
    private FieldValidator validator = new FieldValidator();

    private BufferedReader reader;
    private BufferedWriter writer;
    private Class<T> beanClass;
    private String titleLine;
    private int rowIndex = 0;
    private int count = 0;

    public CsvRW(String inFile, String outFile, Class<T> targetBean) throws CsvReadException, CsvWriteException {
        this(inFile, outFile, DEFAULT_CHARACTER, targetBean);
    }

    public CsvRW(String inFile, String outFile, String charsetName, Class<T> beanClass) throws CsvReadException, CsvWriteException {
        if (inFile == null)
            throw new IllegalArgumentException("参数inFile不能为NULL。");
        if (outFile == null)
            throw new IllegalArgumentException("参数outFile不能为NULL。");
        if (charsetName == null)
            throw new IllegalArgumentException("参数charsetName不能为NULL。");
        if (beanClass == null)
            throw new IllegalArgumentException("参数targetBean不能为NULL。");

        long sysTime = 0;
        if (logger.isDebugEnabled())
            sysTime = System.currentTimeMillis();

        this.beanClass = beanClass;
        initReaderAndWriter(inFile, outFile, charsetName);
        initTitle();
        this.count = CsvUtil.getRowCount(inFile, charsetName);

        if (logger.isDebugEnabled())
            logger.debug("创建CsvRW耗时：" + (System.currentTimeMillis() - sysTime) + "ms。");
    }

    private void initReaderAndWriter(String inFile, String outFile, String charsetName) throws CsvReadException, CsvWriteException {
        try {
            InputStreamReader input = new InputStreamReader(new FileInputStream(inFile), charsetName);
            this.reader = new BufferedReader(input);
            this.writer = new BufferedWriter(new FileWriter(outFile));
        } catch (FileNotFoundException e) {
            throw new CsvReadException("文件" + inFile + "不存在。", e);
        } catch (UnsupportedEncodingException e) {
            throw new CsvReadException("字符集" + charsetName + "不存在。", e);
        } catch (IOException e) {
            throw new CsvWriteException("创建输出文件" + outFile + "失败。", e);
        }
    }

    private void initTitle() throws CsvReadException {
        try {
            titleLine = reader.readLine();
            if (titleLine == null)
                throw new CsvReadException("指定的文件不存在标题行。");
            titleMap = CsvUtil.getTitleFields(titleLine, beanClass);
            writer.append(titleLine).append(",").append(TITLE_PROCESSRESULT).
                    append(",").append(TITLE_MESSAGE).append("\n");
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

    public void writerBeans(List<T> beans) throws CsvWriteException {
        try {
            for (T bean : beans) {
                writer.append(getBeanLine(bean));
            }
        } catch (Exception e) {
            throw new CsvWriteException("将对象写入文件出错。", e);
        }
    }

    public void end() throws IOException {
        writer.flush();
        writer.close();
    }

    private String getBeanLine(T bean) throws CsvWriteException {
        StringBuilder sb = new StringBuilder();
        sb.append(bean.getContent()).append(",");
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

    private T convert(int lineNumber, String line) throws CsvReadException {
        T bean = ReflectUtil.reflectBean(titleMap, beanClass, line, validator);
        bean.setLineNumber(lineNumber);
        bean.setContent(line);
        return bean;
    }

}
