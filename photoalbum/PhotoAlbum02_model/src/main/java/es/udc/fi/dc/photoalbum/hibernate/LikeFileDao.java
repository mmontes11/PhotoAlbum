package es.udc.fi.dc.photoalbum.hibernate;

import java.util.List;

/**
 */
public interface LikeFileDao extends GenericDao<LikeFile> {

    /**
     * Method getLikeFileByFile.
     * 
     * @param fileId
     *            int
     * @return List<LikeFile>
     */
    List<LikeFile> getLikeFileByFile(int fileId);

    /**
     * Method getPositiveLikesByFile.
     * 
     * @param fileId
     *            int
     * @return int
     */
    int getPositiveLikesByFile(int fileId);

}
