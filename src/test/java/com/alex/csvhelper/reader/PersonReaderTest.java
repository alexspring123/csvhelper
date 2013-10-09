package com.alex.csvhelper.reader;

import com.alex.csvhelper.CsvReadException;
import com.alex.csvhelper.CsvReader;
import com.alex.csvhelper.ProcessResult;
import junit.framework.Assert;
import org.junit.Before;
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
public class PersonReaderTest {
  String file = null;

  @Before
  public void setup() {
    URL url = PersonReaderTest.class.getResource("");
    file = url.getPath() + "person.csv";
  }

  @Test
  public void testPerson() throws CsvReadException, IOException {
    CsvReader<Person> reader = new CsvReader(file, Person.class);
    try {
      List<Person> list = reader.getAllBeans();
      Assert.assertEquals("取得记录行数错误。", 4, list.size());
      Assert.assertEquals("person.csv第一行检查错误。", ProcessResult.success, list.get(0).getResult());
      Assert.assertEquals("person.csv第二行检查错误。", ProcessResult.ignore, list.get(1).getResult());
      Assert.assertEquals("person.csv第三行检查错误。", ProcessResult.failed, list.get(2).getResult());
      Assert.assertEquals("person.csv第四行检查错误。", ProcessResult.failed, list.get(3).getResult());
    } finally {
      reader.close();
    }
  }

  @Test(expected = CsvReadException.class)
  public void testPersonHasExtraField() throws CsvReadException, IOException {
    CsvReader<PersonHasExtraField> reader = new CsvReader(file, PersonHasExtraField.class);
    try {
      reader.getAllBeans();
    } finally {
      reader.close();
    }
  }
}
