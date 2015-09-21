import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * Created by serega on 20.09.2015.
 */
public class TestLogger {

    private static final Logger log = Logger.getLogger("myProject.model.DiskAnalyzer");

    @Test
    public void testTraceEnable() throws Exception {
        log.setLevel(Level.TRACE);
        if(log.isTraceEnabled()) {
            log.trace("Trace is enabled!");
        }
    }
    @Test
    public void testLog() throws Exception {
        log.info("test string");
    }
    @Test
    public void testLogTooMuchInfo() throws Exception {
        log.info("FIRST");
        for (int i = 0; i < 3000; i++) {
            log.info("123456789 123456789 123456789 123456789 123456789");
        }
    }
}
