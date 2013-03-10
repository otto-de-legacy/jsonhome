/*
 * Copyright 2012 Guido Steinacker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.otto.jsonhome.controller;

import org.markdown4j.Markdown4jProcessor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;

/**
 * Controller serving the /docs resource based on markdown documents.
 * <p/>
 * The implementation makes use of the Markdown4j library.
 *
 * @author Guido Steinacker
 * @since 17.02.13
 * @see <a href="http://code.google.com/p/markdown4j">Markdown4j</a>
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
