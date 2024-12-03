package com.chamod.ecommerce_backend.service;

import com.chamod.ecommerce_backend.api.model.LoginBody;
import com.chamod.ecommerce_backend.api.model.RegistrationBody;
import com.chamod.ecommerce_backend.exception.EmailFailureException;
import com.chamod.ecommerce_backend.exception.UserAlreadyExistsException;
import com.chamod.ecommerce_backend.model.LocalUser;
import com.chamod.ecommerce_backend.model.VerificationToken;
import com.chamod.ecommerce_backend.model.dao.LocalUserDAO;
import com.chamod.ecommerce_backend.model.dao.VerificationTokenDAO;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class UserService {

    private LocalUserDAO localUserDAO;
    private EncryptionService encryptionService;
    private JWTService jwtService;
    private EmailService emailService;
    private VerificationTokenDAO verificationTokenDAO;

    public UserService(LocalUserDAO localUserDAO, EncryptionService encryptionService, JWTService jwtService, VerificationTokenDAO verificationTokenDAO) {
        this.localUserDAO = localUserDAO;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
        this.verificationTokenDAO = verificationTokenDAO;
    }

    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException, EmailFailureException {

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
        VerificationToken verificationToken = createVerificationToken(user);
        emailService.sendVerificationEmail(verificationToken);
        verificationTokenDAO.save(verificationToken);
        return localUserDAO.save(user);

    }



    public String loginUser(LoginBody loginBody){
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIsIgnoreCase(loginBody.getUsername());

        if(opUser.isPresent()){
            LocalUser user = opUser.get();
            if(encryptionService.verifyPassword(loginBody.getPassword(),user.getPassword())){
                return jwtService.generateJWT(user);
            }
        }
        return null;
    }

    private VerificationToken createVerificationToken(LocalUser user){
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateVerificationJWT(user));
        verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);
        return verificationToken;
    }


}
