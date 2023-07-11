package com.dhorbach.codingchallenge.exceptions.handlers;

import com.dhorbach.codingchallenge.exceptions.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import models.FailureResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.NotAcceptableStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

/**
 * Exception handler to handle specific exceptions and generate appropriate error responses.
 */
@Component
@Order(-2) //ensures that this handler is executed before the default error handling.
@RequiredArgsConstructor
public class ApplicationExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;

    /**
     * Handles the exception and generates the appropriate error response.
     *
     * @param exchange the server web exchange
     * @param ex       the thrown exception
     * @return a Mono representing the completion of the error handling
     */
    @NotNull
    @Override
    @SneakyThrows
    public Mono<Void> handle(ServerWebExchange exchange, @NotNull Throwable ex) {
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String errorMessage;
        HttpStatus httpStatus;
        if (ex instanceof NotAcceptableStatusException notAcceptableStatusException) {
            errorMessage = notAcceptableStatusException.getReason();
            httpStatus = HttpStatus.NOT_ACCEPTABLE;
        } else if (ex instanceof NotFoundException) {
            errorMessage = ex.getMessage();
            httpStatus = HttpStatus.NOT_FOUND;
        } else {
            return Mono.error(ex);
        }
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().writeWith(
            Mono.just(
                exchange.getResponse().bufferFactory().wrap(
                    generateFailureResponseJson(errorMessage, httpStatus).getBytes()
                )
            )
        );
    }

    /**
     * Generates a JSON representation of the FailureResponse object.
     *
     * @param message     the failure message
     * @param httpStatus  the HTTP status code
     * @return a JSON string representing the FailureResponse object
     * @throws com.fasterxml.jackson.core.JsonProcessingException if an error occurs during JSON serialization
     */
    private String generateFailureResponseJson(final String message, final HttpStatus httpStatus)
        throws JsonProcessingException {
        final var failureResponse = new FailureResponse()
            .status(httpStatus.value())
            .message(message);
        return objectMapper.writeValueAsString(failureResponse);
    }
}
