package es.udc.fi.dc.photoalbum.hibernate;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 */
@Entity
@Table(name = "COMMENT")
@SuppressWarnings("serial")
public class Comment implements Serializable {

    private Integer id;
    private String commentText;
    private User user;
    private Calendar commentDate;

    /**
     * Constructor for Comment.
     */
    public Comment() {
    }

    /**
     * Constructor for Comment.
     * 
     * @param id
     *            Integer
     * @param commentText
     *            String
     * @param user
     *            User
     */
    public Comment(Integer id, String commentText, User user) {
        this.id = id;
        this.commentText = commentText;
        this.user = user;
        this.commentDate = Calendar.getInstance();
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
     * Method getCommentText.
     * 
     * @return String
     */
    @Column(name = "COMMENT_TEXT")
    public String getCommentText() {
        return commentText;
    }

    /**
     * Method setCommentText.
     * 
     * @param commentText
     *            String
     */
    public void setCommentText(String commentText) {
        this.commentText = commentText;
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

    /**
     * Method getCommentDate.
     * 
     * @return Calendar
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "COMMENT_DATE")
    public Calendar getCommentDate() {
        return commentDate;
    }

    /**
     * Method setCommentDate.
     * 
     * @param commentDate
     *            Calendar
     */
    public void setCommentDate(Calendar commentDate) {
        this.commentDate = commentDate;
    }

}