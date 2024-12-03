package com.chamod.ecommerce_backend.model.dao;

import com.chamod.ecommerce_backend.model.VerificationToken;
import org.springframework.data.repository.ListCrudRepository;

public interface VerificationTokenDAO extends ListCrudRepository<VerificationToken,Long> {
}
