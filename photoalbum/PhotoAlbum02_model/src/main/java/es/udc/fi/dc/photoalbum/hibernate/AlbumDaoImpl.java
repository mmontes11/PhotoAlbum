package es.udc.fi.dc.photoalbum.hibernate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import es.udc.fi.dc.photoalbum.utils.Constant;

/**
 */
public class AlbumDaoImpl extends HibernateDaoSupport implements AlbumDao {

    /**
     * Method create.
     * 
     * @param album
     *            Album
     */
    public void create(Album album) {
        getHibernateTemplate().save(album);
    }

    /**
     * Method delete.
     * 
     * @param album
     *            Album
     */
    public void delete(Album album) {
        getHibernateTemplate().delete(album);
    }

    /**
     * Method getAlbum.
     * 
     * @param name
     *            String
     * @param userId
     *            int
     * @return Album
     * @see es.udc.fi.dc.photoalbum.hibernate.AlbumDao#getAlbum(String, int)
     */
    @SuppressWarnings("unchecked")
    public Album getAlbum(String name, int userId) {
        ArrayList<Album> list = (ArrayList<Album>) getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(Album.class)
                                .add(Restrictions.eq("name", name))
                                .createCriteria("user")
                                .add(Restrictions.eq("id", userId))
                                .setResultTransformer(
                                        Criteria.DISTINCT_ROOT_ENTITY));
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * Method rename.
     * 
     * @param album
     *            Album
     * @param newName
     *            String
     * @see es.udc.fi.dc.photoalbum.hibernate.AlbumDao#rename(Album, String)
     */
    public void rename(Album album, String newName) {
        album.setName(newName);
        getHibernateTemplate().update(album);
    }

    /**
     * Method getById.
     * 
     * @param id
     *            Integer
     * @return Album
     * @see es.udc.fi.dc.photoalbum.hibernate.AlbumDao#getById(Integer)
     */
    public Album getById(Integer id) {
        return getHibernateTemplate().get(Album.class, id);
    }

    /**
     * Method getAlbums.
     * 
     * @param id
     *            Integer
     * @return ArrayList<Album>
     * @see es.udc.fi.dc.photoalbum.hibernate.AlbumDao#getAlbums(Integer)
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Album> getAlbums(Integer id) {
        return (ArrayList<Album>) getHibernateTemplate().findByCriteria(
                DetachedCriteria.forClass(Album.class).createCriteria("user")
                        .add(Restrictions.eq("id", id))
                        .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
    }

    /**
     * Method searchPublicAlbums.
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
     * @return List<Album>
     * @see es.udc.fi.dc.photoalbum.hibernate.AlbumDao#searchPublicAlbums(String,
     *      String, String, Calendar, Calendar)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Album> searchPublicAlbums(String keywordName,
            String keywordComment, String keywordTag, Calendar initDate,
            Calendar endDate) {
        String queryString = "SELECT a FROM Album a "
                + "WHERE a.id IN "
                + "(SELECT sia.album.id FROM ShareInformationAlbums sia WHERE sia.user.id = "
                + Constant.getId() + ") ";
        if (keywordName != null) {
            keywordName = keywordName.toUpperCase();
            queryString += "AND upper(a.name) LIKE :keywordName ";
        }
        if (keywordComment != null) {
            keywordComment = keywordComment.toUpperCase();
            queryString += "AND a.id IN (SELECT ca.album.id FROM CommentAlbum ca "
                    + "WHERE upper(ca.comment.commentText) LIKE :keywordComment ) ";
        }
        if (keywordTag != null) {
            keywordTag = keywordTag.toUpperCase();
            queryString += "AND a.id IN (SELECT ta.album.id FROM TagAlbum ta "
                    + "WHERE upper(ta.tag.name) LIKE :keywordTag) ";
        }
        if (initDate != null) {
            queryString += "AND a.albumDate > :initDate ";
        }
        if (endDate != null) {
            queryString += "AND a.albumDate < :endDate ";
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
        System.out.println(query.getQueryString());
        return query.list();
    }
}
