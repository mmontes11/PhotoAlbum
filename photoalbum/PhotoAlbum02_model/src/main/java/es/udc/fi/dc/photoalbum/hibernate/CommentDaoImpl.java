package es.udc.fi.dc.photoalbum.hibernate;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 */
public class CommentDaoImpl extends HibernateDaoSupport implements CommentDao {

    /**
     * Method create.
     * 
     * @param t
     *            Comment
     */
    @Override
    public void create(Comment t) {
        getHibernateTemplate().save(t);
    }

    /**
     * Method delete.
     * 
     * @param t
     *            Comment
     */
    @Override
    public void delete(Comment t) {
        getHibernateTemplate().delete(t);
    }

    /**
     * Method getById.
     * 
     * @param commentId
     *            int
     * @return Comment
     * @see es.udc.fi.dc.photoalbum.hibernate.CommentDao#getById(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Comment getById(int commentId) {
        ArrayList<Comment> list = (ArrayList<Comment>) getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(Comment.class)
                                .add(Restrictions.eq("id", commentId))
                                .setResultTransformer(
                                        Criteria.DISTINCT_ROOT_ENTITY));
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * Method updateCommentText.
     * 
     * @param commentId
     *            int
     * @param newtext
     *            String
     * @see es.udc.fi.dc.photoalbum.hibernate.CommentDao#updateCommentText(int,
     *      String)
     */
    @Override
    public void updateCommentText(int commentId, String newtext) {
        Comment comment = getById(commentId);
        comment.setCommentText(newtext);
        getHibernateTemplate().update(comment);
    }

}
