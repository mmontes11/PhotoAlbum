package es.udc.fi.dc.photoalbum.wicket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.spring.FileService;
import es.udc.fi.dc.photoalbum.utils.FileComparator;
import es.udc.fi.dc.photoalbum.wicket.models.CustomFilesModelPaging;

/**
 */
@SuppressWarnings("serial")
public class CustomFileListDataProvider implements IDataProvider<File> {
    @SpringBean
    private FileService fileService;
    private ArrayList<File> files;

    /**
     * Method detach.
     * 
     * @see org.apache.wicket.model.IDetachable#detach()
     */
    public void detach() {
    }

    /**
     * Constructor for CustomFileListDataProvider.
     * 
     * @param files
     *            ArrayList<File>
     */
    public CustomFileListDataProvider(ArrayList<File> files) {
    	Collections.sort(files, new FileComparator());
        this.files = new ArrayList<File>(files);
        Injector.get().inject(this);
    }

    /**
     * Method iterator.
     * 
     * @param first
     *            long
     * @param count
     *            long
     * @return Iterator<File>
     * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(long,
     *      long)
     */
    public Iterator<File> iterator(long first, long count) {
        LoadableDetachableModel<ArrayList<File>> ldm = new CustomFilesModelPaging(
                new ArrayList<>(files.subList((int)first, (int)first+(int)count)));
        return ldm.getObject().iterator();
    }

    /**
     * Method size.
     * 
     * @return long
     * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
     */
    public long size() {
        return files.size();
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
