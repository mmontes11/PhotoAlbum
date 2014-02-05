package es.udc.fi.dc.photoalbum.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 */
public class ShareInformationAlbumsDaoImpl extends HibernateDaoSupport
        implements ShareInformationAlbumsDao {

    /**
     * Method create.
     * 
     * @param shareInformation
     *            ShareInformationAlbums
     */
    public void create(ShareInformationAlbums shareInformation) {
        getHibernateTemplate().save(shareInformation);
    }

    /**
     * Method delete.
     * 
     * @param shareInformation
     *            ShareInformationAlbums
     */
    public void delete(ShareInformationAlbums shareInformation) {
        getHibernateTemplate().delete(shareInformation);
    }

    /**
     * Method getShares.
     * 
     * @param userShared
     *            User
     * @param userSharedTo
     *            User
     * @return List<ShareInformationAlbums>
     * @see es.udc.fi.dc.photoalbum.hibernate.ShareInformationAlbumsDao#getShares(User,
     *      User)
     */
    @SuppressWarnings("unchecked")
    public List<ShareInformationAlbums> getShares(User userShared,
            User userSharedTo) {
        return getHibernateTemplate().findByCriteria(
                DetachedCriteria.forClass(ShareInformationAlbums.class)
                        .createAlias("album", "al")
                        .createAlias("al.user", "alus")
                        .createAlias("user", "us")
                        .add(Restrictions.eq("alus.id", userShared.getId()))
                        .add(Restrictions.eq("us.id", userSharedTo.getId()))
                        .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
    }

    /**
     * Method getShare.
     * 
     * @param albumId
     *            int
     * @param userSharedToId
     *            int
     * @param userSharedEmail
     *            String
     * @return ShareInformationAlbums
     * @see es.udc.fi.dc.photoalbum.hibernate.ShareInformationAlbumsDao#getShare(int,
     *      int, String)
     */
    @SuppressWarnings("unchecked")
    public ShareInformationAlbums getShare(int albumId, int userSharedToId,
            String userSharedEmail) {
        ArrayList<ShareInformationAlbums> list = (ArrayList<ShareInformationAlbums>) getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(ShareInformationAlbums.class)
                                .createAlias("album", "al")
                                .createAlias("al.user", "alus")
                                .createAlias("user", "us")
                                .add(Restrictions.eq("al.id", albumId))
                                .add(Restrictions.eq("us.id", userSharedToId))
                                .add(Restrictions.eq("alus.email",
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
     * Method getAlbumShares.
     * 
     * @param albumId
     *            int
     * @return ArrayList<ShareInformationAlbums>
     * @see es.udc.fi.dc.photoalbum.hibernate.ShareInformationAlbumsDao#getAlbumShares(int)
     */
    @SuppressWarnings("unchecked")
    public ArrayList<ShareInformationAlbums> getAlbumShares(int albumId) {
        return (ArrayList<ShareInformationAlbums>) getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(ShareInformationAlbums.class)
                                .createCriteria("album")
                                .add(Restrictions.eq("id", albumId))
                                .setResultTransformer(
                                        Criteria.DISTINCT_ROOT_ENTITY));
    }

    /**
     * Method getUserShares.
     * 
     * @param userId
     *            int
     * @return ArrayList<ShareInformationAlbums>
     * @see es.udc.fi.dc.photoalbum.hibernate.ShareInformationAlbumsDao#getUserShares(int)
     */
    @SuppressWarnings("unchecked")
    public ArrayList<ShareInformationAlbums> getUserShares(int userId) {
        return (ArrayList<ShareInformationAlbums>) getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(ShareInformationAlbums.class)
                                .createCriteria("user")
                                .add(Restrictions.eq("id", userId))
                                .setResultTransformer(
                                        Criteria.DISTINCT_ROOT_ENTITY));
    }

    /**
     * Method getShare.
     * 
     * @param albumId
     *            int
     * @param userSharedToId
     *            int
     * @return ShareInformationAlbums
     * @see es.udc.fi.dc.photoalbum.hibernate.ShareInformationAlbumsDao#getShare(int,
     *      int)
     */
    @SuppressWarnings("unchecked")
    public ShareInformationAlbums getShare(int albumId, int userSharedToId) {
        ArrayList<ShareInformationAlbums> list = (ArrayList<ShareInformationAlbums>) getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(ShareInformationAlbums.class)
                                .createAlias("album", "al")
                                .createAlias("user", "us")
                                .add(Restrictions.eq("al.id", albumId))
                                .add(Restrictions.eq("us.id", userSharedToId))
                                .setResultTransformer(
                                        Criteria.DISTINCT_ROOT_ENTITY));
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
