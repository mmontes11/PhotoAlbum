package es.udc.fi.dc.photoalbum.spring;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.photoalbum.hibernate.Album;
import es.udc.fi.dc.photoalbum.hibernate.AlbumDao;
import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.hibernate.FileDao;
import es.udc.fi.dc.photoalbum.hibernate.LikeAlbumDao;
import es.udc.fi.dc.photoalbum.hibernate.LikeFileDao;
import es.udc.fi.dc.photoalbum.utils.AlbumDTO;
import es.udc.fi.dc.photoalbum.utils.FileDTO;

/**
 */
@Transactional
public class SearchServiceImpl implements SearchService {

    @Autowired
    private FileDao fileDao;
    @Autowired
    private AlbumDao albumDao;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LikeFileDao likeFileDao;
    @Autowired
    private LikeAlbumDao likeAlbumDao;

    /**
     * Method getAlbumDao.
     * 
     * @return AlbumDao
     */
    public AlbumDao getAlbumDao() {
        return albumDao;
    }

    /**
     * Method setAlbumDao.
     * 
     * @param albumDao
     *            AlbumDao
     */
    public void setAlbumDao(AlbumDao albumDao) {
        this.albumDao = albumDao;
    }

    /**
     * Method getCommentService.
     * 
     * @return CommentService
     */
    public CommentService getCommentService() {
        return commentService;
    }

    /**
     * Method setCommentService.
     * 
     * @param commentService
     *            CommentService
     */
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Method getFileDao.
     * 
     * @return FileDao
     */
    public FileDao getFileDao() {
        return fileDao;
    }

    /**
     * Method setFileDao.
     * 
     * @param fileDao
     *            FileDao
     */
    public void setFileDao(FileDao fileDao) {
        this.fileDao = fileDao;
    }

    /**
     * Method getLikeFileDao.
     * 
     * @return LikeFileDao
     */
    public LikeFileDao getLikeFileDao() {
        return likeFileDao;
    }

    /**
     * Method setLikeFileDao.
     * 
     * @param likeFileDao
     *            LikeFileDao
     */
    public void setLikeFileDao(LikeFileDao likeFileDao) {
        this.likeFileDao = likeFileDao;
    }

    /**
     * Method getLikeAlbumDao.
     * 
     * @return LikeAlbumDao
     */
    public LikeAlbumDao getLikeAlbumDao() {
        return likeAlbumDao;
    }

    /**
     * Method setLikeAlbumDao.
     * 
     * @param likeAlbumDao
     *            LikeAlbumDao
     */
    public void setLikeAlbumDao(LikeAlbumDao likeAlbumDao) {
        this.likeAlbumDao = likeAlbumDao;
    }

