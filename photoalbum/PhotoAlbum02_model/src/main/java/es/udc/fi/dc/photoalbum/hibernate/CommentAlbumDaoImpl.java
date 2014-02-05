package es.udc.fi.dc.photoalbum.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 */
public class CommentAlbumDaoImpl extends HibernateDaoSupport implements
        CommentAlbumDao {

    /**
     * Method create.
     * 
     * @param t
     *            CommentAlbum
     */
    @Override
    public void create(CommentAlbum t) {
        getHibernateTemplate().save(t);
    }

    /**
     * Method delete.
     * 
     * @param t
     *            CommentAlbum
     */
    @Override
    public void delete(CommentAlbum t) {
        getHibernateTemplate().delete(t);
    }

    /**
     * Method getById.
     * 
     * @param commentAlbumId
     *            int
     * @return CommentAlbum
     * @see es.udc.fi.dc.photoalbum.hibernate.CommentAlbumDao#getById(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public CommentAlbum getById(int commentAlbumId) {
        ArrayList<CommentAlbum> list = (ArrayList<CommentAlbum>) getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(CommentAlbum.class)
                                .add(Restrictions.eq("id", commentAlbumId))
                                .setResultTransformer(
                                        Criteria.DISTINCT_ROOT_ENTITY));
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * Method getCommentAlbumsByAlbum.
     * 
     * @param albumId
     *            int
     * @return List<CommentAlbum>
     * @see es.udc.fi.dc.photoalbum.hibernate.CommentAlbumDao#getCommentAlbumsByAlbum(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<CommentAlbum> getCommentAlbumsByAlbum(int albumId) {
        return (ArrayList<CommentAlbum>) getHibernateTemplate().findByCriteria(
                DetachedCriteria.forClass(CommentAlbum.class)
                        .add(Restrictions.eq("album.id", albumId))
                        .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
    }

    /**
     * Method getCommentAlbumsByUser.
     * 
     * @param userId
     *            int
     * @return List<CommentAlbum>
     * @see es.udc.fi.dc.photoalbum.hibernate.CommentAlbumDao#getCommentAlbumsByUser(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<CommentAlbum> getCommentAlbumsByUser(int userId) {
        return (ArrayList<CommentAlbum>) getHibernateTemplate().findByCriteria(
                DetachedCriteria.forClass(CommentAlbum.class)
                        .createAlias("comment", "co")
                        .createAlias("co.user", "us")
                        .add(Restrictions.eq("us.id", userId))
                        .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
    }

    /**
     * Method getCommentAlbumsByAlbumUser.
     * 
     * @param albumId
     *            int
     * @param userId
     *            int
     * @return List<CommentAlbum>
     * @see es.udc.fi.dc.photoalbum.hibernate.CommentAlbumDao#getCommentAlbumsByAlbumUser(int,
     *      int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<CommentAlbum> getCommentAlbumsByAlbumUser(int albumId,
            int userId) {
        return (ArrayList<CommentAlbum>) getHibernateTemplate().findByCriteria(
                DetachedCriteria.forClass(CommentAlbum.class)
                        .createAlias("comment", "co")
                        .createAlias("co.user", "us")
                        .add(Restrictions.eq("album.id", albumId))
                        .add(Restrictions.eq("us.id", userId))
                        .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
    }

}
