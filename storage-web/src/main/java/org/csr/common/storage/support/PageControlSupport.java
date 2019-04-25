package org.csr.common.storage.support;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author caijin
 *
 */
public class PageControlSupport {

    public final static String SESSION_ID_KEY = "com.elearning.app.PageControlSupport.id";
    public final static String SESSION_PASSED_URL = "com.cloverworxs.app.web.PageSequenceHelper.passedurl";
    public final static String ID_KEY = "pid";

    private static PageControlSupport instance = new PageControlSupport();

    private PageControlSupport() {
    }

    public static PageControlSupport getInstance() {
        return instance;
    }

    /**
     * Checks that the given page is of the correct sequence. Uses a request parameter of "pid" for the page id.
     * 
     * @throws PageSequenceException if it is not of the correct sequence
     */
    public void ensureSequence(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idString = request.getParameter(ID_KEY);
        if (idString != null && !idString.trim().equals("")) {
            ensureSequence(request, response, idString);
        }
    }

    /**
     * Returns true if there is a stored id, false if not. This can be used to see if if makes sense to call
     * ensureSequence.
     */
    public boolean hasStoredId(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        Id id = (Id) session.getAttribute(SESSION_ID_KEY);
        return id != null;
    }

    /**
     * Checks that the given page is of the correct sequence. If a POST method then will not accept the same id twice,
     * if a GET then thats OK as you might be doing a refresh. Checks using the specified page id value.
     * 
     * @param idString the id to use to make the check.
     * @throws PageSequenceException if it is not of the correct sequence
     */
    public void ensureSequence(HttpServletRequest request, HttpServletResponse response, String idString)
            throws IOException {
        boolean post = request.getMethod().equalsIgnoreCase("POST");
        setNoCache(response, request);
        Id id = getIdObject(request);
        synchronized (id) {
            int providedId = 0;

            if (idString != null) {
                try {
                    providedId = Integer.parseInt(idString);
                } catch (NumberFormatException e) {
                    // do nothing as it will default to 0
                }

                if (!(providedId == id.val || (!post && providedId == id.val - 1))) {
                    id.errorLastTime = true;
                    throw new IOException("Expected pid = " + id.val);
                }
                // If post request, not record the previous url
                if (!post) {
                    // store this recent url visited by user,if
                    // PageSequenceException not occur
                    StringBuffer sb = new StringBuffer();
                    sb.append(request.getRequestURI());
                    // as per ouyang's suggestion, add pid for null query string
                    String passedParameter = request.getQueryString();
                    if (passedParameter == null) {
                        sb.append("?pid=");
                        sb.append(providedId);
                    } else if (passedParameter.indexOf("pid=") == -1) {
                        sb.append("?pid=");
                        sb.append(providedId);
                        sb.append("&");
                        sb.append(passedParameter);
                    } else {
                        sb.append("?");
                        sb.append(passedParameter);
                    }
                    request.getSession().setAttribute(SESSION_PASSED_URL, sb.toString());
                }
            }

            // do not increment if id is last one as they have refreshed
            if (providedId != id.val - 1) {
                id.val++;
            }
        }

        // clear out error flag
        id.errorLastTime = false;
    }

    /**
     * suppress caching to get an immediate response seems to have no effect on netscape 7
     * 
     * @param request TODO
     */
    public static void setNoCache(HttpServletResponse response, HttpServletRequest request) {
        response.setHeader("Pragma", "No-cache");
        if (request.getProtocol().endsWith("1.0")) {
            // HTTP 1.0 header
            response.setHeader("Pragma", "No-cache");
            response.setDateHeader("Expires", 1L);
            // System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 365
        } else {
            // HTTP 1.1 header
            // FIXME I do not know why following line doesn't work!!!
            response.setHeader("Cache-Control", "no-cache");
            String dateOne = (new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", java.util.Locale.US))
                    .format(new Date(1));
            response.setHeader("Expires", dateOne);
        }
    }

    /**
     * suppress caching to get an immediate response At the same time allow d/l on IE. Note that if the no-cache header
     * is set, IE won't allow d/l of the content.
     */
    public static void setInstantExpire(HttpServletResponse response, HttpServletRequest request) {
        response.setHeader("Pragma", "No-cache");
        if (request.getProtocol().endsWith("1.0")) {
            // HTTP 1.0 header
            response.setHeader("Pragma", "No-cache");
            response.setDateHeader("Expires", 1L);
            // System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 365
        } else {
            // HTTP 1.1 header
            // FIXME I do not know why following line doesn't work!!!
            // response.setHeader("Cache-Control", "no-cache");
            String dateOne = (new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", java.util.Locale.US)) .format(new Date(1));
            response.setHeader("Expires", dateOne);
        }

    }

    public void setIdInRequestId(HttpServletRequest request) {
        request.setAttribute(ID_KEY, String.valueOf(getId(request)));
    }

    public int getId(HttpServletRequest request) {
        return getIdObject(request).val;
    }

    /**
     * Must be called before ensureSequence
     */
    public boolean isErrorLastTime(HttpServletRequest request) {
        return getIdObject(request).errorLastTime;
    }

    private Id getIdObject(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        Id id = (Id) session.getAttribute(SESSION_ID_KEY);
        if (id == null) {
            id = new Id();
            session.setAttribute(SESSION_ID_KEY, id);
        }
        return id;
    }

    public String appendPageId(HttpServletRequest request, String url) {
        String result = "";
        if (url.indexOf(ID_KEY) != -1) {
            int begin = url.indexOf(ID_KEY);
            int end = url.indexOf("&", begin);
            result = url.substring(0, begin - 1);
            if (end != -1) {
                result += url.substring(end);
            }
        } else {
            result = url;
        }
        StringBuffer buffer = new StringBuffer(result);
        buffer.append(getAppendChar(result) + ID_KEY + "=" + String.valueOf(getId(request)));
        return buffer.toString();
    }

    private String getAppendChar(String path) {
        int index = path.indexOf("?");
        return index < 0 ? "?" : "&";
    }

    /**
     * Wraps a mutable int in an object.
     */
    static class Id {

        private int val = 0;
        private boolean errorLastTime = false;

        /**
         * Returns the val.
         * 
         * @return int
         */
        public int getVal() {
            return val;
        }

        public String toString() {
            return String.valueOf(val);
        }

        /**
         * Returns the errorLastTime.
         * 
         * @return boolean
         */
        public boolean isErrorLastTime() {
            return errorLastTime;
        }

    }

}
