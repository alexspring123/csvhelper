import java.io.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-5-26
 * Time: 下午7:35
 * To change this template use File | Settings | File Templates.
 */
public class GetCSVRowCountTest {
    private static final String FILE_NAME = "f:/proreceipt1.csv";

    public static void main(String[] args) throws IOException {
        GetCSVRowCountTest test = new GetCSVRowCountTest();
        test.getFile();

    }

    public String getFile() throws IOException {
        File file = new File(FILE_NAME);
        long begin = System.currentTimeMillis();
        FileReader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);

        int count = 0;
        while (br.readLine() != null) {
            count++;
        }
        System.out.println("共" + count + "行，耗时：" + (System.currentTimeMillis() - begin) + "ms。");

        return "";
    }
}
