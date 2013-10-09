package com.alex.csvhelper.rw;

import com.alex.csvhelper.CsvRW;
import com.alex.csvhelper.CsvReadException;
import com.alex.csvhelper.CsvWriteException;
import com.alex.csvhelper.ProcessResult;
import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-9-30
 * Time: 下午1:29
 * To change this template use File | Settings | File Templates.
 */
public class PersonRW {

  @Test
  public void testPersonRW() throws CsvReadException, CsvWriteException, IOException {
    String fileName = "person.csv";
    URL url = PersonRW.class.getClassLoader().getResource("");
    CsvRW<Person> rw = new CsvRW(url.getPath() + fileName, url.getPath() + "person_bak.csv", Person.class);
    try {
      List<Person> list = rw.getAllBeans();
      Assert.assertEquals("取得记录行数错误。", 4, list.size());
      Assert.assertEquals("person.csv第一行检查错误。", ProcessResult.success, list.get(0).getResult());
      Assert.assertEquals("person.csv第二行检查错误。", ProcessResult.ignore, list.get(1).getResult());
      Assert.assertEquals("person.csv第三行检查错误。", ProcessResult.failed, list.get(2).getResult());
      Assert.assertEquals("person.csv第三行检查错误。", ProcessResult.failed, list.get(3).getResult());

      rw.writerBeans(list);
    } finally {
      try {
        rw.close();
      } catch (Exception e) {
      }
    }
  }
}
