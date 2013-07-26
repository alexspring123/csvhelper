import com.alex.fileparse.csv.CsvReadException;
import com.alex.fileparse.csv.CsvReader;
import com.hd123.workload.jira.TestObject;
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
public class CsvReaderFormatTest {
    private static final String FILE_NAME = "formatTest.csv";

    @Test
    public void testFormat() throws CsvReadException, IOException {
        URL url = CsvReaderFormatTest.class.getResource("");
        CsvReader reader = new CsvReader(url.getPath() + FILE_NAME, TestObject.class);
        List<TestObject> list = reader.getBeans(0);
    }

}
