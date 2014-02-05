package es.udc.fi.dc.photoalbum.wicket.models;

import java.util.ArrayList;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.spring.FileService;

/**
 */
public class CustomFileModel extends LoadableDetachableModel<File> {
    @SpringBean
    private FileService fileService;
    private int id;
    private ArrayList<File> files;

    /**
     * Constructor for CustomFileModel.
     * 
     * @param id
     *            int
     * @param files
     *            ArrayList<File>
     */
    public CustomFileModel(int id, ArrayList<File> files) {
        this.id = id;
        this.files = new ArrayList<File>(files);
        Injector.get().inject(this);
    }

    /**
     * Method load.
     * 
     * @return File
     */
    protected File load() {
        for (File file : files) {
            if (id == file.getId()) {
                return fileService.getById(id);
            }
        }
        return null;
    }
}
