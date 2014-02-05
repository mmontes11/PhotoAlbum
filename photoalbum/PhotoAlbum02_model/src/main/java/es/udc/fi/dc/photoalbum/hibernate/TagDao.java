package es.udc.fi.dc.photoalbum.hibernate;

/**
 */
public interface TagDao extends GenericDao<Tag> {

    /**
     * Method getByName.
     * 
     * @param name
     *            String
     * @return Tag
     */
    Tag getByName(String name);

    /**
     * Method getById.
     * 
     * @param id
     *            Integer
     * @return Tag
     */
    Tag getById(Integer id);

    /**
     * Method rename.
     * 
     * @param tag
     *            Tag
     * @param name
     *            String
     */
    void rename(Tag tag, String name);

}
