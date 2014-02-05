package es.udc.fi.dc.photoalbum.spring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.photoalbum.hibernate.Album;
import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationAlbums;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationAlbumsDao;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationPhotos;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationPhotosDao;
import es.udc.fi.dc.photoalbum.hibernate.Tag;
import es.udc.fi.dc.photoalbum.hibernate.TagAlbum;
import es.udc.fi.dc.photoalbum.hibernate.TagAlbumDao;
import es.udc.fi.dc.photoalbum.hibernate.TagDao;
import es.udc.fi.dc.photoalbum.hibernate.TagFile;
import es.udc.fi.dc.photoalbum.hibernate.TagFileDao;
import es.udc.fi.dc.photoalbum.utils.Constant;
import es.udc.fi.dc.photoalbum.utils.FileComparator;

/**
 */
@Transactional
public class TagServiceImpl implements TagService {

    private TagDao tagDao;
    private TagAlbumDao tagAlbumDao;
    private TagFileDao tagFileDao;
    private ShareInformationPhotosDao shareInformationPhotosDao;
    private ShareInformationAlbumsDao shareInformationAlbumsDao;

    /**
     * Method setTagDao.
     * 
     * @param tagDao
     *            TagDao
     */
    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    /**
     * Method setTagAlbumDao.
     * 
     * @param tagAlbumDao
     *            TagAlbumDao
     */
    public void setTagAlbumDao(TagAlbumDao tagAlbumDao) {
        this.tagAlbumDao = tagAlbumDao;
    }

    /**
     * Method setTagFileDao.
     * 
     * @param tagFileDao
     *            TagFileDao
     */
    public void setTagFileDao(TagFileDao tagFileDao) {
        this.tagFileDao = tagFileDao;
    }

    /**
     * Method getShareInformationPhotosDao.
     * 
     * @return ShareInformationPhotosDao
     */
    public ShareInformationPhotosDao getShareInformationPhotosDao() {
        return shareInformationPhotosDao;
    }

    /**
     * Method setShareInformationPhotosDao.
     * 
     * @param shareInformationPhotosDao
     *            ShareInformationPhotosDao
     */
    public void setShareInformationPhotosDao(
            ShareInformationPhotosDao shareInformationPhotosDao) {
        this.shareInformationPhotosDao = shareInformationPhotosDao;
    }

    /**
     * Method setShareInformationAlbumsDao.
     * 
     * @param shareInformationAlbumsDao
     *            ShareInformationAlbumsDao
     */
    public void setShareInformationAlbumsDao(
            ShareInformationAlbumsDao shareInformationAlbumsDao) {
        this.shareInformationAlbumsDao = shareInformationAlbumsDao;
    }

    /**
     * Method create.
     * 
     * @param newTag
     *            Tag
     * @see es.udc.fi.dc.photoalbum.spring.TagService#create(Tag)
     */
    public void create(Tag newTag) {
        tagDao.create(newTag);
    }

    /**
     * Method create.
     * 
     * @param tagAlbum
     *            TagAlbum
     * @see es.udc.fi.dc.photoalbum.spring.TagService#create(TagAlbum)
     */
    public void create(TagAlbum tagAlbum) {
        tagAlbumDao.create(tagAlbum);
        for (File file : tagAlbum.getAlbum().getFiles()) {
            if (tagFileDao.get(tagAlbum.getTag().getId(), file.getId()) == null) {
                create(new TagFile(null, tagAlbum.getTag(), file));
            }
        }
    }

    /**
     * Method create.
     * 
     * @param tag
     *            TagFile
     * @see es.udc.fi.dc.photoalbum.spring.TagService#create(TagFile)
     */
    public void create(TagFile tag) {
        tagFileDao.create(tag);
        if (tagAlbumDao.get(tag.getTag().getId(), tag.getFile().getAlbum()
                .getId()) == null) {
            create(new TagAlbum(null, tag.getTag(), tag.getFile().getAlbum()));
        }
    }

