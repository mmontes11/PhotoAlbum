package es.udc.fi.dc.photoalbum.hibernate;

/**
 */
public interface CommentDao extends GenericDao<Comment> {

    /**
     * Method getById.
     * 
     * @param commentId
     *            int
     * @return Comment
     */
    Comment getById(int commentId);

    /**
     * Method updateCommentText.
     * 
     * @param commentId
     *            int
     * @param newtext
     *            String
     */
    void updateCommentText(int commentId, String newtext);

}
