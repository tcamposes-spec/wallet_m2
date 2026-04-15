package proyectowallet.repository;

import proyectowallet.model.User;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz del repositorio para gestionar persistencia de usuarios.
 */
public interface UserRepository {
    /**
     * Guarda un usuario.
     * @param user usuario a guardar
     * @return usuario guardado
     */
    User save(User user);

    /**
     * Obtiene un usuario por ID.
     * @param id identificador del usuario
     * @return Optional con el usuario si existe
     */
    Optional<User> findById(String id);

    /**
     * Obtiene todos los usuarios.
     * @return lista de todos los usuarios
     */
    List<User> findAll();

    /**
     * Obtiene un usuario por email.
     * @param email email del usuario
     * @return Optional con el usuario si existe
     */
    Optional<User> findByEmail(String email);

    /**
     * Actualiza un usuario.
     * @param user usuario actualizado
     * @return usuario actualizado
     */
    User update(User user);

    /**
     * Elimina un usuario.
     * @param id identificador del usuario
     * @return true si se eliminó, false si no existe
     */
    boolean delete(String id);

    /**
     * Obtiene el total de usuarios.
     * @return cantidad de usuarios
     */
    long count();
}
