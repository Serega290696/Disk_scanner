import myProject.model.IncorrectFileSelected;
import org.junit.Test;

/**
 * Created by serega.
 */
public class TestIncorrectFileSelected {
    @Test
    public void testName() throws Exception {
        throw new IncorrectFileSelected();
    }

    @Test
    public void testThrow() throws IncorrectFileSelected {
        throw new IncorrectFileSelected("Wrong file path!");
    }
}
