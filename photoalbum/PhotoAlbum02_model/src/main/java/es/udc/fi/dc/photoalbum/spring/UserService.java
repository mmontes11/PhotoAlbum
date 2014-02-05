package es.udc.fi.dc.photoalbum.spring;

import es.udc.fi.dc.photoalbum.hibernate.User;

/**
 */
public interface UserService {

    /**
     * Method create.
     * 
     * @param user
     *            User
     */
    void create(User user);

    /**
     * Method delete.
     * 
     * @param user
     *            User
     */
    void delete(User user);

    /**
     * Method update.
     * 
     * @param user
     *            User
     */
    void update(User user);

    /**
     * Method getUser.
     * 
     * @param email
     *            String
     * @param password
     *            String
     * @return User
     */
    User getUser(String email, String password);

    /**
     * Method getByEmail.
     * 
     * @param userEmail
     *            User
     * @return User
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
