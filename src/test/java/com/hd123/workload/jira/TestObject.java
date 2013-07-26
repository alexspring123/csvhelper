package com.hd123.workload.jira;

import com.alex.fileparse.csv.CsvBean;
import com.alex.fileparse.csv.annotation.Column;
import com.alex.fileparse.csv.annotation.Pattern;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-7-22
 * Time: 下午9:02
 * To change this template use File | Settings | File Templates.
 */
public class TestObject extends CsvBean{
    @Column(name = "姓名", length = 10, nullable = false)
    private String column1;
    @Column(name = "生日", nullable = false)
    @Pattern(value = "yyyyMMdd")
    private Date column2;
    @Column(name = "年龄", nullable = true)
    private int column3;
    @Column(name = "身高", nullable = true)
    private double column4;
    @Column(name = "体重", nullable = true)
    private Double column5;
    @Column(name = "工作岗位（现在）", nullable = true)
    private String column6;

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public Date getColumn2() {
        return column2;
    }

    public void setColumn2(Date column2) {
        this.column2 = column2;
    }

    public int getColumn3() {
        return column3;
    }

    public void setColumn3(int column3) {
        this.column3 = column3;
    }

    public double getColumn4() {
        return column4;
    }

    public void setColumn4(double column4) {
        this.column4 = column4;
    }

    public Double getColumn5() {
        return column5;
    }

    public void setColumn5(Double column5) {
        this.column5 = column5;
    }

    public String getColumn6() {
        return column6;
    }

    public void setColumn6(String column6) {
        this.column6 = column6;
    }
}
