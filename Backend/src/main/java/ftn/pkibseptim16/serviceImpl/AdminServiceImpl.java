package ftn.pkibseptim16.serviceImpl;

import ftn.pkibseptim16.model.Admin;
import ftn.pkibseptim16.model.Authority;
import ftn.pkibseptim16.repository.AdminRepository;
import ftn.pkibseptim16.service.AdminService;
import ftn.pkibseptim16.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class AdminServiceImpl implements UserDetailsService, AdminService {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AdminRepository adminRepository;

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void insertAfterStartup() {
        Set<Authority> authorities = authenticationService.findByName("ROLE_ADMIN");
        Admin admin = new Admin();
        admin.setUsername("pkiadmin");
        admin.setPassword("$2a$10$uIID/dN5b4ErGnIcSbO4lORSNMun1YD76OvhSfjmgNh/wgArbzxum"); // password: tim16
        admin.setAuthorities(authorities);
        if (findByUsername(admin.getUsername()) != null) {
            return;
        }

        adminRepository.save(admin);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = findByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return admin;
        }
    }

    @Override
    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }
}
