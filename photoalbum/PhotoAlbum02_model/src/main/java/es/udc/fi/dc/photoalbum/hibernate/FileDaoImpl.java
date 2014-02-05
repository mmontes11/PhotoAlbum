package es.udc.fi.dc.photoalbum.hibernate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import es.udc.fi.dc.photoalbum.utils.Constant;

/**
 */
public class FileDaoImpl extends HibernateDaoSupport implements FileDao {

    /**
     * Method create.
     * 
     * @param file
     *            File
     */
    public void create(File file) {
        getHibernateTemplate().save(file);
    }

    /**
     * Method getById.
     * 
     * @param id
     *            Integer
     * @return File
     * @see es.udc.fi.dc.photoalbum.hibernate.FileDao#getById(Integer)
     */
    public File getById(Integer id) {
        return getHibernateTemplate().get(File.class, id);
    }

    /**
     * Method getFileOwn.
     * 
     * @param id
     *            int
     * @param name
     *            String
     * @param userId
     *            int
     * @return File
     * @see es.udc.fi.dc.photoalbum.hibernate.FileDao#getFileOwn(int, String,
     *      int)
     */
    public File getFileOwn(int id, String name, int userId) {
        @SuppressWarnings("unchecked")
        ArrayList<File> list = (ArrayList<File>) getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(File.class)
                                .add(Restrictions.eq("id", id))
                                .createCriteria("album")
                                .add(Restrictions.eq("name", name))
                                .setResultTransformer(
                                        Criteria.DISTINCT_ROOT_ENTITY));
        if ((list.size() == 1)
                && ((list.get(0)).getAlbum().getUser().getId() == userId)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * Method getFileShared.
     * 
     * @param id
     *            int
     * @param name
     *            String
     * @param userId
     *            int
     * @return File
     * @see es.udc.fi.dc.photoalbum.hibernate.FileDao#getFileShared(int, String,
     *      int)
     */
    @SuppressWarnings("unchecked")
    public File getFileShared(int id, String name, int userId) {
        File file;
        ArrayList<File> list = (ArrayList<File>) getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(File.class)
                                .add(Restrictions.eq("id", id))
                                .createCriteria("album")
                                .add(Restrictions.eq("name", name))
                                .setResultTransformer(
                                        Criteria.DISTINCT_ROOT_ENTITY));
        if (list.size() == 1) {
            file = list.get(0);
        } else {
            file = null;
        }
        if (file != null) {
            ArrayList<ShareInformationAlbums> list2 = (ArrayList<ShareInformationAlbums>) getHibernateTemplate()
                    .findByCriteria(
                            DetachedCriteria
                                    .forClass(ShareInformationAlbums.class)
                                    .createAlias("album", "al")
                                    .createAlias("user", "us")
                                    .add(Restrictions.eq("al.id", file
                                            .getAlbum().getId()))
                                    .add(Restrictions.eq("us.id", userId))
                                    .setResultTransformer(
                                            Criteria.DISTINCT_ROOT_ENTITY));
            if ((list2.isEmpty())) {
                return null;
            } else {
                return file;
            }
        } else {
            return file;
        }
    }

    /**
     * Method delete.
     * 
     * @param file
     *            File
     */
    public void delete(File file) {
        getHibernateTemplate().delete(file);
    }

    /**
     * Method changeAlbum.
     * 
     * @param file
     *            File
     * @param album
     *            Album
     * @see es.udc.fi.dc.photoalbum.hibernate.FileDao#changeAlbum(File, Album)
     */
    public void changeAlbum(File file, Album album) {
        getHibernateTemplate().update(file);
        file.setAlbum(album);
    }

    /**
     * Method getAlbumFiles.
     * 
     * @param albumId
     *            int
     * @return ArrayList<File>
     * @see es.udc.fi.dc.photoalbum.hibernate.FileDao#getAlbumFiles(int)
     */
    @SuppressWarnings("unchecked")
    public ArrayList<File> getAlbumFiles(int albumId) {
        return (ArrayList<File>) getHibernateTemplate().findByCriteria(
                DetachedCriteria.forClass(File.class).createCriteria("album")
                        .add(Restrictions.eq("id", albumId))
                        .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
    }

    /**
     * Method getAlbumFilesPaging.
     * 
     * @param albumId
     *            int
     * @param first
     *            int
     * @param count
     *            int
     * @return ArrayList<File>
     * @see es.udc.fi.dc.photoalbum.hibernate.FileDao#getAlbumFilesPaging(int,
     *      int, int)
     */
    @SuppressWarnings("unchecked")
    public ArrayList<File> getAlbumFilesPaging(int albumId, int first, int count) {
        return (ArrayList<File>) getHibernateTemplate().getSessionFactory()
                .getCurrentSession().createCriteria(File.class)
                .createCriteria("album").add(Restrictions.eq("id", albumId))
                .addOrder(Order.asc("id")).setFirstResult(first)
                .setMaxResults(count)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
    }

    /**
     * Method getCountAlbumFiles.
     * 
     * @param albumId
     *            int
     * @return Long
     * @see es.udc.fi.dc.photoalbum.hibernate.FileDao#getCountAlbumFiles(int)
     */
    @SuppressWarnings("unchecked")
    public Long getCountAlbumFiles(int albumId) {
        ArrayList<Long> list = (ArrayList<Long>) getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(File.class)
                                .createCriteria("album")
                                .add(Restrictions.eq("id", albumId))
                                .setProjection(Projections.rowCount())
                                .setResultTransformer(
                                        Criteria.DISTINCT_ROOT_ENTITY));
        return list.get(0);
    }

    /**
     * Method searchPublicFiles.
     * 
     * @param keywordName
     *            String
     * @param keywordComment
     *            String
     * @param keywordTag
     *            String
     * @param initDate
     *            Calendar
     * @param endDate
     *            Calendar
     * @return List<File>
     * @see es.udc.fi.dc.photoalbum.hibernate.FileDao#searchPublicFiles(String,
     *      String, String, Calendar, Calendar)
     */
    @SuppressWarnings("unchecked")
    public List<File> searchPublicFiles(String keywordName,
            String keywordComment, String keywordTag, Calendar initDate,
            Calendar endDate) {
        String queryString = "SELECT f FROM File f "
                + "WHERE f.id IN "
                + "(SELECT sip.file.id FROM ShareInformationPhotos sip WHERE sip.user.id = "
                + Constant.getId() + ") ";
        if (keywordName != null) {
            keywordName = keywordName.toUpperCase();
            queryString += "AND upper(f.name) LIKE :keywordName ";
        }
        if (keywordComment != null) {
            keywordComment = keywordComment.toUpperCase();
            queryString += "AND f.id IN (SELECT cf.file.id FROM CommentFile cf "
                    + "	WHERE upper(cf.comment.commentText) LIKE :keywordComment) ";
        }
        if (keywordTag != null) {
            keywordTag = keywordTag.toUpperCase();
            queryString += "AND f.id IN (SELECT tf.file.id FROM TagFile tf "
                    + "WHERE upper(tf.tag.name) LIKE :keywordTag) ";
        }
        if (initDate != null) {
            queryString += "AND f.fileDate > :initDate ";
        }
        if (endDate != null) {
            queryString += "AND f.fileDate < :endDate ";
        }

        Query query = getSession().createQuery(queryString);
        if (keywordName != null) {
            query.setParameter("keywordName", "%" + keywordName + "%");
        }
        if (keywordComment != null) {
            query.setParameter("keywordComment", "%" + keywordComment + "%");
        }
        if (keywordTag != null) {
            query.setParameter("keywordTag", "%" + keywordTag + "%");
        }
        if (initDate != null) {
            query.setParameter("initDate", initDate);
        }
        if (endDate != null) {
            query.setParameter("endDate", endDate);
        }
        return query.list();
    }
}
