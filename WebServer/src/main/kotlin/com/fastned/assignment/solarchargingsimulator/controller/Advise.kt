package com.fastned.assignment.solarchargingsimulator.controller

import com.fastned.assignment.solarchargingsimulator.rest.model.ErrorDto
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class Advise {
    @ExceptionHandler(Throwable::class)
    fun handleException(ex: HttpMessageNotReadableException): ResponseEntity<ErrorDto> {
        println(ex)
        val response = ErrorDto(code = "422", message = "Invalid message sent")
        return ResponseEntity.unprocessableEntity().body(response)
    }
}