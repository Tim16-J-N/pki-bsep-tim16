package ftn.pkibseptim16.service;

import ftn.pkibseptim16.dto.EntityDTO;
import org.springframework.security.authentication.BadCredentialsException;

public interface EntityService {
    EntityDTO createSubject(EntityDTO entityDTO) throws IllegalArgumentException, BadCredentialsException;
}
