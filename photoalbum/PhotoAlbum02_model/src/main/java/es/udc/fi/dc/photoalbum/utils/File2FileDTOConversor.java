package es.udc.fi.dc.photoalbum.utils;

import java.util.ArrayList;
import java.util.List;

import es.udc.fi.dc.photoalbum.hibernate.Album;
import es.udc.fi.dc.photoalbum.hibernate.File;

/**
 */
public class File2FileDTOConversor {

    /**
     * Method toFileDTO.
     * 
     * @param f
     *            File
     * @return FileDTO
     */
    public static FileDTO toFileDTO(File f) {
        return new FileDTO(f.getId(), f.getName(), f.getFileSmall(), f
                .getAlbum().getName(), 0, f.getFileDate());
    }

    /**
     * Method toFile.
     * 
     * @param fdto
     *            FileDTO
     * @return File
     */
    public static File toFile(FileDTO fdto) {
        File f = new File(fdto.getId(), fdto.getName(), null,
                fdto.getFileSmall(), new Album(-1, fdto.getAlbumName(), null,
                        null, null));
        f.setFileDate(fdto.getDate());
        return f;
    }

    /**
     * Method toListFileDTO.
     * 
     * @param files
     *            List<File>
     * @return List<FileDTO>
     */
    public static List<FileDTO> toListFileDTO(List<File> files) {
        List<FileDTO> listfdtos = new ArrayList<FileDTO>();
        for (File f : files) {
            listfdtos.add(toFileDTO(f));
        }
        return listfdtos;
    }

    /**
     * Method toListFile.
     * 
     * @param filedtos
     *            List<FileDTO>
     * @return List<File>
     */
    public static List<File> toListFile(List<FileDTO> filedtos) {
        List<File> listfiles = new ArrayList<File>();
        for (FileDTO fdto : filedtos) {
            listfiles.add(toFile(fdto));
        }
        return listfiles;
    }

}
