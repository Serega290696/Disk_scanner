import myProject.model.AddToStartUp;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by serega.
 */
public class TestStartUpClass {

    @Test
    public void testAdd() {
        try {
            AddToStartUp.add();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
