package myProject.model.interfaces;

import myProject.model.EntityFile;

import java.util.List;

/**
 * Created by serega on 20.09.2015.
 */
public interface DAOInterface<T> {

    public T get(long id);
    public T get(String path);
    public List<EntityFile> getAll();
    public void insert(T obj);
    public void update(T obj);

    public long clean();
    public long deleteAll();

}
