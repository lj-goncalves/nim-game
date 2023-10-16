/**
 * Copyright 2002-2023 MULTICERT S.A.
 * All rights reserved.
 *
 * This source is provided for inspection purposes and recompilation only,
 * unless specified differently in a contract with MULTICERT S.A.. This
 * source has to be kept in strict confidence and must not be disclosed to any
 * third party under any circumstances. Redistribution in source and binary
 * forms, with or without modification, are NOT permitted in any case!
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * Should any term or condition of this representation be deemed invalid or
 * inefficient, it will not affect the validity and efficiency of the remainder
 * of the Contract.
 */
package com.coding.kata.demo.project.controller;

import com.coding.kata.demo.project.dto.ErrorResponseDTO;
import com.coding.kata.demo.project.exceptions.NimGameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static com.coding.kata.demo.project.enums.ApiErrorCode.INVALID_PARAMETER;
import static com.coding.kata.demo.project.enums.ApiErrorCode.UNEXPECTED_ERROR;


@RestControllerAdvice(basePackages = "com.coding.kata.demo.project.controller")
public class ResourceAdvisor {
    private static final Logger LOG = LoggerFactory.getLogger(ResourceAdvisor.class.getSimpleName());

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NimGameException.class)
    @ResponseBody
    public ErrorResponseDTO handleBusinessError(NimGameException ex) {

        return new ErrorResponseDTO(ex.getMessage(), ex.getErrorCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponseDTO handleInvalidParameter(MethodArgumentNotValidException ex) {

        return new ErrorResponseDTO(INVALID_PARAMETER.getMessage() + ": "
                + Objects.requireNonNull(ex.getBindingResult().getFieldError()).getField() + ", "
                + ex.getBindingResult().getFieldError().getDefaultMessage(), INVALID_PARAMETER.getCode());
    }
    
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorResponseDTO handleSystemError(Exception ex) {

        LOG.error("API Handler: Unexpected error occurred", ex);
        return new ErrorResponseDTO(UNEXPECTED_ERROR.getMessage(), UNEXPECTED_ERROR.getCode());
    }

}
