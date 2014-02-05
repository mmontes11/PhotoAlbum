package es.udc.fi.dc.photoalbum.spring;

import java.util.List;

import es.udc.fi.dc.photoalbum.hibernate.Comment;
import es.udc.fi.dc.photoalbum.hibernate.CommentAlbum;
import es.udc.fi.dc.photoalbum.hibernate.CommentFile;

/**
 */
public interface CommentService {

    /**
     * Method getCommentById.
     * 
     * @param commentId
     *            int
     * @return Comment
     */
    Comment getCommentById(int commentId);

    /**
     * Method updateCommentText.
     * 
     * @param commentId
     *            int
     * @param newtext
     *            String
     */
    void updateCommentText(int commentId, String newtext);

    /**
     * Method create.
     * 
     * @param commentAlbum
     *            CommentAlbum
     */
    void create(CommentAlbum commentAlbum);

    /**
     * Method delete.
     * 
     * @param commentAlbum
     *            CommentAlbum
     */
    void delete(CommentAlbum commentAlbum);

    /**
     * Method geCommentAlbumById.
     * 
     * @param commentAlbumId
     *            int
     * @return CommentAlbum
     */
    CommentAlbum geCommentAlbumById(int commentAlbumId);

    /**
     * Method getCommentAlbumsByAlbum.
     * 
     * @param albumId
     *            int
     * @return List<CommentAlbum>
     */
    List<CommentAlbum> getCommentAlbumsByAlbum(int albumId);

    /**
     * Method getCommentAlbumsByUser.
     * 
     * @param userId
     *            int
     * @return List<CommentAlbum>
     */
    List<CommentAlbum> getCommentAlbumsByUser(int userId);

    /**
     * Method getCommentAlbumsByAlbumUser.
     * 
     * @param albumId
     *            int
     * @param userId
     *            int
     * @return List<CommentAlbum>
     */
    List<CommentAlbum> getCommentAlbumsByAlbumUser(int albumId, int userId);

    /**
     * Method create.
     * 
     * @param commentFile
     *            CommentFile
     */
    void create(CommentFile commentFile);

    /**
     * Method delete.
     * 
     * @param commentFile
     *            CommentFile
     */
    void delete(CommentFile commentFile);

    /**
     * Method getCommentFileById.
     * 
     * @param commentFileId
     *            int
     * @return CommentFile
     */
    CommentFile getCommentFileById(int commentFileId);

    /**
     * Method getCommentFilesByFile.
     * 
     * @param fileId
     *            int
     * @return List<CommentFile>
     */
    List<CommentFile> getCommentFilesByFile(int fileId);

    /**
     * Method getCommentFilesByUser.
     * 
     * @param userId
     *            int
     * @return List<CommentFile>
     */
    List<CommentFile> getCommentFilesByUser(int userId);

    /**
     * Method getCommentFileByFileUser.
     * 
     * @param fileId
     *            int
     * @param userId
     *            int
     * @return List<CommentFile>
     */
    List<CommentFile> getCommentFileByFileUser(int fileId, int userId);

}
