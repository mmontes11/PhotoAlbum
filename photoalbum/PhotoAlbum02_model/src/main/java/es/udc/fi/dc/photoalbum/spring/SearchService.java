package es.udc.fi.dc.photoalbum.spring;

import java.util.Calendar;
import java.util.List;

import es.udc.fi.dc.photoalbum.utils.FileDTO;

/**
 */
public interface SearchService {

    /**
     * Method search.
     * 
     * @param numPics
     *            Integer
     * @param keywordName
     *            String
     * @param keywordComment
     *            String
     * @param keywordTag
     *            String
     * @param resource
     *            int
     * @param hotCriterion
     *            int
     * @param orderCriterion
     *            int
     * @param initDate
     *            Calendar
     * @param endDate
     *            Calendar
     * @return List<FileDTO>
     */
    public List<FileDTO> search(Integer numPics, String keywordName,
            String keywordComment, String keywordTag, int resource,
            int hotCriterion, int orderCriterion, Calendar initDate,
            Calendar endDate);
}
