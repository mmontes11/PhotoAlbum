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
import es.udc.fi.dc.photoalbum.hibernate.Tag;
import es.udc.fi.dc.photoalbum.hibernate.TagAlbum;
import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.AlbumService;
import es.udc.fi.dc.photoalbum.spring.TagService;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.utils.Constant;
import es.udc.fi.dc.photoalbum.wicket.AjaxDataView;
import es.udc.fi.dc.photoalbum.wicket.BlobFromFile;
import es.udc.fi.dc.photoalbum.wicket.MySession;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.BasePageAuth;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.ErrorPage404;

/**
 */
@SuppressWarnings("serial")
public class SearchFiles extends BasePageAuth {

    @SpringBean
    private TagService tagService;
    @SpringBean
    private UserService userService;
    @SpringBean
    private AlbumService albumService;
    private Tag tag;
    private Album album;
    private User user;
    private User owner = null;
    private static final int ITEMS_PER_PAGE = 10;

    /**
     * Constructor for SearchFiles.
     * 
     * @param parameters
     *            PageParameters
     */
    public SearchFiles(PageParameters parameters) {
        super(parameters);
        if (parameters.getNamedKeys().contains("album")
                && parameters.getNamedKeys().contains("tag")) {
            String name = parameters.get("album").toString();
            int tagId = parameters.get("tag").toInt();

            this.user = userService.getById(((MySession) Session.get())
                    .getuId());
            this.owner = new User(null, null,
                    parameters.get("user").toString(), null);
            this.album = albumService.getAlbum(name,
                    userService.getByEmail(owner).getId());
            this.tag = tagService.getTagById(tagId);
        } else {
            throw new RestartResponseException(ErrorPage404.class);
        }

        add(new BookmarkablePageLink<Void>("linkBack", Search.class,
                (new PageParameters())
                        .add("tag", parameters.get("tag").toInt())));
        add(new AjaxDataView("dataContainerFile", "navigator",
                createFileDataView()));
        DataView<TagAlbum> TagsDataView = createTagsDataView();
        add(TagsDataView);
        add(new PagingNavigator("tagsNavigator", TagsDataView));
    }

    /**
     * Method createFileDataView.
     * 
     * @return DataView<File>
     */
    private DataView<File> createFileDataView() {
        ArrayList<File> list = tagService.getFiles(tag.getId(), album.getId(),
                user.getId());
        if (list.isEmpty()) {
            list = tagService.getFiles(tag.getId(), album.getId(),
                    Constant.getId());
        }

        DataView<File> dataView = new DataView<File>("pageable",
                new ListDataProvider<File>(list)) {
            public void populateItem(final Item<File> item) {
                final File file = item.getModelObject();
                PageParameters pars = new PageParameters();
                pars.add("album", file.getAlbum().getName());
                pars.add("fid", Integer.toString(item.getModelObject().getId()));
                pars.add("tag", tag.getId());
                if (owner == null) {
                    pars.add("user", user.getEmail());
                } else {
                    pars.add("user", owner.getEmail());
                }
                BookmarkablePageLink<Void> bpl = new BookmarkablePageLink<Void>(
                        "big", SearchBig.class, pars);
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

    /**
     * Method createTagsDataView.
     * 
     * @return DataView<TagAlbum>
     */
    private DataView<TagAlbum> createTagsDataView() {
        final List<TagAlbum> list = new ArrayList<TagAlbum>(
                tagService.getTagsByAbumId(this.album.getId()));
        DataView<TagAlbum> dataView = new DataView<TagAlbum>("tagsPageable",
                new ListDataProvider<TagAlbum>(list)) {

            public void populateItem(final Item<TagAlbum> item) {
                final TagAlbum tagAlbum = item.getModelObject();
                PageParameters pars = new PageParameters();
                pars.add("tag", tagAlbum.getTag().getId());
                BookmarkablePageLink<Void> bp = new BookmarkablePageLink<Void>(
                        "tagsLink", Search.class, pars);
                bp.add(new Label("name", tagAlbum.getTag().getName()));
                item.add(bp);
            }
        };
        dataView.setItemsPerPage(ITEMS_PER_PAGE);
        return dataView;
    }
}
