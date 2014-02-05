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
public class LikeFileDaoImpl extends HibernateDaoSupport implements LikeFileDao {

    /**
     * Method create.
     * 
     * @param t
     *            LikeFile
     */
    @Override
    public void create(LikeFile t) {
        getHibernateTemplate().save(t);
    }

    /**
     * Method delete.
     * 
     * @param t
     *            LikeFile
     */
    @Override
    public void delete(LikeFile t) {
        getHibernateTemplate().delete(t);
    }

    /**
     * Method getLikeFileByFile.
     * 
     * @param fileId
     *            int
     * @return List<LikeFile>
     * @see es.udc.fi.dc.photoalbum.hibernate.LikeFileDao#getLikeFileByFile(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<LikeFile> getLikeFileByFile(int fileId) {
        return (ArrayList<LikeFile>) getHibernateTemplate().findByCriteria(
                DetachedCriteria.forClass(LikeFile.class)
                        .add(Restrictions.eq("file.id", fileId))
                        .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
    }

    /**
     * Method getPositiveLikesByFile.
     * 
     * @param fileId
     *            int
     * @return int
     * @see es.udc.fi.dc.photoalbum.hibernate.LikeFileDao#getPositiveLikesByFile(int)
     */
    @Override
    public int getPositiveLikesByFile(int fileId) {
        String queryString = "SELECT lf FROM LikeFile lf WHERE lf.file.id = :fileId AND lf.like.megusta = 1";
        Query query = getSession().createQuery(queryString);
        query.setParameter("fileId", fileId);
        return query.list().size();
    }
}
