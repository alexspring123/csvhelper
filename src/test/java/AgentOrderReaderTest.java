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
        CsvReader reader = new CsvReader(url.getPath() + FILE_NAME, CSVAgentOrder.class);
        List<CSVAgentOrder> list = new ArrayList<CSVAgentOrder>();
        list.addAll(reader.getBeans(50));
        while (reader.hasMore()) {
            list.addAll(reader.getBeans(50));
        }

        CsvWriter writer = new CsvWriter("f:/111.csv", CSVAgentOrder.class);
        writer.start();
        writer.append(list);
        writer.end();
    }
}
