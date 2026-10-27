package com.japim.services;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails; // Import obrigatório!
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.japim.exceptions.defaults.EntityDontFindException;
import com.japim.repositories.UserRepository;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepo;

    public AuthService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    @Transactional(readOnly = true) // ADICIONE ISSO AQUI
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Usando o parâmetro 'username' recebido e a sua exception personalizada
        com.japim.models.User user = userRepo.findByEmail(username) // Ou findByEmail(username) dependendo da sua regra
                .orElseThrow(() -> new EntityDontFindException(null, "Usuário não encontrado: " + username));

        var authorities = user.getRole().getPermissions().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());

        return new User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}