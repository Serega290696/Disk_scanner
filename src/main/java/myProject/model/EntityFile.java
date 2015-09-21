package myProject.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "disk_structure")
public class EntityFile implements Serializable{


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_folder")
    private long id_folder;

    @Column(name = "path")
    private String path;

    @Column(name = "last_check_time")
    private Date last_check_time;

    @Column(name = "size")
    private long size;

    @Column(name = "last_used")
    private java.sql.Date last_used;

    public EntityFile() {
    }

    public EntityFile(String path, Date last_modify, long size, java.sql.Date last_used) {
        this.path = path;
        this.last_check_time = last_modify;
        this.size = size;
        this.last_used = last_used;
    }

    public long getId_folder() {
        return id_folder;
    }

    public void setId_folder(long id_folder) {
        this.id_folder = id_folder;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getLast_check_time() {
        return last_check_time;
    }

    public void setLast_check_time(Date last_check_time) {
        this.last_check_time = last_check_time;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public java.sql.Date getLast_used() {
        return last_used;
    }

    public void setLast_used(java.sql.Date last_used) {
        this.last_used = last_used;
    }
}
