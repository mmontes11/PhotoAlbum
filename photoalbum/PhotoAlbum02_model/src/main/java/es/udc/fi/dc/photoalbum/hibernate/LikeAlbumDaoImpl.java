package es.udc.fi.dc.photoalbum.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 */
public class LikeAlbumDaoImpl extends HibernateDaoSupport implements
        LikeAlbumDao {

    /**
     * Method create.
     * 
     * @param t
     *            LikeAlbum
     */
    @Override
    public void create(LikeAlbum t) {
        getHibernateTemplate().save(t);
    }

    /**
     * Method delete.
     * 
     * @param t
     *            LikeAlbum
     */
    @Override
    public void delete(LikeAlbum t) {
        getHibernateTemplate().delete(t);
    }

    /**
     * Method getLikeAlbumsByAlbum.
     * 
     * @param albumId
     *            int
     * @return List<LikeAlbum>
     * @see es.udc.fi.dc.photoalbum.hibernate.LikeAlbumDao#getLikeAlbumsByAlbum(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<LikeAlbum> getLikeAlbumsByAlbum(int albumId) {
        return (ArrayList<LikeAlbum>) getHibernateTemplate().findByCriteria(
                DetachedCriteria.forClass(LikeAlbum.class)
                        .add(Restrictions.eq("album.id", albumId))
                        .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
    }

    /**
     * Method getPositiveLikesByAlbum.
     * 
     * @param albumId
     *            int
     * @return int
     * @see es.udc.fi.dc.photoalbum.hibernate.LikeAlbumDao#getPositiveLikesByAlbum(int)
     */
    @Override
    public int getPositiveLikesByAlbum(int albumId) {
        String queryString = "SELECT la FROM LikeAlbum la WHERE la.album.id = :albumId AND la.like.megusta = 1";
        Query query = getSession().createQuery(queryString);
        query.setParameter("albumId", albumId);
        return query.list().size();
    }

}
