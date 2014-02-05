package es.udc.fi.dc.photoalbum.wicket;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.spring.FileService;
import es.udc.fi.dc.photoalbum.wicket.models.FilesModelPaging;

/**
 */
@SuppressWarnings("serial")
public class FileListDataProvider implements IDataProvider<File> {
    @SpringBean
    private FileService fileService;
    private int size;
    private int id;

    /**
     * Method detach.
     * 
     * @see org.apache.wicket.model.IDetachable#detach()
     */
    public void detach() {
    }

    /**
     * Constructor for FileListDataProvider.
     * 
     * @param size
     *            int
     * @param id
     *            int
     */
    public FileListDataProvider(int size, int id) {
        this.size = size;
        this.id = id;
        Injector.get().inject(this);
    }

    /**
     * Method iterator.
     * 
     * @param arg0
     *            long
     * @param arg1
     *            long
     * @return Iterator<File>
     * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(long,
     *      long)
     */
    public Iterator<File> iterator(long arg0, long arg1) {
        int first = (int) arg0;
        int count = (int) arg1;
        LoadableDetachableModel<ArrayList<File>> ldm = new FilesModelPaging(
                this.id, first, count);
        return ldm.getObject().iterator();
    }

    /**
     * Method size.
     * 
     * @return long
     * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
     */
    public long size() {
        return this.size;
    }

    /**
     * Method model.
     * 
     * @param object
     *            File
     * @return IModel<File>
     */
    public IModel<File> model(File object) {
        final Integer id = object.getId();
        return new LoadableDetachableModel<File>() {
            @Override
            protected File load() {
                return fileService.getById(id);
            }
        };
    }

}
