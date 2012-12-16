package de.otto.jsonhome.generator;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.net.URI;

/**
 * @author Sebastian Schroeder
 * @since 09.12.2012
 */
public final class JerseyHrefVarsGenerator extends HrefVarsGenerator {

    public JerseyHrefVarsGenerator(final URI relationTypeBaseUri) {
        super(relationTypeBaseUri);
    }

    /**
     * @param parameterInfo information about a method parameter.
     * @return true if the parameterInfo is describing a request parameter, false otherwise.
     */
    @Override
    protected boolean hasRequestParam(ParameterInfo parameterInfo) {
        return parameterInfo.hasAnnotation(QueryParam.class);
    }

    /**
     * @param parameterInfo information about a method parameter.
     * @return true if the parameterInfo is describing a path variable, false otherwise.
     */
    @Override
    protected boolean hasPathVariable(ParameterInfo parameterInfo) {
        return parameterInfo.hasAnnotation(PathParam.class);
    }

}
