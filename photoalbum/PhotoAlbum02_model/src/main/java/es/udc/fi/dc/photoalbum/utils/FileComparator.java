package es.udc.fi.dc.photoalbum.utils;

import java.util.Comparator;

import es.udc.fi.dc.photoalbum.hibernate.File;

/**
 * Defines comparator for Files, comparing by id
 * 
 * @author usuario
 * @version $Revision: 1.0 $
 */
public class FileComparator implements Comparator<File> {

    /**
     * Method compare.
     * 
     * @param file1
     *            File
     * @param file2
     *            File
     * @return int
     */
    public int compare(File file1, File file2) {
        if (file1.getId() > file2.getId()) {
            return 1;
        } else {
            return -1;
        }
    }
}
