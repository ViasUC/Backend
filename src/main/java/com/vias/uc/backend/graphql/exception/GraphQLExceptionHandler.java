package com.vias.uc.backend.graphql.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

/**
 * Manejador global de excepciones para GraphQL
 * Convierte RuntimeException con mensajes específicos en errores GraphQL legibles
 */
@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        String message = ex.getMessage();
        
        // Si el mensaje contiene códigos específicos, extraerlos
        if (message != null && message.contains("PENDIENTE_APROBACION:")) {
            // Extraer el mensaje limpio
            String cleanMessage = message.replace("PENDIENTE_APROBACION:", "").trim();
            
            return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.UNAUTHORIZED)
                .message("PENDIENTE_APROBACION:" + cleanMessage)
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
        }
        
        // Para otras RuntimeException, devolver el mensaje tal cual
        if (ex instanceof RuntimeException) {
            return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message(message != null ? message : "Error en la operación")
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
        }
        
        // Para el resto, dejar que Spring lo maneje
        return null;
    }
}
