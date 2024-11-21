package com.chamod.ecommerce_backend.service;

import com.chamod.ecommerce_backend.api.model.RegistrationBody;
import com.chamod.ecommerce_backend.exception.UserAlreadyExistsException;
import com.chamod.ecommerce_backend.model.LocalUser;
import com.chamod.ecommerce_backend.model.dao.LocalUserDAO;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private LocalUserDAO localUserDAO;
    private EncryptionService encryptionService;

    public UserService(LocalUserDAO localUserDAO, EncryptionService encryptionService) {
        this.localUserDAO = localUserDAO;
        this.encryptionService = encryptionService;
    }

    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException {

        if (localUserDAO.findByEmailIsIgnoreCase(registrationBody.getEmail()).isPresent()
                || localUserDAO.findByUsernameIsIgnoreCase(registrationBody.getUsername()).isPresent()){
            throw new UserAlreadyExistsException();
        }

        LocalUser user = new LocalUser();
        user.setEmail(registrationBody.getEmail());
        user.setFirstName(registrationBody.getFirstName());
        user.setLastName(registrationBody.getLastName());
        user.setUsername(registrationBody.getUsername());

        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));

        return localUserDAO.save(user);

    }


}
