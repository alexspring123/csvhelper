package com.alex.fileparse.csv;

/**
 * CSV文件标题对象
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-7-26
 * Time: 下午5:55
 * To change this template use File | Settings | File Templates.
 */
public class Title {
    private String caption;
    private int column;

    public Title(String caption, int column) {
        this.caption = caption;
        this.column = column;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Title title = (Title) o;

        if (column != title.column) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return column;
    }
}
