package ftn.pkibseptim16.service;

import ftn.pkibseptim16.dto.EntityDTO;
import ftn.pkibseptim16.exceptionHandler.InvalidEntityDataException;

import java.util.List;

public interface EntityService {
    EntityDTO createSubject(EntityDTO entityDTO) throws InvalidEntityDataException;

    List<EntityDTO> getAll();

    List<EntityDTO> getAllWithoutRootEntities();
}
