package es.udc.fi.dc.photoalbum.spring;

import java.util.List;

import es.udc.fi.dc.photoalbum.hibernate.LikeAlbum;
import es.udc.fi.dc.photoalbum.hibernate.LikeComment;
import es.udc.fi.dc.photoalbum.hibernate.LikeFile;

/**
 */
public interface LikeService {

    // Like
    /**
     * Method updateLikeDislike.
     * 
     * @param likeId
     *            int
     * @param megusta
     *            int
     */
    void updateLikeDislike(int likeId, int megusta);

    // Album
    /**
     * Method create.
     * 
     * @param like
     *            LikeAlbum
     */
    void create(LikeAlbum like);

    /**
     * Method delete.
     * 
     * @param like
     *            LikeAlbum
     */
    void delete(LikeAlbum like);

    /**
     * Method getLikeAlbumsByAlbumId.
     * 
     * @param albumId
     *            int
     * @return List<LikeAlbum>
     */
    List<LikeAlbum> getLikeAlbumsByAlbumId(int albumId);

    /**
     * Method getLikesAlbum.
     * 
     * @param albumId
     *            int
     * @param megusta
     *            int
     * @return List<LikeAlbum>
     */
    List<LikeAlbum> getLikesAlbum(int albumId, int megusta);

    /**
     * Method getLikeAlbumByUserAlbum.
     * 
     * @param userId
     *            int
     * @param albumId
     *            int
     * @return LikeAlbum
     */
    LikeAlbum getLikeAlbumByUserAlbum(int userId, int albumId);

    // Comment
    /**
     * Method create.
     * 
     * @param like
     *            LikeComment
     */
    void create(LikeComment like);

    /**
     * Method delete.
     * 
     * @param like
     *            LikeComment
     */
    void delete(LikeComment like);

    /**
     * Method getLikeCommentByCommentId.
     * 
     * @param commentId
     *            int
     * @return List<LikeComment>
     */
    List<LikeComment> getLikeCommentByCommentId(int commentId);

    /**
     * Method getLikesComment.
     * 
     * @param commentId
     *            int
     * @param megusta
     *            int
     * @return List<LikeComment>
     */
    List<LikeComment> getLikesComment(int commentId, int megusta);

    /**
     * Method getLikeCommentByUserComment.
     * 
     * @param userId
     *            int
     * @param commentId
     *            int
     * @return LikeComment
     */
    LikeComment getLikeCommentByUserComment(int userId, int commentId);

    // File
    /**
     * Method create.
     * 
     * @param like
     *            LikeFile
     */
    void create(LikeFile like);

    /**
     * Method delete.
     * 
     * @param like
     *            LikeFile
     */
    void delete(LikeFile like);

    /**
     * Method getLikeFileByFileId.
     * 
     * @param fileId
     *            int
     * @return List<LikeFile>
     */
    List<LikeFile> getLikeFileByFileId(int fileId);

    /**
     * Method getLikesFile.
     * 
     * @param fileId
     *            int
     * @param megusta
     *            int
     * @return List<LikeFile>
     */
    List<LikeFile> getLikesFile(int fileId, int megusta);

    /**
     * Method getLikeFileByUserFile.
     * 
     * @param userId
     *            int
     * @param fileId
     *            int
     * @return LikeFile
     */
    LikeFile getLikeFileByUserFile(int userId, int fileId);

}
