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
@Table(name = "SHARE_INFORMATION_ALBUMS")
@SuppressWarnings("serial")
public class ShareInformationAlbums implements Serializable {

    private Integer id;
    private Album album;
    private User user;

    /**
     * Constructor for ShareInformationAlbums.
     */
    public ShareInformationAlbums() {
    }

    /**
     * Constructor for ShareInformationAlbums.
     * 
     * @param id
     *            Integer
     * @param album
     *            Album
     * @param user
     *            User
     */
    public ShareInformationAlbums(Integer id, Album album, User user) {
        this.id = id;
        this.album = album;
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
        return this.id;
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
