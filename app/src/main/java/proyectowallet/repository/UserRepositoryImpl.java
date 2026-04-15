package proyectowallet.repository;

import proyectowallet.model.User;
import java.util.*;

/**
 * Implementación en memoria del repositorio de usuarios.
 */
public class UserRepositoryImpl implements UserRepository {
    private final Map<String, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Usuario no puede ser nulo");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        return users.values().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public User update(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Usuario no puede ser nulo");
        }
        if (!users.containsKey(user.getId())) {
            throw new IllegalArgumentException("Usuario no existe");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean delete(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }
        return users.remove(id) != null;
    }

    @Override
    public long count() {
        return users.size();
    }
}
