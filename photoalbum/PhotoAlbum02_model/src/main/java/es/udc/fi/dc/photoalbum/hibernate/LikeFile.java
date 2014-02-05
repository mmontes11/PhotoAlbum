package es.udc.fi.dc.photoalbum.hibernate;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 */
@Entity
@Table(name = "LIKE_ARCHIVO")
@SuppressWarnings("serial")
public class LikeFile implements Serializable {

    private Integer id;
    private Likefield like;
    private File file;

    /**
     * Constructor for LikeFile.
     */
    public LikeFile() {
    }

    /**
     * Constructor for LikeFile.
     * 
     * @param id
     *            Integer
     * @param likefield
     *            Likefield
     * @param file
     *            File
     */
    public LikeFile(Integer id, Likefield likefield, File file) {
        this.id = id;
        this.like = likefield;
        this.file = file;
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
     * Method getLike.
     * 
     * @return Likefield
     */
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "LIKE_ID")
    public Likefield getLike() {
        return like;
    }

    /**
     * Method setLike.
     * 
     * @param like
     *            Likefield
     */
    public void setLike(Likefield like) {
        this.like = like;
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
}