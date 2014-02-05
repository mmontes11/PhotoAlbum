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
@Table(name = "LIKE_ALBUM")
@SuppressWarnings("serial")
public class LikeAlbum implements Serializable {

    private Integer id;
    private Likefield like;
    private Album album;

    /**
     * Constructor for LikeAlbum.
     */
    public LikeAlbum() {
    }

    /**
     * Constructor for LikeAlbum.
     * 
     * @param id
     *            Integer
     * @param like
     *            Likefield
     * @param album
     *            Album
     */
    public LikeAlbum(Integer id, Likefield like, Album album) {
        this.id = id;
        this.like = like;
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
     * Method getLike.
     * 
     * @return Likefield
     */
    @ManyToOne
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