package es.udc.fi.dc.photoalbum.rest.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.udc.fi.dc.photoalbum.spring.SearchService;

/**
 */
@Component
@Path("/search")
public class SearchResource {

    @Autowired
    private SearchService searchService;

    /**
     * @param num
     *            Número de recursos a devolver
     * 
     * @param keywordName
     *            Palabra clave del nombre del recurso
     * 
     * @param keywordComment
     *            Palabra clave de los comentarios del recurso
     * 
     * @param keywordTag
     *            Palabra clave de los tags del recurso
     * 
     * @param resource
     *            Tipo de recurso: 0->File ; 1->Album; 2->File y Album
     * 
     * @param hotCriterion
     *            Criterio hot: 0->Número de Likes; 1-> Número de Comentarios
     * 
     * @param orderCriterion
     *            Criterio de ordenación: 0-> Por el hotCriterion; 1-> Por fecha
     *            de creación del álbum/foto
     * 
     * @param initDate
     *            Fecha en la que se empezará a buscar el recurso. Formato:
     *            dd-MM-yyyy
     * 
     * @param endDate
     *            Fecha hasta la que se buscará el recurso. Formato: dd-MM-yyyy
     * 
     * @return List<FileDTO> o List<AlbumDTo> En función de el parámetro
     *         resource
     */
    @GET
    @SuppressWarnings("rawtypes")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public List search(@QueryParam("num") String numPicsString,
            @QueryParam("keywordName") String keywordName,
            @QueryParam("keywordComment") String keywordComment,
            @QueryParam("keywordTag") String keywordTag,
            @QueryParam("resource") String resourceString,
            @QueryParam("hotCriterion") String hotCriterionString,
            @QueryParam("orderCriterion") String orderCriterionString,
            @QueryParam("initDate") String initDateString,
            @QueryParam("endDate") String endDateString) {
        Integer numPics = numPicsString == null ? null : Integer
                .parseInt(numPicsString);
        if (numPics != null && numPics < 0) {
            numPics = null;
        }
        int hotCriterion = (hotCriterionString == null) ? 0 : Integer
                .parseInt(hotCriterionString);
        if (hotCriterion != 0 && hotCriterion != 1) {
            hotCriterion = 0;
        }
        int orderCriterion = (orderCriterionString == null) ? 0 : Integer
                .parseInt(orderCriterionString);
        if (orderCriterion != 0 && orderCriterion != 1) {
            orderCriterion = 0;
        }
        int resource = (resourceString == null) ? 0 : Integer
                .parseInt(resourceString);
        if (resource != 0 && resource != 1 && resource != 2) {
            resource = 0;
        }
        Calendar initDate = null;
        Calendar endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if (initDateString != null) {
            initDate = Calendar.getInstance();
            try {
                initDate.setTime(sdf.parse(initDateString));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (endDateString != null) {
            endDate = Calendar.getInstance();
            try {
                endDate.setTime(sdf.parse(endDateString));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return searchService.search(numPics, keywordName, keywordComment,
                keywordTag, resource, hotCriterion, orderCriterion, initDate,
                endDate);
    }
}