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
@Table(name = "LIKE_COMMENT")
@SuppressWarnings("serial")
public class LikeComment implements Serializable {

    private Integer id;
    private Likefield like;
    private Comment comment;

    /**
     * Constructor for LikeComment.
     */
    public LikeComment() {
    }

    /**
     * Constructor for LikeComment.
     * 
     * @param id
     *            Integer
     * @param like
     *            Likefield
     * @param comment
     *            Comment
     */
    public LikeComment(Integer id, Likefield like, Comment comment) {
        this.id = id;
        this.like = like;
        this.comment = comment;
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
}
