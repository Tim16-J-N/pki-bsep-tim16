package ftn.pkibseptim16.repository;

import ftn.pkibseptim16.model.OCSPItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OCSPRepository extends JpaRepository<OCSPItem, Long> {

    OCSPItem findBySerialNumber(String serialNumber);

}
