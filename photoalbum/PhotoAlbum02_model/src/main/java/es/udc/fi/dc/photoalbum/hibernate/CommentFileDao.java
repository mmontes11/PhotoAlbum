package es.udc.fi.dc.photoalbum.hibernate;

import java.util.List;

/**
 */
public interface CommentFileDao extends GenericDao<CommentFile> {

    /**
     * Method getById.
     * 
     * @param commentFileId
     *            int
     * @return CommentFile
     */
    CommentFile getById(int commentFileId);

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
