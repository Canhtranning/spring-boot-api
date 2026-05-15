package com.example.api.repository;

import com.example.api.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User(null, "Nguyen Van A", "nguyenvana@example.com", "0123456789", "Ha Noi");
        user2 = new User(null, "Tran Thi B", "tranthib@example.com", "0987654321", "Ho Chi Minh");
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        // Given
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        // When
        List<User> users = userRepository.findAll();

        // Then
        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getName)
                .containsExactlyInAnyOrder("Nguyen Van A", "Tran Thi B");
    }

    @Test
    void findById_WhenUserExists_ShouldReturnUser() {
        // Given
        User savedUser = entityManager.persist(user1);
        entityManager.flush();

        // When
        Optional<User> found = userRepository.findById(savedUser.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Nguyen Van A");
        assertThat(found.get().getEmail()).isEqualTo("nguyenvana@example.com");
    }

    @Test
    void findById_WhenUserNotExists_ShouldReturnEmpty() {
        // When
        Optional<User> found = userRepository.findById(999L);

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void save_ShouldPersistUser() {
        // When
        User savedUser = userRepository.save(user1);

        // Then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("Nguyen Van A");
        assertThat(savedUser.getEmail()).isEqualTo("nguyenvana@example.com");

        User found = entityManager.find(User.class, savedUser.getId());
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Nguyen Van A");
    }

    @Test
    void delete_ShouldRemoveUser() {
        // Given
        User savedUser = entityManager.persist(user1);
        entityManager.flush();
        Long userId = savedUser.getId();

        // When
        userRepository.delete(savedUser);
        entityManager.flush();

        // Then
        User found = entityManager.find(User.class, userId);
        assertThat(found).isNull();
    }

    @Test
    void findByEmail_WhenUserExists_ShouldReturnUser() {
        // Given
        entityManager.persist(user1);
        entityManager.flush();

        // When
        Optional<User> found = userRepository.findByEmail("nguyenvana@example.com");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Nguyen Van A");
        assertThat(found.get().getEmail()).isEqualTo("nguyenvana@example.com");
    }

    @Test
    void findByEmail_WhenUserNotExists_ShouldReturnEmpty() {
        // When
        Optional<User> found = userRepository.findByEmail("notfound@example.com");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void existsByEmail_WhenEmailExists_ShouldReturnTrue() {
        // Given
        entityManager.persist(user1);
        entityManager.flush();

        // When
        boolean exists = userRepository.existsByEmail("nguyenvana@example.com");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_WhenEmailNotExists_ShouldReturnFalse() {
        // When
        boolean exists = userRepository.existsByEmail("notfound@example.com");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void save_WithDuplicateEmail_ShouldThrowException() {
        // Given
        entityManager.persist(user1);
        entityManager.flush();

        User duplicateUser = new User(null, "Another User", "nguyenvana@example.com", 
                                     "0999999999", "Da Nang");

        // When & Then
        assertThatThrownBy(() -> {
            userRepository.save(duplicateUser);
            entityManager.flush();
        }).isInstanceOf(Exception.class);
    }

    @Test
    void update_ShouldModifyExistingUser() {
        // Given
        User savedUser = entityManager.persist(user1);
        entityManager.flush();
        Long userId = savedUser.getId();

        // When
        savedUser.setName("Nguyen Van A Updated");
        savedUser.setEmail("nguyenvana.updated@example.com");
        savedUser.setPhone("0999999999");
        savedUser.setAddress("Da Nang");
        userRepository.save(savedUser);
        entityManager.flush();
        entityManager.clear();

        // Then
        User updatedUser = entityManager.find(User.class, userId);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isEqualTo("Nguyen Van A Updated");
        assertThat(updatedUser.getEmail()).isEqualTo("nguyenvana.updated@example.com");
        assertThat(updatedUser.getPhone()).isEqualTo("0999999999");
        assertThat(updatedUser.getAddress()).isEqualTo("Da Nang");
    }
}
