package myProject.model;

/**
 * Created by serega on 07.09.2015.
 */
public class IncorrectFileSelected extends Exception {

    public IncorrectFileSelected() {
        System.err.println("Wrong file!");
    }
    public IncorrectFileSelected(String message) {
        super(message);
    }
}
