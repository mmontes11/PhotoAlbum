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
@Table(name = "TAG_ALBUM")
@SuppressWarnings("serial")
public class TagAlbum implements Serializable {

    private Integer id;
    private Tag tag;
    private Album album;

    /**
     * Constructor for TagAlbum.
     */
    public TagAlbum() {
    }

    /**
     * Constructor for TagAlbum.
     * 
     * @param id
     *            Integer
     * @param tag
     *            Tag
     * @param album
     *            Album
     */
    public TagAlbum(Integer id, Tag tag, Album album) {
        this.id = id;
        this.tag = tag;
        this.album = album;
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
     * Method getAlbum.
     * 
     * @return Album
     */
    @ManyToOne
    @JoinColumn(name = "ALBUM_ID")
    public Album getAlbum() {
        return album;
    }

    /**
     * Method setAlbum.
     * 
     * @param album
     *            Album
     */
    public void setAlbum(Album album) {
        this.album = album;
    }

}
