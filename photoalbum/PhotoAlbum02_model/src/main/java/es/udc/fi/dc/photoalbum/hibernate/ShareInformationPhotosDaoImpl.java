package es.udc.fi.dc.photoalbum.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 */
public class ShareInformationPhotosDaoImpl extends HibernateDaoSupport
        implements ShareInformationPhotosDao {

    /**
     * Method create.
     * 
     * @param t
     *            ShareInformationPhotos
     */
    public void create(ShareInformationPhotos t) {
        getHibernateTemplate().save(t);
    }

    /**
     * Method delete.
     * 
     * @param t
     *            ShareInformationPhotos
     */
    public void delete(ShareInformationPhotos t) {
        getHibernateTemplate().delete(t);
    }

    /**
     * Method getShare.
     * 
     * @param photoId
     *            int
     * @param userSharedToId
     *            int
     * @param userSharedEmail
     *            String
     * @return ShareInformationPhotos
     * @see es.udc.fi.dc.photoalbum.hibernate.ShareInformationPhotosDao#getShare(int,
     *      int, String)
     */
    public ShareInformationPhotos getShare(int photoId, int userSharedToId,
            String userSharedEmail) {
        @SuppressWarnings("unchecked")
        ArrayList<ShareInformationPhotos> list = (ArrayList<ShareInformationPhotos>) getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(ShareInformationPhotos.class)
                                .createAlias("file", "fi")
                                .createAlias("fi.album", "fial")
                                .createAlias("fial.user", "fialus")
                                .add(Restrictions.eq("fi.id", photoId))
                                .add(Restrictions.eq("user.id", userSharedToId))
                                .add(Restrictions.eq("fialus.email",
                                        userSharedEmail))
                                .setResultTransformer(
                                        Criteria.DISTINCT_ROOT_ENTITY));
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * Method getPhotosShares.
     * 
     * @param photoId
     *            int
     * @return List<ShareInformationPhotos>
     * @see es.udc.fi.dc.photoalbum.hibernate.ShareInformationPhotosDao#getPhotosShares(int)
     */
    @SuppressWarnings("unchecked")
    public List<ShareInformationPhotos> getPhotosShares(int photoId) {
        return (ArrayList<ShareInformationPhotos>) getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(ShareInformationPhotos.class)
                                .add(Restrictions.eq("file.id", photoId))
                                .setResultTransformer(
                                        Criteria.DISTINCT_ROOT_ENTITY));
    }

    /**
     * Method getSharingInformation.
     * 
     * @param photoId
     *            int
     * @param userId
     *            int
     * @return ShareInformationPhotos
     * @see es.udc.fi.dc.photoalbum.hibernate.ShareInformationPhotosDao#getSharingInformation(int,
     *      int)
     */
    @SuppressWarnings("unchecked")
    public ShareInformationPhotos getSharingInformation(int photoId, int userId) {
        List<ShareInformationPhotos> list = getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(ShareInformationPhotos.class)
                                .add(Restrictions.eq("file.id", photoId))
                                .add(Restrictions.eq("user.id", userId))
                                .setResultTransformer(
                                        Criteria.DISTINCT_ROOT_ENTITY));
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * Method getPhotosShares.
     * 
     * @param albumId
     *            int
     * @param userId
     *            int
     * @return List<ShareInformationPhotos>
     * @see es.udc.fi.dc.photoalbum.hibernate.ShareInformationPhotosDao#getPhotosShares(int,
     *      int)
     */
    @SuppressWarnings("unchecked")
    public List<ShareInformationPhotos> getPhotosShares(int albumId, int userId) {
        return (ArrayList<ShareInformationPhotos>) getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(ShareInformationPhotos.class)
                                .createAlias("file", "fi")
                                .createAlias("fi.album", "fial")
                                .add(Restrictions.eq("user.id", userId))
                                .add(Restrictions.eq("fial.id", albumId))
                                .setResultTransformer(
                                        Criteria.DISTINCT_ROOT_ENTITY));
    }

    /**
     * Method getPhotosSharesTo.
     * 
     * @param userId
     *            int
     * @return List<ShareInformationPhotos>
     * @see es.udc.fi.dc.photoalbum.hibernate.ShareInformationPhotosDao#getPhotosSharesTo(int)
     */
    @SuppressWarnings("unchecked")
    public List<ShareInformationPhotos> getPhotosSharesTo(int userId) {
        return (ArrayList<ShareInformationPhotos>) getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(ShareInformationPhotos.class)
                                .add(Restrictions.eq("user.id", userId))
                                .setResultTransformer(
                                        Criteria.DISTINCT_ROOT_ENTITY));
    }

}
