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
@Table(name = "COMMENT_ALBUM")
@SuppressWarnings("serial")
public class CommentAlbum implements Serializable {

    private Integer id;
    private Comment comment;
    private Album album;

    /**
     * Constructor for CommentAlbum.
     */
    public CommentAlbum() {
    }

    /**
     * Constructor for CommentAlbum.
     * 
     * @param id
     *            Integer
     * @param comment
     *            Comment
     * @param album
     *            Album
     */
    public CommentAlbum(Integer id, Comment comment, Album album) {
        this.id = id;
        this.comment = comment;
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
     * Method getComment.
     * 
     * @return Comment
     */
    @ManyToOne
    @JoinColumn(name = "COMMENT_ID")
    public Comment getComment() {
        return comment;
    }

    /**
     * Method setComment.
     * 
     * @param comment
     *            Comment
     */
    public void setComment(Comment comment) {
        this.comment = comment;
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
