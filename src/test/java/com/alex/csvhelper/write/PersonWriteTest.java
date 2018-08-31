package com.alex.csvhelper.write;

import com.alex.csvhelper.CsvReadException;
import com.alex.csvhelper.CsvReader;
import com.alex.csvhelper.CsvWriteException;
import com.alex.csvhelper.CsvWriter;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-9-30
 * Time: 下午1:29
 * To change this template use File | Settings | File Templates.
 */
public class PersonWriteTest {
  String file = null;

  @Before
  public void setup() {
    URL url = PersonWriteTest.class.getResource("");
    file = url.getPath() + "person.csv";
    new File(file).deleteOnExit();
  }

  @Test
  public void testPerson() throws IOException, CsvWriteException, CsvReadException {
    CsvWriter<Person> writer = new CsvWriter(file, Person.class);
    List<Person> list = getPersons();
    writer.start();
    writer.append(list);
    writer.end();

    CsvReader<PersonRead> reader = new CsvReader(file, "UTF-8", PersonRead.class);
    try {
      List<PersonRead> result = reader.getAllBeans();
      Assert.assertEquals("不是两行。", result.size(), list.size());
      for (int i = 0; i < result.size(); i++) {
        Assert.assertEquals("名称不同。", result.get(i).getName(), list.get(i).getName());
        Assert.assertEquals("年龄不同。", result.get(i).getAge(), list.get(i).getAge());
        Assert.assertEquals("身高不同。", result.get(i).getHigh(), list.get(i).getHigh());
        Assert.assertEquals("生日不同。", result.get(i).getBirthday(), list.get(i).getBirthday());
        Assert.assertEquals("岗位不同。", result.get(i).getPosition(), list.get(i).getPosition());
        Assert.assertEquals("体重不同。", result.get(i).getWeight(), list.get(i).getWeight());
      }

    } finally {
      reader.close();
    }

  }

  private List<Person> getPersons() {
    List<Person> list = new ArrayList<Person>();

    Person p1 = new Person();
    p1.setName("程新文");
    p1.setAge(20);
    p1.setHigh(7);
    p1.setBirthday(new Date(1984 - 1900, 10, 24));
    p1.setPosition("程序员");
    p1.setWeight(BigDecimal.valueOf(150));
    list.add(p1);

    Person p2 = new Person();
    p2.setName("程新文2");
    p2.setAge(20);
    p2.setHigh(7);
    p2.setBirthday(new Date(1984 - 1900, 10, 24));
    p2.setPosition("程序员2");
    p2.setWeight(BigDecimal.valueOf(150));
    list.add(p2);


    return list;
  }
}
