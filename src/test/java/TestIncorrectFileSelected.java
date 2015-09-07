import myProject.model.IncorrectFileSelected;
import org.junit.Test;

/**
 * Created by serega on 07.09.2015.
 */
public class TestIncorrectFileSelected {
    @Test
    public void testName() throws Exception {

    }

    @Test
    public void testThrow() throws IncorrectFileSelected {
        throw new IncorrectFileSelected("Wrong file path!");
    }
}
