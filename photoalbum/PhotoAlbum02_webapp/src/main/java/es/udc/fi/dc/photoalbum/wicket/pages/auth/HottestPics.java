package es.udc.fi.dc.photoalbum.wicket.pages.auth;

import java.sql.Blob;
import java.util.List;

import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.image.resource.BlobImageResource;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.rest.client.RestClient;
import es.udc.fi.dc.photoalbum.utils.File2FileDTOConversor;
import es.udc.fi.dc.photoalbum.utils.FileDTO;
import es.udc.fi.dc.photoalbum.wicket.AjaxDataView;
import es.udc.fi.dc.photoalbum.wicket.BlobFromFile;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.share.SharedBig;

/**
 */
@SuppressWarnings("serial")
public class HottestPics extends BasePageAuth {

    private RestClient restClient = new RestClient();
    private static final int ITEMS_PER_PAGE = 5;

    /**
     * Constructor for HottestPics.
     * 
     * @param parameters
     *            PageParameters
     */
    public HottestPics(PageParameters parameters) {
        super(parameters);
        add(new AjaxDataView("dataContainerFile", "navigator",
                createFileDataView()));
    }

    /**
     * Method createFileDataView.
     * 
     * @return DataView<File>
     */
    private DataView<File> createFileDataView() {
        // Obtenemos las 10 fotos con más likes
        List<FileDTO> list = restClient.search(10, null, null, null, 0);
        List<File> fileList = File2FileDTOConversor.toListFile(list);

        DataView<File> dataView = new DataView<File>("pageable",
                new ListDataProvider<File>(fileList)) {
            public void populateItem(final Item<File> item) {
                final File file = item.getModelObject();
                PageParameters pars = new PageParameters();
                pars.add("album", file.getAlbum().getName());
                pars.add("fid", Integer.toString(item.getModelObject().getId()));
                BookmarkablePageLink<Void> bpl = new BookmarkablePageLink<Void>(
                        "big", SharedBig.class, pars);
                bpl.add(new NonCachingImage("img", new BlobImageResource() {
                    protected Blob getBlob(Attributes arg0) {
                        return BlobFromFile.getSmall(item.getModelObject());
                    }
                }));
                item.add(bpl);
            }
        };
        dataView.setItemsPerPage(ITEMS_PER_PAGE);
        return dataView;
    }
}
