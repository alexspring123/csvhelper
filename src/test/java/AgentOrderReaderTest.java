import com.alex.fileparse.csv.CsvReadException;
import com.alex.fileparse.csv.CsvReader;
import com.alex.fileparse.csv.CsvWriteException;
import com.alex.fileparse.csv.CsvWriter;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-7-23
 * Time: 下午12:51
 * To change this template use File | Settings | File Templates.
 */
public class AgentOrderReaderTest {
    private static final String FILE_NAME = "agentorder_直营.csv";

    @Test
    public void testFormat() throws CsvReadException, IOException, CsvWriteException {
        URL url = CsvReaderFormatTest.class.getResource("");

        CsvReader reader = new CsvReader("f:/agentorder_直营.csv", CSVAgentOrder.class);
        CsvWriter writer = new CsvWriter("f:/111.csv", CSVAgentOrder.class);
        writer.start();

        List<CSVAgentOrder> list = reader.getBeans(50);
        writer.append(list);
        while (reader.hasMore()) {
            list = reader.getBeans(50);
            writer.append(list);
        }
        writer.end();
    }
}
