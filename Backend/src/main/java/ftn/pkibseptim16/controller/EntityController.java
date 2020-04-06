package ftn.pkibseptim16.controller;

import ftn.pkibseptim16.dto.EntityDTO;
import ftn.pkibseptim16.exceptionHandler.InvalidEntityDataException;
import ftn.pkibseptim16.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/entity")
public class EntityController {

    @Autowired
    private EntityService entityService;

    @PostMapping(value = "/create-subject", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityDTO> createSubject(@Valid @RequestBody EntityDTO entityDTO) throws InvalidEntityDataException {
        return new ResponseEntity<>(entityService.createSubject(entityDTO), HttpStatus.CREATED);
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EntityDTO>> getAll() {
        return new ResponseEntity<>(entityService.getAll(), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EntityDTO>> getAllWithoutRootEntities() {
        return new ResponseEntity<>(entityService.getAllWithoutRootEntities(), HttpStatus.OK);
    }

}
