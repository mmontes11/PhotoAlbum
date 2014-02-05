package es.udc.fi.dc.photoalbum.wicket.models;

import java.util.ArrayList;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.spring.FileService;

/**
 */
@SuppressWarnings("serial")
public class FilesModelPaging extends LoadableDetachableModel<ArrayList<File>> {
    @SpringBean
    private FileService fileService;
    private int id;
    private int first;
    private int count;

    /**
     * Constructor for FilesModelPaging.
     * 
     * @param id
     *            int
     * @param first
     *            int
     * @param count
     *            int
     */
    public FilesModelPaging(int id, int first, int count) {
        this.id = id;
        this.first = first;
        this.count = count;
        Injector.get().inject(this);
    }

    /**
     * Method load.
     * 
     * @return ArrayList<File>
     */
    @Override
    protected ArrayList<File> load() {
        return new ArrayList<File>(fileService.getAlbumFilesPaging(id, first,
                count));
    }
}
