package ru.relex.rozhnovL.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.relex.rozhnovL.entity.User;
import ru.relex.rozhnovL.repository.UserRepository;
import ru.relex.rozhnovL.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;

    @Override
    public User save(User user) {
        return repository.saveAndFlush(user);
    }

    @Override
    public User getBySecretKey(String secretKey) {
        return repository.getBySecretKey(secretKey);
    }
}
