package es.udc.fi.dc.photoalbum.hibernate;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 */
public class TagAlbumDaoImpl extends HibernateDaoSupport implements TagAlbumDao {

    /**
     * Method create.
     * 
     * @param t
     *            TagAlbum
     */
    public void create(TagAlbum t) {
        getHibernateTemplate().save(t);
    }

    /**
     * Method delete.
     * 
     * @param t
     *            TagAlbum
     */
    public void delete(TagAlbum t) {
        getHibernateTemplate().delete(t);
    }

    /**
     * Method getById.
     * 
     * @param tagAlbumId
     *            int
     * @return TagAlbum
     * @see es.udc.fi.dc.photoalbum.hibernate.TagAlbumDao#getById(int)
     */
    public TagAlbum getById(int tagAlbumId) {
        return getHibernateTemplate().get(TagAlbum.class, tagAlbumId);
    }

    /**
     * Method getTagsByAlbumId.
     * 
     * @param albumId
     *            int
     * @return ArrayList<TagAlbum>
     * @see es.udc.fi.dc.photoalbum.hibernate.TagAlbumDao#getTagsByAlbumId(int)
     */
    @SuppressWarnings("unchecked")
    public ArrayList<TagAlbum> getTagsByAlbumId(int albumId) {
        return (ArrayList<TagAlbum>) getHibernateTemplate().findByCriteria(
                DetachedCriteria.forClass(TagAlbum.class)
                        .add(Restrictions.eq("album.id", albumId))
                        .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
    }

    /**
     * Method getByTagId.
     * 
     * @param tagId
     *            int
     * @return ArrayList<TagAlbum>
     * @see es.udc.fi.dc.photoalbum.hibernate.TagAlbumDao#getByTagId(int)
     */
    @SuppressWarnings("unchecked")
    public ArrayList<TagAlbum> getByTagId(int tagId) {
        return (ArrayList<TagAlbum>) getHibernateTemplate().findByCriteria(
                DetachedCriteria.forClass(TagAlbum.class)
                        .add(Restrictions.eq("tag.id", tagId))
                        .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
    }

    /**
     * Method get.
     * 
     * @param tagId
     *            Integer
     * @param albumId
     *            Integer
     * @return TagAlbum
     * @see es.udc.fi.dc.photoalbum.hibernate.TagAlbumDao#get(Integer, Integer)
     */
    @SuppressWarnings("unchecked")
    public TagAlbum get(Integer tagId, Integer albumId) {
        ArrayList<TagAlbum> list = (ArrayList<TagAlbum>) getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(TagAlbum.class)
                                .add(Restrictions.eq("tag.id", tagId))
                                .add(Restrictions.eq("album.id", albumId))
                                .setResultTransformer(
                                        Criteria.DISTINCT_ROOT_ENTITY));
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

}