    /**
     * Method search.
     * 
     * @param numResources
     *            Integer
     * @param keywordName
     *            String
     * @param keywordComment
     *            String
     * @param keywordTag
     *            String
     * @param resource
     *            int
     * @param hotCriterion
     *            int
     * @param orderCriterion
     *            int
     * @param initDate
     *            Calendar
     * @param endDate
     *            Calendar
     * @return List
     * @see es.udc.fi.dc.photoalbum.spring.SearchService#search(Integer, String,
     *      String, String, int, int, int, Calendar, Calendar)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public List search(Integer numResources, String keywordName,
            String keywordComment, String keywordTag, int resource,
            int hotCriterion, int orderCriterion, Calendar initDate,
            Calendar endDate) {
        List resources = null;
        if (resource == 0) {
            resources = fileDao.searchPublicFiles(keywordName, keywordComment,
                    keywordTag, initDate, endDate);
        }
        if (resource == 1) {
            resources = albumDao.searchPublicAlbums(keywordName,
                    keywordComment, keywordTag, initDate, endDate);
        }
        if (resource == 2) {
            resources = fileDao.searchPublicFiles(keywordName, keywordComment,
                    keywordTag, initDate, endDate);
            resources.addAll(albumDao.searchPublicAlbums(keywordName,
                    keywordComment, keywordTag, initDate, endDate));
        }

        List result = new ArrayList<>();

        if (numResources != null && numResources == 0) {
            return result;
        }

        for (Object r : resources) {
            int numLikesOrComments = 0;
            if (r instanceof File) {
                File file = (File) r;
                if (hotCriterion == 1) {
                    numLikesOrComments = commentService.getCommentFilesByFile(
                            file.getId()).size();
                } else {
                    numLikesOrComments = likeFileDao
                            .getPositiveLikesByFile(file.getId());
                }
                if (numLikesOrComments == 0) {
                    continue;
                }

                if (numResources == null) {
                    result.add(new FileDTO(file.getId(), file.getName(), file
                            .getFileSmall(), file.getAlbum().getName(),
                            numLikesOrComments, file.getFileDate()));

                } else {
                    if (result.size() < numResources) {
                        result.add(new FileDTO(file.getId(), file.getName(),
                                file.getFileSmall(), file.getAlbum().getName(),
                                numLikesOrComments, file.getFileDate()));
                    } else {
                        int index = getMinimum(result);
                        int min = ((FileDTO) result.get(index)).getNum();
                        if (numLikesOrComments >= min) {
                            result.remove(index);
                            result.add(new FileDTO(file.getId(),
                                    file.getName(), file.getFileSmall(), file
                                            .getAlbum().getName(),
                                    numLikesOrComments, file.getFileDate()));
                        }
                    }
                }
            } else {
                if (r instanceof Album) {
                    Album album = (Album) r;
                    if (hotCriterion == 1) {
                        numLikesOrComments = commentService
                                .getCommentAlbumsByAlbum(album.getId()).size();
                    } else {
                        numLikesOrComments = likeAlbumDao
                                .getPositiveLikesByAlbum(album.getId());
                    }
                    if (numLikesOrComments == 0) {
                        continue;
                    }

                    if (numResources == null) {
                        result.add(new AlbumDTO(album.getId(), album.getName(),
                                album.getUser().getUsername(),
                                numLikesOrComments, album.getAlbumDate()));

                    } else {
                        if (result.size() < numResources) {
                            result.add(new AlbumDTO(album.getId(), album
                                    .getName(), album.getUser().getUsername(),
                                    numLikesOrComments, album.getAlbumDate()));
                        } else {
                            int index = getMinimum(result);
                            int min = ((AlbumDTO) result.get(index)).getNum();
                            if (numLikesOrComments >= min) {
                                result.remove(index);
                                result.add(new AlbumDTO(album.getId(), album
                                        .getName(), album.getUser()
                                        .getUsername(), numLikesOrComments,
                                        album.getAlbumDate()));
                            }
                        }
                    }
                }
            }
        }
        if (orderCriterion == 1) {
            Collections.sort(result, new SortResourceByDate());
        } else {
            Collections.sort(result, new SortResource());
        }

        return result;
    }

    /**
     * Method getMinimum.
     * 
     * @param list
     *            List<Object>
     * @return int
     */
    private int getMinimum(List<Object> list) {
        int minimum = 0;
        if (list.get(0) instanceof FileDTO) {
            minimum = list.isEmpty() ? 0 : ((FileDTO) list.get(0)).getNum();
        } else {
            if (list.get(0) instanceof AlbumDTO) {
                minimum = list.isEmpty() ? 0 : ((AlbumDTO) list.get(0))
                        .getNum();
            }
        }
        int index = 0;
        for (Object r : list) {
            if (r instanceof FileDTO) {
                FileDTO fileDTO = (FileDTO) r;
                if (fileDTO.getNum() < minimum) {
                    minimum = fileDTO.getNum();
                    index = list.indexOf(r);
                }
            } else {
                if (r instanceof AlbumDTO) {
                    AlbumDTO albumDTO = (AlbumDTO) r;
                    if (albumDTO.getNum() < minimum) {
                        minimum = albumDTO.getNum();
                        index = list.indexOf(r);
                    }
                }
            }
        }
        return index;
    }

    /**
	 */
    private class SortResourceByDate implements Comparator<Object> {
        /**
         * Method compare.
         * 
         * @param o1
         *            Object
         * @param o2
         *            Object
         * @return int
         */
        @Override
        public int compare(Object o1, Object o2) {
            if (o1 instanceof FileDTO && o2 instanceof FileDTO) {
                return ((FileDTO) o2).getDate().compareTo(
                        ((FileDTO) o1).getDate());
            }
            if (o1 instanceof AlbumDTO && o2 instanceof AlbumDTO) {
                return ((AlbumDTO) o2).getDate().compareTo(
                        ((AlbumDTO) o1).getDate());
            }
            if (o1 instanceof FileDTO && o2 instanceof AlbumDTO) {
                return ((AlbumDTO) o2).getDate().compareTo(
                        ((FileDTO) o1).getDate());
            }
            if (o1 instanceof AlbumDTO && o2 instanceof FileDTO) {
                return ((FileDTO) o2).getDate().compareTo(
                        ((AlbumDTO) o1).getDate());
            }
            return 0;
        }
    }

    /**
	 */
    private class SortResource implements Comparator<Object> {
        /**
         * Method compare.
         * 
         * @param o1
         *            Object
         * @param o2
         *            Object
         * @return int
         */
        @Override
        public int compare(Object o1, Object o2) {
            if (o1 instanceof FileDTO && o2 instanceof FileDTO) {
                return (((FileDTO) o2).getNum() - ((FileDTO) o1).getNum());
            }
            if (o1 instanceof AlbumDTO && o2 instanceof AlbumDTO) {
                return (((AlbumDTO) o2).getNum() - ((AlbumDTO) o1).getNum());
            }
            if (o1 instanceof FileDTO && o2 instanceof AlbumDTO) {
                return (((AlbumDTO) o2).getNum() - ((FileDTO) o1).getNum());
            }
            if (o1 instanceof AlbumDTO && o2 instanceof FileDTO) {
                return (((FileDTO) o2).getNum() - ((AlbumDTO) o1).getNum());
            }
            return 0;
        }
    }
}
