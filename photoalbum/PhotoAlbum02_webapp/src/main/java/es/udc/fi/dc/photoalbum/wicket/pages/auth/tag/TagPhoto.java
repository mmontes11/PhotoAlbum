package es.udc.fi.dc.photoalbum.wicket.pages.auth.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.dao.DataIntegrityViolationException;

import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.hibernate.Tag;
import es.udc.fi.dc.photoalbum.hibernate.TagFile;
import es.udc.fi.dc.photoalbum.spring.FileService;
import es.udc.fi.dc.photoalbum.spring.TagService;
import es.udc.fi.dc.photoalbum.utils.Validator;
import es.udc.fi.dc.photoalbum.wicket.MyAjaxButton;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.BasePageAuth;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.ErrorPage404;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.search.Search;

/**
 */
@SuppressWarnings("serial")
public class TagPhoto extends BasePageAuth {
    @SpringBean
    private TagService tagService;
    @SpringBean
    private FileService fileService;
    private PageParameters parameters;
    private File file = null;
    private static final int ITEMS_PER_PAGE = 20;

    /**
     * Constructor for TagPhoto.
     * 
     * @param parameters
     *            PageParameters
     */
    public TagPhoto(final PageParameters parameters) {
        super(parameters);
        this.parameters = parameters;
        if (parameters.getNamedKeys().contains("fid")) {
            String id = parameters.get("fid").toString();
            add(new Label("fid", id));
            this.file = fileService.getById(Integer.valueOf(id));
            if (file == null
            /*
             * || (file != null && file.getAlbum().getUser().getId() !=
             * ((MySession) Session .get()).getuId())
             */) {
                throw new RestartResponseException(ErrorPage404.class);
            }
        }
        add(createShareForm());
        DataView<TagFile> dataView = createShareDataView();
        add(dataView);
        add(new PagingNavigator("navigator", dataView));
    }

    /**
     * Method createShareDataView.
     * 
     * @return DataView<TagFile>
     */
    private DataView<TagFile> createShareDataView() {
        final List<TagFile> list = new ArrayList<TagFile>(
                tagService.getTagsByFileId(this.file.getId()));
        DataView<TagFile> dataView = new DataView<TagFile>("pageable",
                new ListDataProvider<TagFile>(list)) {

            public void populateItem(final Item<TagFile> item) {
                final TagFile tagFile = item.getModelObject();
                PageParameters pars = new PageParameters();
                pars.add("tag", tagFile.getTag().getId());
                BookmarkablePageLink<Void> bp = new BookmarkablePageLink<Void>(
                        "tagsLink", Search.class, pars);
                bp.add(new Label("name", tagFile.getTag().getName()));
                item.add(bp);
                item.add(new Link<Void>("delete") {
                    public void onClick() {
                        tagService.delete(tagFile);
                        info(new StringResourceModel("tag.deleted", this, null)
                                .getString());
                        setResponsePage(new TagPhoto(parameters));
                    }
                });
            }
        };
        dataView.setItemsPerPage(ITEMS_PER_PAGE);
        return dataView;
    }

    /**
     * Method createShareForm.
     * 
     * @return Form<Tag>
     */
    private Form<Tag> createShareForm() {
        Form<Tag> form = new Form<Tag>("form") {
            @Override
            protected void onSubmit() {
                Tag tag = getModelObject();

                String tagName = tag.getName().toLowerCase();
                Tag existedTag = null;
                if (Validator.isUsernameValid(tagName)) {
                    existedTag = tagService.getTagbyName(tagName);
                } else {
                    error(new StringResourceModel("validator.characterError",
                            this, null).getString());
                    return;
                }

                if (existedTag == null) {
                    Tag newTag = new Tag(null, tagName);
                    tagService.create(newTag);
                    tagService.create(new TagFile(null, newTag, file));
                } else {
                    try {
                        tagService.create(new TagFile(null, existedTag, file));
                    } catch (DataIntegrityViolationException e) {
                        Logger.getLogger(TagPhoto.class.getName()).log(
                                Level.WARNING, e.toString(), e);
                        error(new StringResourceModel("tag.alreadyCreated",
                                this, null).getString());
                    }
                }
                setResponsePage(new TagPhoto(parameters));
            }
        };
        Tag tag = new Tag();
        form.setDefaultModel(new Model<Tag>(tag));
        RequiredTextField<String> tagName = new RequiredTextField<String>(
                "TagName", new PropertyModel<String>(tag, "name"));
        tagName.setLabel(new StringResourceModel("tag.nameField", this, null));
        form.add(tagName);
        FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        form.add(feedback);
        form.add(new MyAjaxButton("ajax-button", form, feedback));
        return form;
    }
}
