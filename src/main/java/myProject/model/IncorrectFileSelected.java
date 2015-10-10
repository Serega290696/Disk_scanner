package myProject.model;

/**
 * Created by serega.
 */
public class IncorrectFileSelected extends Exception {

    public IncorrectFileSelected() {
        System.err.println("Wrong file!");
    }
    public IncorrectFileSelected(String message) {
        super(message);
    }
}
