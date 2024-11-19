package com.chamod.ecommerce_backend.model.dao;

import com.chamod.ecommerce_backend.model.LocalUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LocalUserDAO extends CrudRepository<LocalUser,Long> {
    Optional<LocalUser> findByUsernameIsIgnoreCase(String username);

    Optional<LocalUser> findByEmailIsIgnoreCase(String email);

}
