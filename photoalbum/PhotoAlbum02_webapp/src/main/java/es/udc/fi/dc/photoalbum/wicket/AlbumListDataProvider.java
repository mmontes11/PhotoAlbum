package es.udc.fi.dc.photoalbum.wicket;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import es.udc.fi.dc.photoalbum.hibernate.Album;
import es.udc.fi.dc.photoalbum.spring.AlbumService;
import es.udc.fi.dc.photoalbum.wicket.models.AlbumsModelFull;

/**
 */
@SuppressWarnings("serial")
public class AlbumListDataProvider implements IDataProvider<Album> {

    @SpringBean
    private AlbumService albumService;
    private int size;

    /**
     * Constructor for AlbumListDataProvider.
     * 
     * @param size
     *            int
     */
    public AlbumListDataProvider(int size) {
        this.size = size;
        Injector.get().inject(this);
    }

    /**
     * Method detach.
     * 
     * @see org.apache.wicket.model.IDetachable#detach()
     */
    public void detach() {
    }

    /**
     * Method iterator.
     * 
     * @param arg0
     *            long
     * @param arg1
     *            long
     * @return Iterator<? extends Album>
     * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(long,
     *      long)
     */
    public Iterator<? extends Album> iterator(long arg0, long arg1) {
        int first = (int) arg0;
        int count = (int) arg1;
        LoadableDetachableModel<ArrayList<Album>> ldm = new AlbumsModelFull();
        int toIndex = first + count;
        if (toIndex > ldm.getObject().size()) {
            toIndex = ldm.getObject().size();
        }
        return ldm.getObject().subList(first, toIndex).iterator();
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
     *            Album
     * @return IModel<Album>
     */
    public IModel<Album> model(Album object) {
        final Integer id = object.getId();
        return new LoadableDetachableModel<Album>() {
            @Override
            protected Album load() {
                return albumService.getById(id);
            }
        };
    }

}
