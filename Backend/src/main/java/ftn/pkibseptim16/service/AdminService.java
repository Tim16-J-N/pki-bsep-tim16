package ftn.pkibseptim16.service;

import ftn.pkibseptim16.model.Admin;

public interface AdminService {
    Admin findByUsername(String username);
}
