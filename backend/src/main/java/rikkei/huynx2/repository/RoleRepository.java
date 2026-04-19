package rikkei.huynx2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rikkei.huynx2.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    
}
