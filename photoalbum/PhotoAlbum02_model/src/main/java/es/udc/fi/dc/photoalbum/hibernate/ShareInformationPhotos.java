package es.udc.fi.dc.photoalbum.hibernate;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 */
@Entity
@Table(name = "SHARE_INFORMATION_ARCHIVOS")
@SuppressWarnings("serial")
public class ShareInformationPhotos implements Serializable {

    private Integer id;
    private File file;
    private User user;

    /**
     * Constructor for ShareInformationPhotos.
     */
    public ShareInformationPhotos() {
    }

    /**
     * Constructor for ShareInformationPhotos.
     * 
     * @param id
     *            Integer
     * @param file
     *            File
     * @param user
     *            User
     */
    public ShareInformationPhotos(Integer id, File file, User user) {
        super();
        this.id = id;
        this.file = file;
        this.user = user;
    }

    /**
     * Method getId.
     * 
     * @return Integer
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    /**
     * Method setId.
     * 
     * @param id
     *            Integer
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Method getFile.
     * 
     * @return File
     */
    @ManyToOne
    @JoinColumn(name = "ARCHIVO_ID")
    public File getFile() {
        return file;
    }

    /**
     * Method setFile.
     * 
     * @param file
     *            File
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Method getUser.
     * 
     * @return User
     */
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    public User getUser() {
        return user;
    }

    /**
     * Method setUser.
     * 
     * @param user
     *            User
     */
    public void setUser(User user) {
        this.user = user;
    }

}
