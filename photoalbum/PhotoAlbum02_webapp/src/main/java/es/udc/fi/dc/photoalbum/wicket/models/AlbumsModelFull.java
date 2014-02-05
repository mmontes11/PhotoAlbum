package es.udc.fi.dc.photoalbum.wicket.models;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.wicket.Session;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import es.udc.fi.dc.photoalbum.hibernate.Album;
import es.udc.fi.dc.photoalbum.spring.AlbumService;
import es.udc.fi.dc.photoalbum.utils.AlbumsComparator;
import es.udc.fi.dc.photoalbum.wicket.MySession;

/**
 */
@SuppressWarnings("serial")
public class AlbumsModelFull extends LoadableDetachableModel<ArrayList<Album>> {

    @SpringBean
    private AlbumService albumService;

    /**
     * Constructor for AlbumsModelFull.
     */
    public AlbumsModelFull() {
        Injector.get().inject(this);
    }

    /**
     * Method load.
     * 
     * @return ArrayList<Album>
     */
    protected ArrayList<Album> load() {
        ArrayList<Album> list = new ArrayList<Album>(
                albumService.getAlbums(((MySession) Session.get()).getuId()));
        Collections.sort(list, new AlbumsComparator());
        return list;
    }
}
