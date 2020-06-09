package ftn.pkibseptim16.exceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.ietf.jgss.GSSException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.text.ParseException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidCertificateDataException.class)
    protected ResponseEntity<Object> handleInvalidCertificateDataException(InvalidCertificateDataException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST);
        error.setMessage(ex.getMessage());
        return buildResponseEntity(error);
    }


    @ExceptionHandler(InvalidEntityDataException.class)
    protected ResponseEntity<Object> handleInvalidEntityDataException(InvalidEntityDataException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST);
        error.setMessage(ex.getMessage());
        return buildResponseEntity(error);
    }

    @ExceptionHandler(UnrecoverableKeyException.class)
    protected ResponseEntity<Object> handleUnrecoverableKeyException() {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST);
        error.setMessage("Wrong password for the private key. Access denied.");
        return buildResponseEntity(error);
    }

    @ExceptionHandler(IOException.class)
    protected ResponseEntity<Object> handleIOException() {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST);
        error.setMessage("Wrong password for the Key Store. Access denied.");
        return buildResponseEntity(error);
    }

    @ExceptionHandler({CertificateEncodingException.class, KeyStoreException.class, CertificateException.class,
            NoSuchAlgorithmException.class, NoSuchProviderException.class, CertificateEncodingException.class,
            CertificateParsingException.class, OperatorCreationException.class, CertIOException.class,
            InvalidAlgorithmParameterException.class, OCSPException.class, GSSException.class, CMSException.class})
    protected ResponseEntity<Object> handleCertificateAndKeyStoreException() {
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR);
        error.setMessage("Something went wrong. Please try again.");
        return buildResponseEntity(error);
    }

    @ExceptionHandler(ParseException.class)
    protected ResponseEntity<Object> handleParseException() {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST);
        error.setMessage("Date format is not valid.");
        return buildResponseEntity(error);
    }

    @ExceptionHandler(JsonProcessingException.class)
    protected ResponseEntity<Object> handleJsonProcessingException() {
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR);
        error.setMessage("Certificate status check failed. Please try again later.");
        return buildResponseEntity(error);
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorResponse error) {
        return new ResponseEntity<>(error, error.getStatus());
    }
}
