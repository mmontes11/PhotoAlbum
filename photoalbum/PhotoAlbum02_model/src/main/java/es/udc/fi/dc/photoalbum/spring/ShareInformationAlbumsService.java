package es.udc.fi.dc.photoalbum.spring;

import java.util.ArrayList;
import java.util.List;

import es.udc.fi.dc.photoalbum.hibernate.Album;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationAlbums;
import es.udc.fi.dc.photoalbum.hibernate.User;

/**
 */
public interface ShareInformationAlbumsService {

    /**
     * Method create.
     * 
     * @param shareInformation
     *            ShareInformationAlbums
     */
    void create(ShareInformationAlbums shareInformation);

    /**
     * Method delete.
     * 
     * @param shareInformation
     *            ShareInformationAlbums
     */
    void delete(ShareInformationAlbums shareInformation);

    /**
     * @param userShared
     *            usuario propietario
     * @param userSharedTo
     *            usuario con permisos
     * 
     * @return lista con la informacion de los albumes compartidos
     */
    List<ShareInformationAlbums> getShares(User userShared, User userSharedTo);

    /**
     * 
     * @param userSharedToId
     *            id del usuario con permisos de acceso
     * @param userSharedEmail
     *            email del propietario
     * 
     * @param albumId
     *            int
     * @return el objeto con la informacion del album compartido
     */
    ShareInformationAlbums getShare(int albumId, int userSharedToId,
            String userSharedEmail);

    /**
     * @param albumId
     *            id del album compartido
     * @param userSharedToId
     *            id del usuario con permisos
     * 
     * @return objeto con la informacion del album compartido
     */
    ShareInformationAlbums getShare(int albumId, int userSharedToId);

    /**
     * Method getAlbumShares.
     * 
     * @param albumId
     *            int
     * @return ArrayList<ShareInformationAlbums>
     */
    ArrayList<ShareInformationAlbums> getAlbumShares(int albumId);

    /**
     * Method getUserShares.
     * 
     * @param userId
     *            int
     * @return ArrayList<ShareInformationAlbums>
     */
    ArrayList<ShareInformationAlbums> getUserShares(int userId);

    /**
     * @param album
     * @param userSharedToId
     *            id del usuario con permisos de acceso
     * 
     * @return true si estan todos los archivos del album ya compartidos
     */
    public boolean alreadyShared(Album album, int userSharedToId);
}
