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
public class CustomFilesModel extends LoadableDetachableModel<ArrayList<File>> {

    @SpringBean
    private FileService fileService;
    private ArrayList<File> list;

    /**
     * Constructor for CustomFilesModel.
     * 
     * @param list
     *            ArrayList<File>
     */
    public CustomFilesModel(ArrayList<File> list) {
        this.list = list;
        Injector.get().inject(this);
    }

    /**
     * Method load.
     * 
     * @return ArrayList<File>
     */
    @Override
    protected ArrayList<File> load() {
        ArrayList<File> files = new ArrayList<File>();
        for (File fid : list) {
            files.add(fileService.getById(fid.getId()));
        }
        Collections.sort(files, new FileComparator());
        return files;
    }
}
