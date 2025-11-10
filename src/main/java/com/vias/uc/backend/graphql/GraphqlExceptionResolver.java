package com.vias.uc.backend.graphql;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class GraphqlExceptionResolver extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        // Negocio / validación
        if (ex instanceof IllegalArgumentException || ex instanceof IllegalStateException) {
            return GraphqlErrorBuilder.newError(env)
                    .message(ex.getMessage()).errorType(ErrorType.BAD_REQUEST).build();
        }
        if (ex instanceof SecurityException) {
            return GraphqlErrorBuilder.newError(env)
                    .message(ex.getMessage()).errorType(ErrorType.FORBIDDEN).build();
        }
        // HTTP semantic
        if (ex instanceof ResponseStatusException rse) {
            return GraphqlErrorBuilder.newError(env)
                    .message(rse.getReason() != null ? rse.getReason() : rse.getStatusCode().toString())
                    .errorType(map(rse.getStatusCode())).build();
        }
        // DB / constraints
        if (ex instanceof DataIntegrityViolationException dive) {
            String msg = dive.getMostSpecificCause() != null ? dive.getMostSpecificCause().getMessage() : "Violación de integridad de datos";
            return GraphqlErrorBuilder.newError(env)
                    .message(msg).errorType(ErrorType.BAD_REQUEST).build();
        }
        return null; // deja pasar otros (si no querés, devuélvelos como INTERNAL_ERROR)
    }

    private static ErrorType map(HttpStatusCode s) {
        return switch (s.value()) {
            case 400 -> ErrorType.BAD_REQUEST;
            case 403 -> ErrorType.FORBIDDEN;
            case 404 -> ErrorType.NOT_FOUND;
            default -> ErrorType.INTERNAL_ERROR;
        };
    }
}
