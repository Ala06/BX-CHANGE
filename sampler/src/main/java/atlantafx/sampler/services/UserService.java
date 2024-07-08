package atlantafx.sampler.services;

import atlantafx.sampler.entities.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(int userId);
    User getUserByUsername(String username);
    void addUser(User user);
}
