package es.udc.fi.dc.photoalbum.hibernate;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 */
public class LikeDaoImpl extends HibernateDaoSupport implements LikeDao {

    /**
     * Method create.
     * 
     * @param t
     *            Likefield
     */
    @Override
    public void create(Likefield t) {
        getHibernateTemplate().save(t);
    }

    /**
     * Method delete.
     * 
     * @param t
     *            Likefield
     */
    @Override
    public void delete(Likefield t) {
        getHibernateTemplate().save(t);
    }

    /**
     * Method getById.
     * 
     * @param likeId
     *            int
     * @return Likefield
     * @see es.udc.fi.dc.photoalbum.hibernate.LikeDao#getById(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Likefield getById(int likeId) {
        ArrayList<Likefield> list = (ArrayList<Likefield>) getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(Likefield.class)
                                .add(Restrictions.eq("id", likeId))
                                .setResultTransformer(
                                        Criteria.DISTINCT_ROOT_ENTITY));
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * Method updateLikeDislike.
     * 
     * @param likeId
     *            int
     * @param megusta
     *            int
     * @see es.udc.fi.dc.photoalbum.hibernate.LikeDao#updateLikeDislike(int,
     *      int)
     */
    @Override
    public void updateLikeDislike(int likeId, int megusta) {
        Likefield like = getById(likeId);
        like.setMegusta(megusta);
        getHibernateTemplate().update(like);
    }

    /**
     * Method findLike.
     * 
     * @param megusta
     *            int
     * @param user
     *            User
     * @return Likefield
     * @see es.udc.fi.dc.photoalbum.hibernate.LikeDao#findLike(int, User)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Likefield findLike(int megusta, User user) {
        ArrayList<Likefield> list = (ArrayList<Likefield>) getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(Likefield.class)
                                .add(Restrictions.eq("megusta", megusta))
                                .add(Restrictions.eq("user.id", user.getId()))
                                .setResultTransformer(
                                        Criteria.DISTINCT_ROOT_ENTITY));
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

}
