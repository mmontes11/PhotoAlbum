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
@Table(name = "TAG_ARCHIVO")
@SuppressWarnings("serial")
public class TagFile implements Serializable {

    private Integer id;
    private Tag tag;
    private File file;

    /**
     * Constructor for TagFile.
     */
    public TagFile() {
    }

    /**
     * Constructor for TagFile.
     * 
     * @param id
     *            Integer
     * @param tag
     *            Tag
     * @param file
     *            File
     */
    public TagFile(Integer id, Tag tag, File file) {
        this.id = id;
        this.tag = tag;
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
     * Method getTag.
     * 
     * @return Tag
     */
    @ManyToOne
    @JoinColumn(name = "TAG_ID")
    public Tag getTag() {
        return tag;
    }

    /**
     * Method setTag.
     * 
     * @param tag
     *            Tag
     */
    public void setTag(Tag tag) {
        this.tag = tag;
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
