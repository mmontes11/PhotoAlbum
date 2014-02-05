package es.udc.fi.dc.photoalbum.hibernate;

/**
 */
public interface GenericDao<T> {

    /**
     * Method create.
     * 
     * @param t
     *            T
     */
    void create(T t);

    /**
     * Method delete.
     * 
     * @param t
     *            T
     */
    void delete(T t);
}
