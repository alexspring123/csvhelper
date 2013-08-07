package R;

import com.alex.fileparse.csv.CsvRW;
import com.alex.fileparse.csv.CsvReadException;
import com.alex.fileparse.csv.CsvWriteException;
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

        CsvRW rw = new CsvRW(inFile, outFile, CSVWlbProduct.class);

        long begin = System.currentTimeMillis();
        while (rw.hasMore()) {
            List<CSVWlbProduct> list = rw.getBeans(2);
            rw.writerBeans(list);
        }
        rw.end();
        System.out.println("耗时:" + (System.currentTimeMillis() - begin) + "ms。");
    }

}
