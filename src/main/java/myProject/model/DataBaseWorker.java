package myProject.model;

import myProject.model.interfaces.IDataBaseWorker;

import java.util.ArrayList;

/**
 * Created by serega on 10.09.2015.
 */
public class DataBaseWorker implements IDataBaseWorker{


    @Override
    public boolean add(ResultFile file) {
        return false;
    }

    @Override
    public void update(ResultFile file) {

    }

    @Override
    public ResultFile get(ResultFile file) {
        return null;
    }

    @Override
    public ArrayList<ResultFile> getAll() {
        return null;
    }

    @Override
    public void delete(ResultFile file) {

    }
}
