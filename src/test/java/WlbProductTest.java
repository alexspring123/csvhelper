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
    private static final String FILE_NAME = "wlbproduct.csv";

    @Test
    public void testFormat() throws CsvReadException, IOException, CsvWriteException {
        URL url = CsvReaderFormatTest.class.getResource("");
        CsvReader reader = new CsvReader(url.getPath() + FILE_NAME, CSVWlbProduct.class);
        List<CSVWlbProduct> list = reader.getAllBeans();

        CsvWriter<CSVWlbProduct> writer = new CsvWriter<CSVWlbProduct>("f:/test.csv", CSVWlbProduct.class);
        writer.start();
        writer.append(list);
        writer.end();
    }

}
