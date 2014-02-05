package es.udc.fi.dc.photoalbum.spring;

import java.util.Collections;
import java.util.List;

import es.udc.fi.dc.photoalbum.hibernate.Comment;
import es.udc.fi.dc.photoalbum.hibernate.CommentAlbum;
import es.udc.fi.dc.photoalbum.hibernate.CommentAlbumDao;
import es.udc.fi.dc.photoalbum.hibernate.CommentDao;
import es.udc.fi.dc.photoalbum.hibernate.CommentFile;
import es.udc.fi.dc.photoalbum.hibernate.CommentFileDao;

/**
 */
public class CommentServiceImpl implements CommentService {

    private CommentDao commentDao;
    private CommentAlbumDao commentAlbumDao;
    private CommentFileDao commentFileDao;

    /**
     * Method setCommentDao.
     * 
     * @param commentDao
     *            CommentDao
     */
    public void setCommentDao(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    /**
     * Method setCommentAlbumDao.
     * 
     * @param commentAlbumDao
     *            CommentAlbumDao
     */
    public void setCommentAlbumDao(CommentAlbumDao commentAlbumDao) {
        this.commentAlbumDao = commentAlbumDao;
    }

    /**
     * Method setCommentFileDao.
     * 
     * @param commentFileDao
     *            CommentFileDao
     */
    public void setCommentFileDao(CommentFileDao commentFileDao) {
        this.commentFileDao = commentFileDao;
    }

    /**
     * Method getCommentById.
     * 
     * @param commentId
     *            int
     * @return Comment
     * @see es.udc.fi.dc.photoalbum.spring.CommentService#getCommentById(int)
     */
    @Override
    public Comment getCommentById(int commentId) {
        return commentDao.getById(commentId);
    }

    /**
     * Method updateCommentText.
     * 
     * @param commentId
     *            int
     * @param newtext
     *            String
     * @see es.udc.fi.dc.photoalbum.spring.CommentService#updateCommentText(int,
     *      String)
     */
    @Override
    public void updateCommentText(int commentId, String newtext) {
        commentDao.updateCommentText(commentId, newtext);
    }

    /**
     * Method create.
     * 
     * @param commentAlbum
     *            CommentAlbum
     * @see es.udc.fi.dc.photoalbum.spring.CommentService#create(CommentAlbum)
     */
    @Override
    public void create(CommentAlbum commentAlbum) {
        commentDao.create(commentAlbum.getComment());
        commentAlbumDao.create(commentAlbum);
    }

    /**
     * Method delete.
     * 
     * @param commentAlbum
     *            CommentAlbum
     * @see es.udc.fi.dc.photoalbum.spring.CommentService#delete(CommentAlbum)
     */
    @Override
    public void delete(CommentAlbum commentAlbum) {
        Comment comment = commentAlbum.getComment();
        commentAlbumDao.delete(commentAlbum);
        commentDao.delete(comment);
    }

    /**
     * Method geCommentAlbumById.
     * 
     * @param commentAlbumId
     *            int
     * @return CommentAlbum
     * @see es.udc.fi.dc.photoalbum.spring.CommentService#geCommentAlbumById(int)
     */
    @Override
    public CommentAlbum geCommentAlbumById(int commentAlbumId) {
        return commentAlbumDao.getById(commentAlbumId);
    }

    /**
     * Method getCommentAlbumsByAlbum.
     * 
     * @param albumId
     *            int
     * @return List<CommentAlbum>
     * @see es.udc.fi.dc.photoalbum.spring.CommentService#getCommentAlbumsByAlbum(int)
     */
    @Override
    public List<CommentAlbum> getCommentAlbumsByAlbum(int albumId) {
        List<CommentAlbum> list = commentAlbumDao
                .getCommentAlbumsByAlbum(albumId);
        Collections.reverse(list);
        return list;
    }

    /**
     * Method getCommentAlbumsByUser.
     * 
     * @param userId
     *            int
     * @return List<CommentAlbum>
     * @see es.udc.fi.dc.photoalbum.spring.CommentService#getCommentAlbumsByUser(int)
     */
    @Override
    public List<CommentAlbum> getCommentAlbumsByUser(int userId) {
        return commentAlbumDao.getCommentAlbumsByUser(userId);
    }

    /**
     * Method getCommentAlbumsByAlbumUser.
     * 
     * @param albumId
     *            int
     * @param userId
     *            int
     * @return List<CommentAlbum>
     * @see es.udc.fi.dc.photoalbum.spring.CommentService#getCommentAlbumsByAlbumUser(int,
     *      int)
     */
    @Override
    public List<CommentAlbum> getCommentAlbumsByAlbumUser(int albumId,
            int userId) {
        return commentAlbumDao.getCommentAlbumsByAlbumUser(albumId, userId);
    }

    /**
     * Method create.
     * 
     * @param commentFile
     *            CommentFile
     * @see es.udc.fi.dc.photoalbum.spring.CommentService#create(CommentFile)
     */
    @Override
    public void create(CommentFile commentFile) {
        commentDao.create(commentFile.getComment());
        commentFileDao.create(commentFile);
    }

    /**
     * Method delete.
     * 
     * @param commentFile
     *            CommentFile
     * @see es.udc.fi.dc.photoalbum.spring.CommentService#delete(CommentFile)
     */
    @Override
    public void delete(CommentFile commentFile) {
        Comment comment = commentFile.getComment();
        commentFileDao.delete(commentFile);
        commentDao.delete(comment);
    }

    /**
     * Method getCommentFileById.
     * 
     * @param commentFileId
     *            int
     * @return CommentFile
     * @see es.udc.fi.dc.photoalbum.spring.CommentService#getCommentFileById(int)
     */
    @Override
    public CommentFile getCommentFileById(int commentFileId) {
        return commentFileDao.getById(commentFileId);
    }

    /**
     * Method getCommentFilesByFile.
     * 
     * @param fileId
     *            int
     * @return List<CommentFile>
     * @see es.udc.fi.dc.photoalbum.spring.CommentService#getCommentFilesByFile(int)
     */
    @Override
    public List<CommentFile> getCommentFilesByFile(int fileId) {
        List<CommentFile> list = commentFileDao.getCommentFilesByFile(fileId);
        Collections.reverse(list);
        return list;
    }

    /**
     * Method getCommentFilesByUser.
     * 
     * @param userId
     *            int
     * @return List<CommentFile>
     * @see es.udc.fi.dc.photoalbum.spring.CommentService#getCommentFilesByUser(int)
     */
    @Override
    public List<CommentFile> getCommentFilesByUser(int userId) {
        return commentFileDao.getCommentFilesByUser(userId);
    }

    /**
     * Method getCommentFileByFileUser.
     * 
     * @param fileId
     *            int
     * @param userId
     *            int
     * @return List<CommentFile>
     * @see es.udc.fi.dc.photoalbum.spring.CommentService#getCommentFileByFileUser(int,
     *      int)
     */
    @Override
    public List<CommentFile> getCommentFileByFileUser(int fileId, int userId) {
        return commentFileDao.getCommentFileByFileUser(fileId, userId);
    }

}
