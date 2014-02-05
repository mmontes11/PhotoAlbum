package es.udc.fi.dc.photoalbum.spring;

import java.util.ArrayList;

import es.udc.fi.dc.photoalbum.hibernate.Album;
import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.hibernate.Tag;
import es.udc.fi.dc.photoalbum.hibernate.TagAlbum;
import es.udc.fi.dc.photoalbum.hibernate.TagFile;

/**
 */
public interface TagService {

    /**
     * Method create.
     * 
     * @param newTag
     *            Tag
     */
    void create(Tag newTag);

    /**
     * Method create.
     * 
     * @param tag
     *            TagAlbum
     */
    void create(TagAlbum tag);

    /**
     * Method create.
     * 
     * @param tag
     *            TagFile
     */
    void create(TagFile tag);

    /**
     * El objeto tag se deberia borrar si se acaban las referencias a el desde
     * TagAlbum o TagFoto
     * 
     * @param tag
     *            TagAlbum
     */
    void delete(TagAlbum tag);

    /**
     * Method delete.
     * 
     * @param tag
     *            TagFile
     */
    void delete(TagFile tag);

    /**
     * Method getTagById.
     * 
     * @param id
     *            int
     * @return Tag
     */
    Tag getTagById(int id);

    /**
     * Method getTagbyName.
     * 
     * @param name
     *            String
     * @return Tag
     */
    Tag getTagbyName(String name);

    /**
     * Method getTagAlbum.
     * 
     * @param tagAlbumId
     *            int
     * @return TagAlbum
     */
    TagAlbum getTagAlbum(int tagAlbumId);

    /**
     * Method getTagFile.
     * 
     * @param tagFileId
     *            int
     * @return TagFile
     */
    TagFile getTagFile(int tagFileId);

    /**
     * Method getTagsByFileId.
     * 
     * @param fileId
     *            int
     * @return ArrayList<TagFile>
     */
    ArrayList<TagFile> getTagsByFileId(int fileId);

    /**
     * Method getTagsByAbumId.
     * 
     * @param albumId
     *            int
     * @return ArrayList<TagAlbum>
     */
    ArrayList<TagAlbum> getTagsByAbumId(int albumId);

    /**
     * 
     * @param userId
     *            el usuario que quiere la informacion
     * 
     * @param tagId
     *            int
     * @return los albumes que tienen ese tag respetando la reglas de privacidad
     */

    // usuario no tiene acceso a la foto
    ArrayList<Album> getAlbumes(int tagId, int userId);

    /**
     * @param tagId
     *            el id del tag
     * @param albumId
     *            el id de album
     * @param userId
     *            el usuario que quiere la informacion
     * 
     * @return las fotos que puede ver el usuario filtrando el album
     */
    ArrayList<File> getFiles(int tagId, int albumId, int userId);

    /**
     * @param tagId
     *            el id del tag
     * @param userId
     *            el usuario que quiere la informacion
     * 
     * @return todas las fotos que puede ver el usuario
     */
    ArrayList<File> getAllFiles(int tagId, int userId);

}