    /**
     * Method delete.
     * 
     * @param tAlbum
     *            TagAlbum
     * @see es.udc.fi.dc.photoalbum.spring.TagService#delete(TagAlbum)
     */
    public void delete(TagAlbum tAlbum) {
        TagAlbum tagAlbum = tagAlbumDao.getById(tAlbum.getId());
        Tag tag = tagDao.getById(tagAlbum.getTag().getId());
        Album album = tagAlbum.getAlbum();
        tagAlbumDao.delete(tagAlbum);

        for (File file : album.getFiles()) {
            TagFile tagFile = tagFileDao.get(tag.getId(), file.getId());
            if (tagFile != null) {
                deleteSimple(tagFile);
            }
        }

        if (!isReferenced(tag.getId())) {
            tagDao.delete(tag);
        }
    }

    /**
     * Method deleteSimple.
     * 
     * @param tagAlbum
     *            TagAlbum
     */
    private void deleteSimple(TagAlbum tagAlbum) {
        tagAlbumDao.delete(tagAlbum);
    }

    /**
     * Method isReferenced.
     * 
     * @param tagId
     *            Integer
     * @return boolean
     */
    private boolean isReferenced(Integer tagId) {
        return !(tagAlbumDao.getByTagId(tagId).isEmpty() && tagFileDao
                .getByTagId(tagId).isEmpty());
    }

    /**
     * Method deleteSimple.
     * 
     * @param tagFile
     *            TagFile
     */
    private void deleteSimple(TagFile tagFile) {
        tagFileDao.delete(tagFile);
    }

    /**
     * Method delete.
     * 
     * @param tFile
     *            TagFile
     * @see es.udc.fi.dc.photoalbum.spring.TagService#delete(TagFile)
     */
    public void delete(TagFile tFile) {
        TagFile tagFile = tagFileDao.getById(tFile.getId());
        Tag tag = tagDao.getById(tagFile.getTag().getId());
        Album album = tagFile.getFile().getAlbum();
        tagFileDao.delete(tagFile);

        if (tagFileDao.getByTagIdandAlbumId(tag.getId(), album.getId())
                .isEmpty()) {
            deleteSimple(tagAlbumDao.get(tag.getId(), album.getId()));
        }

        if (!isReferenced(tag.getId())) {
            tagDao.delete(tag);
        }
    }

    /**
     * Method getTagById.
     * 
     * @param id
     *            int
     * @return Tag
     * @see es.udc.fi.dc.photoalbum.spring.TagService#getTagById(int)
     */
    public Tag getTagById(int id) {
        return tagDao.getById(id);
    }

    /**
     * Method getTagbyName.
     * 
     * @param name
     *            String
     * @return Tag
     * @see es.udc.fi.dc.photoalbum.spring.TagService#getTagbyName(String)
     */
    public Tag getTagbyName(String name) {
        return tagDao.getByName(name);
    }

    /**
     * Method getTagAlbum.
     * 
     * @param tagAlbumId
     *            int
     * @return TagAlbum
     * @see es.udc.fi.dc.photoalbum.spring.TagService#getTagAlbum(int)
     */
    public TagAlbum getTagAlbum(int tagAlbumId) {
        return tagAlbumDao.getById(tagAlbumId);
    }

    /**
     * Method getTagFile.
     * 
     * @param tagFileId
     *            int
     * @return TagFile
     * @see es.udc.fi.dc.photoalbum.spring.TagService#getTagFile(int)
     */
    public TagFile getTagFile(int tagFileId) {
        return tagFileDao.getById(tagFileId);
    }

    /**
     * Method getTagsByFileId.
     * 
     * @param fileId
     *            int
     * @return ArrayList<TagFile>
     * @see es.udc.fi.dc.photoalbum.spring.TagService#getTagsByFileId(int)
     */
    public ArrayList<TagFile> getTagsByFileId(int fileId) {
        return tagFileDao.getTagsByFileId(fileId);
    }

