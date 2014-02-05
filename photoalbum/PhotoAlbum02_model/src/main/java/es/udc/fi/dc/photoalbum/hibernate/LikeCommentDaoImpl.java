package es.udc.fi.dc.photoalbum.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 */
public class LikeCommentDaoImpl extends HibernateDaoSupport implements
        LikeCommentDao {

    /**
     * Method create.
     * 
     * @param t
     *            LikeComment
     */
    @Override
    public void create(LikeComment t) {
        getHibernateTemplate().save(t);
    }

    /**
     * Method delete.
     * 
     * @param t
     *            LikeComment
     */
    @Override
    public void delete(LikeComment t) {
        getHibernateTemplate().delete(t);
    }

    /**
     * Method getLikeCommentByComment.
     * 
     * @param commentId
     *            int
     * @return List<LikeComment>
     * @see es.udc.fi.dc.photoalbum.hibernate.LikeCommentDao#getLikeCommentByComment(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<LikeComment> getLikeCommentByComment(int commentId) {
        return (ArrayList<LikeComment>) getHibernateTemplate().findByCriteria(
                DetachedCriteria.forClass(LikeComment.class)
                        .add(Restrictions.eq("comment.id", commentId))
                        .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
    }
}
