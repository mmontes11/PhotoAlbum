package es.udc.fi.dc.photoalbum.wicket.models;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.spring.FileService;
import es.udc.fi.dc.photoalbum.utils.FileComparator;

/**
 */
@SuppressWarnings("serial")
public class FilesModel extends LoadableDetachableModel<ArrayList<File>> {

    @SpringBean
    private FileService fileService;
    private int id;

    /**
     * Constructor for FilesModel.
     * 
     * @param id
     *            int
     */
    public FilesModel(int id) {
        this.id = id;
        Injector.get().inject(this);
    }

    /**
     * Method load.
     * 
     * @return ArrayList<File>
     */
    @Override
    protected ArrayList<File> load() {
        ArrayList<File> list = new ArrayList<File>(
                fileService.getAlbumFiles(id));
        Collections.sort(list, new FileComparator());
        return list;
    }
}
