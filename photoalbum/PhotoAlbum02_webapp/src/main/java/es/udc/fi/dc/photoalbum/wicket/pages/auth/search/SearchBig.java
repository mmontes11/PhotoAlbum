package es.udc.fi.dc.photoalbum.wicket.pages.auth.search;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.image.resource.BlobImageResource;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import es.udc.fi.dc.photoalbum.hibernate.Album;
import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.hibernate.TagFile;
import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.AlbumService;
import es.udc.fi.dc.photoalbum.spring.FileService;
import es.udc.fi.dc.photoalbum.spring.TagService;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.wicket.BlobFromFile;
import es.udc.fi.dc.photoalbum.wicket.CustomNavigateForm;
import es.udc.fi.dc.photoalbum.wicket.MySession;
import es.udc.fi.dc.photoalbum.wicket.models.CustomFileModel;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.BasePageAuth;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.ErrorPage404;

/**
 */
@SuppressWarnings("serial")
public class SearchBig extends BasePageAuth {

    private int photoId;
    @SpringBean
    private UserService userService;
    @SpringBean
    private TagService tagService;
    @SpringBean
    private FileService fileService;
    @SpringBean
    private AlbumService albumService;
    private int tagId;
    private User user;
    CustomFileModel filesModel;
    private static final int ITEMS_PER_PAGE = 20;

    /**
     * Constructor for SearchBig.
     * 
     * @param parameters
     *            PageParameters
     */
    public SearchBig(PageParameters parameters) {
        super(parameters);
        if (parameters.getNamedKeys().contains("fid")
                && parameters.getNamedKeys().contains("tag")) {
            this.photoId = parameters.get("fid").toInt();
            this.user = userService.getById(((MySession) Session.get())
                    .getuId());
            tagId = parameters.get("tag").toInt();

            ArrayList<File> list;
            if (parameters.getNamedKeys().contains("album")
                    && parameters.getNamedKeys().contains("user")) {

                String name = parameters.get("album").toString();
                String ownerEmail = parameters.get("user").toString();
                User owner = new User(null, null, ownerEmail, null);
                Album album = albumService.getAlbum(name, userService
                        .getByEmail(owner).getId());
                list = tagService.getFiles(tagId, album.getId(), album
                        .getUser().getId());
                add(new CustomNavigateForm<Void>("formNavigate", tagId,
                        photoId, ownerEmail, list, SearchBig.class));
            } else {
                list = tagService.getAllFiles(tagId, user.getId());
                add(new CustomNavigateForm<Void>("formNavigate", tagId,
                        photoId, list, SearchBig.class));
            }

            filesModel = new CustomFileModel(photoId, list);
            add(createNonCachingImage());

            PageParameters pars = new PageParameters();
            pars.add("tag", parameters.get("tag").toInt(-1));
            add(new BookmarkablePageLink<Void>("linkBack", Search.class, (pars)));
            DataView<TagFile> tagDataView = createTagDataView();
            add(tagDataView);
            add(new PagingNavigator("navigator", tagDataView));
        } else {
            throw new RestartResponseException(ErrorPage404.class);
        }
    }

    /**
     * Method createNonCachingImage.
     * 
     * @return NonCachingImage
     */
    private NonCachingImage createNonCachingImage() {
        return new NonCachingImage("img", new BlobImageResource() {
            protected Blob getBlob(Attributes arg0) {
                return BlobFromFile.getBig(filesModel.getObject());
            }
        });
    }

    /**
     * Method createTagDataView.
     * 
     * @return DataView<TagFile>
     */
    private DataView<TagFile> createTagDataView() {
        final List<TagFile> list = new ArrayList<TagFile>(
                tagService.getTagsByFileId(photoId));
        DataView<TagFile> TagDataView = new DataView<TagFile>("tagPageable",
                new ListDataProvider<TagFile>(list)) {
            public void populateItem(final Item<TagFile> item) {
                final TagFile tagFile = item.getModelObject();
                PageParameters pars = new PageParameters();
                pars.add("tag", tagFile.getTag().getId());
                BookmarkablePageLink<Void> bp = new BookmarkablePageLink<Void>(
                        "tagsLink", Search.class, pars);
                bp.add(new Label("name", tagFile.getTag().getName()));
                item.add(bp);
                item.add(new Label("name", tagFile.getTag().getName()));
            }
        };
        TagDataView.setItemsPerPage(ITEMS_PER_PAGE);
        return TagDataView;
    }

}
