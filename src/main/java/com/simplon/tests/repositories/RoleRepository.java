package com.simplon.tests.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.simplon.tests.entities.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, String> {

}