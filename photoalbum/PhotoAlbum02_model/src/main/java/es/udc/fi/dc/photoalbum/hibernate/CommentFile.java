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
@Table(name = "COMMENT_ARCHIVO")
@SuppressWarnings("serial")
public class CommentFile implements Serializable {

    private Integer id;
    private Comment comment;
    private File file;

    /**
     * Constructor for CommentFile.
     */
    public CommentFile() {
    }

    /**
     * Constructor for CommentFile.
     * 
     * @param id
     *            Integer
     * @param comment
     *            Comment
     * @param file
     *            File
     */
    public CommentFile(Integer id, Comment comment, File file) {
        this.id = id;
        this.comment = comment;
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