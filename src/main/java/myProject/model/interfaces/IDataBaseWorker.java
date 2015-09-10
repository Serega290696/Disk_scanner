package myProject.model.interfaces;

import myProject.model.ResultFile;

import java.util.ArrayList;

/**
 * Created by serega on 10.09.2015.
 */
public interface IDataBaseWorker {
    public boolean add(ResultFile file);
    public void update(ResultFile file);
    public ResultFile get(ResultFile file);
    public ArrayList<ResultFile> getAll();
    public void delete(ResultFile file);
}
