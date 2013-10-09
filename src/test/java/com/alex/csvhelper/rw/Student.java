package com.alex.csvhelper.rw;

import com.alex.csvhelper.CsvBean;
import com.alex.csvhelper.annotation.Column;
import com.alex.csvhelper.annotation.Min;
import com.alex.csvhelper.annotation.Pattern;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-9-30
 * Time: 下午1:24
 * To change this template use File | Settings | File Templates.
 */
public class Student extends CsvBean {
  @Column(name = "名称", nullable = false, length = 20)
  private String name;
  @Column(name = "生日", nullable = false)
  @Pattern(value = "yyyy-mm-dd", description = "必须使用yyyy-mm-dd格式。")
  private Date bothday;
  @Column(name = "身高", nullable = false, precision = 19, scale = 2)
  private long high;
  @Column(name = "年龄", nullable = false)
  @Min(value = 0, allowEqual = false)
  private int age;
  @Column(name = "体重", precision = 19, scale = 2)
  private BigDecimal weight;
  @Column(name = "岗位")
  private String position;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getHigh() {
    return high;
  }

  public void setHigh(long high) {
    this.high = high;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public BigDecimal getWeight() {
    return weight;
  }

  public void setWeight(BigDecimal weight) {
    this.weight = weight;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public Date getBothday() {
    return bothday;
  }

  public void setBothday(Date bothday) {
    this.bothday = bothday;
  }
}
