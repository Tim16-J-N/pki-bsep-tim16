package ftn.pkibseptim16.service;

import ftn.pkibseptim16.model.Authority;
import ftn.pkibseptim16.model.UserTokenState;
import ftn.pkibseptim16.security.auth.JwtAuthenticationRequest;

import java.util.Set;

public interface AuthenticationService {
    Set<Authority> findByName(String name);

    Set<Authority> findById(Long id);

    UserTokenState login(JwtAuthenticationRequest authenticationRequest);
}
