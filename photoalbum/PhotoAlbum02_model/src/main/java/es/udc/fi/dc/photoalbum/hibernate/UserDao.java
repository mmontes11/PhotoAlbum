package es.udc.fi.dc.photoalbum.hibernate;

/**
 */
public interface UserDao extends GenericDao<User> {

    /**
     * Method update.
     * 
     * @param user
     *            User
     */
    void update(User user);

    /**
     * @param email
     *            email of user
     * @param password
     *            password of user
     * 
     * @return user instance if exists or null
     */
    User getUser(String email, String password);

    /**
     * @param userEmail
     *            email of user
     * 
     * @return user if exists or null
     */
    User getByEmail(User userEmail);

    /**
     * Method getById.
     * 
     * @param id
     *            Integer
     * @return User
     */
    User getById(Integer id);

    /**
     * Method getByUsername.
     * 
     * @param user
     *            User
     * @return User
     */
    User getByUsername(User user);
}