    /**
     * Method getTagsByAbumId.
     * 
     * @param albumId
     *            int
     * @return ArrayList<TagAlbum>
     * @see es.udc.fi.dc.photoalbum.spring.TagService#getTagsByAbumId(int)
     */
    public ArrayList<TagAlbum> getTagsByAbumId(int albumId) {
        return tagAlbumDao.getTagsByAlbumId(albumId);
    }

    /**
     * Method getAlbumes.
     * 
     * @param tagId
     *            int
     * @param userId
     *            int
     * @return ArrayList<Album>
     * @see es.udc.fi.dc.photoalbum.spring.TagService#getAlbumes(int, int)
     */
    public ArrayList<Album> getAlbumes(int tagId, int userId) {
        ArrayList<Album> albumList = new ArrayList<Album>();
        ArrayList<TagAlbum> tagList = new ArrayList<TagAlbum>(
                tagAlbumDao.getByTagId(tagId));
        ArrayList<ShareInformationAlbums> shared = shareInformationAlbumsDao
                .getUserShares(userId);
        shared.addAll(shareInformationAlbumsDao.getUserShares(Constant.getId()));
        for (TagAlbum tagAlbum : tagList) {
            if (tagAlbum.getAlbum().getUser().getId() == userId) {
                albumList.add(tagAlbum.getAlbum());
            } else {
                for (ShareInformationAlbums sAlbum : shared) {
                    if (tagAlbum.getAlbum().getId()
                            .equals(sAlbum.getAlbum().getId())
                            && !albumList.contains(tagAlbum.getAlbum())) {
                        if (!getFiles(tagId, tagAlbum.getAlbum().getId(),
                                userId).isEmpty()) {
                            albumList.add(tagAlbum.getAlbum());
                        }
                    }
                }
            }
        }
        return albumList;
    }

    /**
     * Method getFiles.
     * 
     * @param tagId
     *            int
     * @param albumId
     *            int
     * @param userId
     *            int
     * @return ArrayList<File>
     * @see es.udc.fi.dc.photoalbum.spring.TagService#getFiles(int, int, int)
     */
    public ArrayList<File> getFiles(int tagId, int albumId, int userId) {
        ArrayList<File> fileList = new ArrayList<File>(getAllFiles(tagId,
                userId));
        ArrayList<File> rfileList = new ArrayList<File>();

        for (File file : fileList) {
            if (file.getAlbum().getId() == albumId) {
                rfileList.add(file);
            }
        }
        Collections.sort(rfileList, new FileComparator());
        return rfileList;
    }

    /**
     * Method getAllFiles.
     * 
     * @param tagId
     *            int
     * @param userId
     *            int
     * @return ArrayList<File>
     * @see es.udc.fi.dc.photoalbum.spring.TagService#getAllFiles(int, int)
     */
    @Override
    public ArrayList<File> getAllFiles(int tagId, int userId) {
        ArrayList<File> fileList = new ArrayList<File>();
        ArrayList<TagFile> tagList = new ArrayList<TagFile>(
                tagFileDao.getByTagId(tagId));
        List<ShareInformationPhotos> shared = shareInformationPhotosDao
                .getPhotosSharesTo(userId);
        shared.addAll(shareInformationPhotosDao.getPhotosSharesTo(Constant
                .getId()));

        for (TagFile tagFile : tagList) {
            if (tagFile.getFile().getAlbum().getUser().getId() == userId) {
                fileList.add(tagFile.getFile());
            } else {
                for (ShareInformationPhotos sFile : shared) {
                    if (tagFile.getFile().getId()
                            .equals(sFile.getFile().getId())
                            && !fileList.contains(tagFile.getFile())) {
                        fileList.add(tagFile.getFile());
                    }
                }
            }
        }
        Collections.sort(fileList, new FileComparator());
        return fileList;

    }

}
