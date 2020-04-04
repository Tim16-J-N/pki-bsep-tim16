package ftn.pkibseptim16.repository;

import ftn.pkibseptim16.model.Entity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntityRepository extends JpaRepository<Entity, Long> {
    Entity findByEmail(String email);
    Entity findByCommonName(String commonName);
    List<Entity> findAll();
    List<Entity> findByNumberOfRootCertificatesEquals(Integer number);
}
