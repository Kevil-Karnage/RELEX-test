package ru.relex.rozhnovL.service;

import ru.relex.rozhnovL.entity.User;

public interface UserService {
    User save(User user);
    User getBySecretKey(String secretKey);
}
