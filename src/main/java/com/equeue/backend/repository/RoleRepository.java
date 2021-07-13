package com.equeue.backend.repository;

import com.equeue.backend.models.Role;
import com.equeue.backend.models.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long>{

	Optional<RoleEntity> findByName(Role name);
}
