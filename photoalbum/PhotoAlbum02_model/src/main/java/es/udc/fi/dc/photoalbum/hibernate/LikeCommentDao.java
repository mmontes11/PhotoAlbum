package es.udc.fi.dc.photoalbum.hibernate;

import java.util.List;

/**
 */
public interface LikeCommentDao extends GenericDao<LikeComment> {
    /**
     * Method getLikeCommentByComment.
     * 
     * @param commentId
     *            int
     * @return List<LikeComment>
     */
    List<LikeComment> getLikeCommentByComment(int commentId);
}
