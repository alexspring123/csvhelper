package RW;

import com.alex.fileparse.csv.CsvReadException;
import com.alex.fileparse.csv.CsvReader;
import com.alex.fileparse.csv.CsvWriteException;
import com.alex.fileparse.csv.CsvWriter;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-5-28
 * Time: 下午9:15
 * To change this template use File | Settings | File Templates.
 */
public class WlbProductTest {
    @Test
    public void test() throws CsvReadException, IOException, CsvWriteException {
        URL url = this.getClass().getResource("/wlbproduct.csv");
        String inFile = url.getPath();
        String outFile = "f:/test.csv";

        long begin = System.currentTimeMillis();
        CsvReader reader = new CsvReader(inFile, CSVWlbProduct.class);
        CsvWriter writer = new CsvWriter(outFile, CSVWlbProduct.class);
        writer.start();
        while (reader.hasMore()) {
            List<CSVWlbProduct> list = reader.getBeans(10);
            writer.append(list);
        }
        writer.end();
        System.out.println("耗时:" + (System.currentTimeMillis() - begin) + "ms。");
    }

}
