import myProject.model.DAOFiles;
import myProject.model.EntityFile;
import org.junit.Test;

import java.io.File;
import java.util.Date;

/**
 * Created by serega on 20.09.2015.
 */
public class TestDAOFiles {

    private DAOFiles daoFiles = new DAOFiles();
    private final String filePath = "";
    private final long fileSize = 101;
    private EntityFile file = new EntityFile("D:\\Downloads\\example3\\aa", new Date(), 12000L, new java.sql.Date(new Date().getTime()));

    @Test
    public void testGetById() {
        EntityFile f = daoFiles.get(1);
        System.out.println(f.getId_folder());
        System.out.println(f.getPath());
        System.out.println(f.getLast_check_time());
        System.out.println(f.getSize());
        System.out.println(f.getLast_used());
    }

    @Test
    public void testGetByPath() {
        EntityFile f = daoFiles.get(file.getPath());
        if(f != null) {
            System.out.println("!!! " + new Date(new File(f.getPath()).lastModified()));
            System.out.println(f.getId_folder());
            System.out.println(f.getPath());
            System.out.println(f.getLast_check_time());
            System.out.println(f.getSize());
            System.out.println(f.getLast_used());
        } else System.err.println("Don't found.");
    }

    @Test
    public void testAdd() {
        daoFiles.insert(file);
    }

    @Test
    public void testClean() {
        System.out.println(daoFiles.clean());
    }

    @Test
    public void testDeleteAll() throws Exception {
        daoFiles.deleteAll();
    }
}
