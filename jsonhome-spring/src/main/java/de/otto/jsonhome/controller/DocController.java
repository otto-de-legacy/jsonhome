package de.otto.jsonhome.controller;

import org.markdown4j.Markdown4jProcessor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;

/**
 * Controller serving the /docs resource based on markdown documents.
 *
 * @author Guido Steinacker
 * @since 17.02.13
 */
@Controller
@RequestMapping("/docs")
public class DocController {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private Resource rootDir;

    public void setRootDir(final Resource rootDir) {
        this.rootDir = rootDir;
    }

    /**
     * Returns the requested /docs resource as html.
     * The returned content type is text/html.
     *
     * @param request the HttpServletRequest.
     * @return markdown of the requested document.
     * @throws IOException if the document does not exist.
     */
    @RequestMapping(
            value = "/**",
            method = {GET, HEAD},
            produces = {"text/html", "*/*"})
    @ResponseBody
    public String getMarkdownAsHtml(final HttpServletRequest request) throws IOException {
        return new Markdown4jProcessor().process(getSourceFor(request));
    }

    /**
     * Returns the requested /docs resource as markdown.
     * The returned content type is text/x-markdown.
     *
     * @param request the HttpServletRequest.
     * @return markdown of the requested document.
     * @throws IOException if the document does not exist.
     */
    @RequestMapping(
            value = "/**",
            method = {GET, HEAD},
            produces = {"text/x-markdown", "text/plain"})
    @ResponseBody
    public String getMarkdown(final HttpServletRequest request) throws IOException {
        return getSourceFor(request);
    }

    /**
     * Exception handler used to translate IOExceptions into HTTP 404 NOT FOUND.
     *
     * @param response the response object.
     * @throws IOException if sending the error fails for some reason.
     */
    @ResponseStatus(value = NOT_FOUND)
    @ExceptionHandler(IOException.class)
    public void handleNotFound(final HttpServletResponse response) throws IOException {
        response.sendError(NOT_FOUND.value(), "File not found");
    }

    /**
     * Returns the source code of the requested document.
     *
     * @param request the HttpServletRequest.
     * @return source of the documentation.
     * @throws IOException if the resource can not be found.
     */
    private String getSourceFor(final HttpServletRequest request) throws IOException {
        final String requestURI = request.getRequestURI().substring(request.getRequestURI().indexOf("/docs/") + 6);
        final InputStream file = rootDir.createRelative(requestURI).getInputStream();
        try {
            final BufferedReader reader = new BufferedReader( new InputStreamReader(file));
            String line;
            final StringBuilder  markdown = new StringBuilder();

            while((line = reader.readLine()) != null) {
                markdown.append(line);
                markdown.append(LINE_SEPARATOR);
            }

            return markdown.toString();
        } finally {
            file.close();
        }
    }

}
