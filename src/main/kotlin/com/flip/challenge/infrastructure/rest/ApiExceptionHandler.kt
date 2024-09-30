package com.flip.challenge.infrastructure.rest

import com.flip.challenge.domain.URLNotFound
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.net.MalformedURLException


@ControllerAdvice
class ApiExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleGenericException(ex: Exception, request: WebRequest): ResponseEntity<Any>? {
        return handleExceptionInternal(
            ex,
            ex.message,
            HttpHeaders(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            request
        )
    }

    @ExceptionHandler(URLNotFound::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleURLNotFound(ex: URLNotFound, request: WebRequest): ResponseEntity<Any>? {
        return handleExceptionInternal(
            ex,
            ex.message,
            HttpHeaders(),
            HttpStatus.NOT_FOUND,
            request
        )
    }

    @ExceptionHandler(MalformedURLException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMalformedURLException(ex: MalformedURLException, request: WebRequest): ResponseEntity<Any>? {
        return handleExceptionInternal(
            ex,
            "The provided URL is malformed",
            HttpHeaders(),
            HttpStatus.BAD_REQUEST,
            request
        )
    }
}