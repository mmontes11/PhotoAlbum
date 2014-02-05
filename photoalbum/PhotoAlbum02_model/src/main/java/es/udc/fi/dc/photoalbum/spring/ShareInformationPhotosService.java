package es.udc.fi.dc.photoalbum.spring;

import java.util.List;

import es.udc.fi.dc.photoalbum.hibernate.ShareInformationPhotos;

/**
 */
public interface ShareInformationPhotosService {

    /**
     * Method create.
     * 
     * @param sip
     *            ShareInformationPhotos
     */
    void create(ShareInformationPhotos sip);

    /**
     * Method delete.
     * 
     * @param sip
     *            ShareInformationPhotos
     */
    void delete(ShareInformationPhotos sip);

    /**
     * @param photoId
     *            identificador de la foto
     * @param userSharedToId
     *            usuario con derechos de accesp
     * @param userSharedEmail
     *            usuario propietario
     * 
     * @return informacion de la comparticion
     */
    ShareInformationPhotos getShare(int photoId, int userSharedToId,
            String userSharedEmail);

    /**
     * @param photoId
     *            identificador de la foto
     * 
     * @return lista de los usuarios con los que se comparte
     */
    List<ShareInformationPhotos> getPhotosShares(int photoId);

    /**
     * @param albumId
     *            identificador del album
     * @param userId
     *            usuario con permisos de acceso
     * 
     * @return fotos compartidas con el usuario en ese album
     */
    List<ShareInformationPhotos> getPhotosShares(int albumId, int userId);

    /**
     * @param photoid
     *            identificador de la foto
     * @param userId
     *            usuario con derechos de acceso
     * 
     * @return informacion de la comparticion
     */
    ShareInformationPhotos getShareInformation(int photoid, int userId);

}
