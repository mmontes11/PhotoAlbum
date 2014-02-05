package es.udc.fi.dc.photoalbum.wicket.models;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.spring.FileService;

/**
 */
@SuppressWarnings("serial")
public class FileModelForNavigate extends LoadableDetachableModel<File> {
    private Integer id;
    @SpringBean
    private FileService fileService;

    /**
     * Method getId.
     * 
     * @return Integer
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Method setId.
     * 
     * @param id
     *            Integer
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Constructor for FileModelForNavigate.
     * 
     * @param id
     *            Integer
     */
    public FileModelForNavigate(Integer id) {
        this.id = id;
        Injector.get().inject(this);
    }

    /**
     * Method load.
     * 
     * @return File
     */
    @Override
    protected File load() {
        if (this.id == -1) {
            return null;
        } else {
            System.out.println(this.id);
            return fileService.getById(this.id);
        }

    }
}
