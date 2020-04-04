package ftn.pkibseptim16.serviceImpl;

import ftn.pkibseptim16.dto.EntityDTO;
import ftn.pkibseptim16.enumeration.EntityType;
import ftn.pkibseptim16.model.Entity;
import ftn.pkibseptim16.repository.EntityRepository;
import ftn.pkibseptim16.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EntityServiceImpl implements EntityService {

    @Autowired
    private EntityRepository entityRepository;

    @Override
    public EntityDTO createSubject(EntityDTO entityDTO) throws IllegalArgumentException,BadCredentialsException{
        isSubjectValid(entityDTO);
        EntityType type = EntityType.valueOf(entityDTO.getType().toUpperCase());

        Entity subject = new Entity(type,entityDTO.getCommonName(),entityDTO.getEmail(),entityDTO.getOrganizationUnitName(),
                entityDTO.getOrganization(),entityDTO.getCountryCode(),entityDTO.getSurname(),entityDTO.getGivename(),
                entityDTO.getLocalityName(),entityDTO.getState());

        return new EntityDTO(entityRepository.save(subject));
    }

    @Override
    public List<EntityDTO> getAll() {
        return convertToDTO(entityRepository.findAll());
    }

    @Override
    public List<EntityDTO> getAllWithoutRootEntities() {
        return convertToDTO(entityRepository.findByNumberOfRootCertificatesEquals(0));
    }

    private List<EntityDTO> convertToDTO(List<Entity> entities){
        List<EntityDTO> entityDTOS =new ArrayList<>();
        for (Entity entity:entities) {
            entityDTOS.add(new EntityDTO(entity));
        }
        return entityDTOS;
    }

    private boolean isSubjectValid(EntityDTO entityDTO) throws IllegalArgumentException,BadCredentialsException{
        EntityType type = EntityType.valueOf(entityDTO.getType().toUpperCase());
        if(entityRepository.findByCommonName(entityDTO.getCommonName()) != null){
            throw new BadCredentialsException("Subject with same common name already exist");
        }
        if(type.toString() == "USER"){
            if(entityDTO.getEmail().isEmpty() || entityDTO.getSurname().isEmpty() || entityDTO.getGivename().isEmpty()){
                throw new BadCredentialsException("Some data are missing");
            }
            if(entityRepository.findByEmail(entityDTO.getEmail())!=null){
                throw new BadCredentialsException("Subject with same email address already exist");
            }
        }else{
            if(entityDTO.getOrganizationUnitName().isEmpty() || entityDTO.getLocalityName().isEmpty() || entityDTO.getState().isEmpty()){
                throw new BadCredentialsException("Some data are missing");
            }
        }
        return true;
    }


}
