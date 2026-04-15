package proyectowallet.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase User.
 * Valida la creación y manipulación de usuarios.
 */
@DisplayName("Pruebas de User")
class UserTest {

    @Test
    @DisplayName("Debe crear usuario correctamente")
    void testCreateUser() {
        User user = new User("Carlos", "García", "carlos@example.com");

        assertNotNull(user.getId());
        assertEquals("Carlos", user.getFirstName());
        assertEquals("García", user.getLastName());
        assertEquals("carlos@example.com", user.getEmail());
        assertEquals("Carlos García", user.getFullName());
    }

    @Test
    @DisplayName("Debe actualizar datos del usuario")
    void testUpdateUserData() {
        User user = new User("Ana", "López", "ana@example.com");

        user.setFirstName("Angela");
        user.setLastName("López Martínez");
        user.setEmail("angela@example.com");

        assertEquals("Angela", user.getFirstName());
        assertEquals("López Martínez", user.getLastName());
        assertEquals("angela@example.com", user.getEmail());
    }

    @Test
    @DisplayName("Debe generar ID único para cada usuario")
    void testUserUniqueId() {
        User user1 = new User("Juan", "Pérez", "juan@example.com");
        User user2 = new User("María", "García", "maria@example.com");

        assertNotEquals(user1.getId(), user2.getId());
    }

    @Test
    @DisplayName("Debe retornar nombre completo correcto")
    void testGetFullName() {
        User user = new User("Jorge", "López Martínez", "jorge@example.com");

        assertEquals("Jorge López Martínez", user.getFullName());
    }
}
