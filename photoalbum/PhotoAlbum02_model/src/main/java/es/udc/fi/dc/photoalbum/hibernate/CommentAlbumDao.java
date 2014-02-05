package es.udc.fi.dc.photoalbum.hibernate;

import java.util.List;

/**
 */
public interface CommentAlbumDao extends GenericDao<CommentAlbum> {

    /**
     * Method getById.
     * 
     * @param commentAlbumId
     *            int
     * @return CommentAlbum
     */
    CommentAlbum getById(int commentAlbumId);

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

}
