package es.udc.fi.dc.photoalbum.wicket.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

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
public class AlbumsModel extends LoadableDetachableModel<ArrayList<Album>> {

    @SpringBean
    private AlbumService albumService;
    private Album album;

    /**
     * Constructor for AlbumsModel.
     * 
     * @param album
     *            Album
     */
    public AlbumsModel(Album album) {
        this.album = album;
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
        Iterator<Album> itr = list.iterator();
        while (itr.hasNext()) {
            Album album = itr.next();
            if (album.getId().equals(this.album.getId())) {
                itr.remove();
                break;
            }
        }
        Collections.sort(list, new AlbumsComparator());
        return list;
    }
}
