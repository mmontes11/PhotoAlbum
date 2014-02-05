package es.udc.fi.dc.photoalbum.wicket.models;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.spring.FileService;

/**
 */
@SuppressWarnings("serial")
public class FileOwnModel extends LoadableDetachableModel<File> {

    @SpringBean
    private FileService fileService;
    private int id;
    private String name;
    private int userId;

    /**
     * Constructor for FileOwnModel.
     * 
     * @param id
     *            int
     * @param name
     *            String
     * @param userId
     *            int
     */
    public FileOwnModel(int id, String name, int userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        Injector.get().inject(this);
    }

    /**
     * Method load.
     * 
     * @return File
     */
    protected File load() {
        return fileService.getFileOwn(id, name, userId);
    }
}
