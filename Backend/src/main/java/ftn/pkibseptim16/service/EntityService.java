package ftn.pkibseptim16.service;

import ftn.pkibseptim16.dto.EntityDTO;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.List;

public interface EntityService {
    EntityDTO createSubject(EntityDTO entityDTO) throws IllegalArgumentException, BadCredentialsException;

    List<EntityDTO> getAll();
}
