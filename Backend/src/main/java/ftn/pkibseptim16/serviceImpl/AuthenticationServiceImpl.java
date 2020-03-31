package ftn.pkibseptim16.serviceImpl;

import ftn.pkibseptim16.model.Admin;
import ftn.pkibseptim16.model.Authority;
import ftn.pkibseptim16.model.UserTokenState;
import ftn.pkibseptim16.repository.AuthorityRepository;
import ftn.pkibseptim16.security.TokenUtils;
import ftn.pkibseptim16.security.auth.JwtAuthenticationRequest;
import ftn.pkibseptim16.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public Set<Authority> findByName(String name) {
        Authority auth = this.authorityRepository.findByName(name);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(auth);
        return authorities;
    }

    @Override
    public Set<Authority> findById(Long id) {
        Authority auth = this.authorityRepository.getOne(id);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(auth);
        return authorities;
    }

    @Override
    public UserTokenState login(JwtAuthenticationRequest authenticationRequest) {
        final Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Admin admin = (Admin) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(admin.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();

        return new UserTokenState(jwt, expiresIn);
    }

}
