package es.udc.fi.dc.photoalbum.wicket;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.rowset.serial.SerialBlob;

import es.udc.fi.dc.photoalbum.hibernate.File;

/**
 * Gets Blob from File
 * 
 * @author usuario
 * @version $Revision: 1.0 $
 */
public final class BlobFromFile {

    /**
     * Constructor for BlobFromFile.
     */
    private BlobFromFile() {
    }

    /**
     * @param file
     *            file, small image from what you want to get
     * 
     * @return blob of small image
     */
    public static Blob getSmall(File file) {
        try {
            return new SerialBlob(file.getFileSmall());
        } catch (SQLException e) {
            Logger.getLogger(BlobFromFile.class.getName()).log(Level.WARNING,
                    e.toString(), e);
            return null;
        }
    }

    /**
     * @param file
     *            file, big image from what you want to get
     * 
     * @return blob of big image
     */
    public static Blob getBig(File file) {
        try {
            return new SerialBlob(file.getFile());
        } catch (SQLException e) {
            Logger.getLogger(BlobFromFile.class.getName()).log(Level.WARNING,
                    e.toString(), e);
            return null;
        }
    }
}
