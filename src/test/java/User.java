import com.alex.fileparse.csv.CsvBean;
import com.alex.fileparse.csv.annotation.Column;
import com.alex.fileparse.csv.annotation.Max;
import com.alex.fileparse.csv.annotation.Min;
import com.alex.fileparse.csv.annotation.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-5-28
 * Time: 下午9:16
 * To change this template use File | Settings | File Templates.
 */
public class User extends CsvBean {
    @Column(name = "代码", length = 10)
    private String code;
    @Column(name = "名称", length = 20)
    private String name;
    @Column(name = "年龄")
    private int age;

    @Column(name = "charTest")
    private char charTest;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("代码=").append(code);
        sb.append(",  名称=").append(name);
        sb.append(",  年龄=").append(age);
        sb.append(",  charTest=").append(charTest);
        return sb.toString();
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public char getCharTest() {
        return charTest;
    }

    public void setCharTest(char charTest) {
        this.charTest = charTest;
    }
}
