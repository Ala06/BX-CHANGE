<<<<<<< Updated upstream
package atlantafx.sampler.services;public interface UserService {
=======
package atlantafx.sampler.services;

import atlantafx.sampler.entities.User;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    void createUser(User user)throws SQLException;

    User getUserById(int userId);


    List<User> getAllUsers();

    void updateUser(User user);


    void deleteUser(int userId);
>>>>>>> Stashed changes
}
