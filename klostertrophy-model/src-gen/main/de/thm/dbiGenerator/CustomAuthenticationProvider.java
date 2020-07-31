package de.thm.dbiGenerator;

import de.thm.dbiGenerator.entities.Admin;
import de.thm.dbiGenerator.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.ArrayList;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    private AdminRepository adminRepository;

    public CustomAuthenticationProvider(AdminRepository adminRepository){
        this.adminRepository = adminRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        for(Admin admin : adminRepository.findAll()){
            if (admin.getUsername().equals(name) && admin.getPassword().equals(password)) {
                return new UsernamePasswordAuthenticationToken(
                        name, password, new ArrayList<>());
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
