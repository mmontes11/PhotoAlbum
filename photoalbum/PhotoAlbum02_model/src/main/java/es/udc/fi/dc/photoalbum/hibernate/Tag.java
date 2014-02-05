package es.udc.fi.dc.photoalbum.hibernate;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 */
@Entity
@Table(name = "TAG")
@SuppressWarnings("serial")
public class Tag implements Serializable {

    private Integer id;
    private String name;
    private Set<TagAlbum> tagAlbumes = new HashSet<TagAlbum>();

    /**
     * Constructor for Tag.
     */
    public Tag() {
    }

    /**
     * Constructor for Tag.
     * 
     * @param id
     *            Integer
     * @param name
     *            String
     */
    public Tag(Integer id, String name) {
        this.id = id;
        this.name = name;
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
     * Method getName.
     * 
     * @return String
     */
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    /**
     * Method setName.
     * 
     * @param name
     *            String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method getTagAlbumes.
     * 
     * @return Set<TagAlbum>
     */
    @OneToMany(mappedBy = "tag", fetch = FetchType.EAGER)
    public Set<TagAlbum> getTagAlbumes() {
        return tagAlbumes;
    }

    /**
     * Method setTagAlbumes.
     * 
     * @param tagAlbumes
     *            Set<TagAlbum>
     */
    public void setTagAlbumes(Set<TagAlbum> tagAlbumes) {
        this.tagAlbumes = tagAlbumes;
    }

}
