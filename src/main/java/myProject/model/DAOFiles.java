package myProject.model;

import myProject.model.interfaces.DAOInterface;
import org.hibernate.Session;

import java.io.File;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Created by serega on 20.09.2015.
 */
public class DAOFiles implements DAOInterface<EntityFile> {
    private static final int DAYS_BEFORE_DELETE = 30;
    private Session session = HibernateUtil.getSessionFactory().openSession();

    @Override
    public EntityFile get(long id) {
        return (EntityFile) session.get(EntityFile.class, id);
    }

    @Override
    public EntityFile get(String path) {
//        if(session.isDirty())
//            list = getAll();
        for (EntityFile f : getAll()) {
            if (f.getPath().equals(path)) {
                System.out.println("*");
                System.out.println(f.getLast_check_time());
                System.out.println(new Date(new File(path).lastModified()));
                System.out.println(f.getLast_check_time().after(new Date(new File(path).lastModified())));
                if (f.getLast_check_time().after(new Date(new File(path).lastModified())))
                    return f;
                else {
                    delete(f);
                    return null;
                }
            }
        }
        return null;
//        Query q = session.createQuery("from EntityFile as u where u.path=" + obj.getPath());
//        return (EntityFile)q.uniqueResult();
    }

    private void delete(EntityFile f) {
        session.beginTransaction();
        session.delete(f);
        session.getTransaction().commit();
    }

    @Override
    public void insert(EntityFile obj) {
//        if(get(obj.getPath()))
        session.beginTransaction();
        session.merge(obj);
        session.getTransaction().commit();
    }

    @Override
    public void update(EntityFile obj) {
        session.beginTransaction();
        long id = get(obj.getPath()).getId_folder();
        obj.setId_folder(id);
        session.merge(obj);
        session.getTransaction().commit();
    }

    @Override
    public List<EntityFile> getAll() {
        List<EntityFile> result = session.createQuery("from EntityFile order by id").list();
        return result;
    }

    @Override
    public long clean() {
        LocalDate date = LocalDate.now().minusDays(DAYS_BEFORE_DELETE);
        int count = 0;

        session.beginTransaction();
        for (EntityFile f : getAll()) {
            if (f.getLast_used().toLocalDate().isBefore(date)) {
                session.delete(f);
                count++;
            }
        }
        session.getTransaction().commit();
        return count;
    }

    @Override
    public long deleteAll() {
        session.beginTransaction();
        getAll().forEach(session::delete);
        session.getTransaction().commit();
        return 0;
    }

    public boolean isSessionDirty() {
        return session.isDirty();
    }
}
