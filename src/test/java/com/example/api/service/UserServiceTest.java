package com.example.api.service;

import com.example.api.model.User;
import com.example.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User(1L, "Nguyen Van A", "nguyenvana@example.com", "0123456789", "Ha Noi");
        user2 = new User(2L, "Tran Thi B", "tranthib@example.com", "0987654321", "Ho Chi Minh");
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Given
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // When
        List<User> users = userService.getAllUsers();

        // Then
        assertThat(users).hasSize(2);
        assertThat(users).containsExactly(user1, user2);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        // When
        Optional<User> result = userService.getUserById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user1);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_WhenUserNotExists_ShouldReturnEmpty() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.getUserById(999L);

        // Then
        assertThat(result).isEmpty();
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    void createUser_WithValidData_ShouldSaveAndReturnUser() {
        // Given
        User newUser = new User(null, "Le Van C", "levanc@example.com", "0111222333", "Can Tho");
        User savedUser = new User(3L, "Le Van C", "levanc@example.com", "0111222333", "Can Tho");
        
        when(userRepository.existsByEmail("levanc@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        User result = userService.createUser(newUser);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("Le Van C");
        assertThat(result.getEmail()).isEqualTo("levanc@example.com");
        verify(userRepository, times(1)).existsByEmail("levanc@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_WithDuplicateEmail_ShouldThrowException() {
        // Given
        User newUser = new User(null, "Test", "nguyenvana@example.com", "0123456789", "Ha Noi");
        when(userRepository.existsByEmail("nguyenvana@example.com")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.createUser(newUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Email already exists");
        
        verify(userRepository, times(1)).existsByEmail("nguyenvana@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_WhenUserExists_ShouldUpdateAndReturnUser() {
        // Given
        User updatedData = new User(null, "Nguyen Van A Updated", "nguyenvana.updated@example.com", 
                                   "0999999999", "Da Nang");
        User existingUser = new User(1L, "Nguyen Van A", "nguyenvana@example.com", "0123456789", "Ha Noi");
        User updatedUser = new User(1L, "Nguyen Van A Updated", "nguyenvana.updated@example.com", 
                                   "0999999999", "Da Nang");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When
        User result = userService.updateUser(1L, updatedData);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Nguyen Van A Updated");
        assertThat(result.getEmail()).isEqualTo("nguyenvana.updated@example.com");
        assertThat(result.getPhone()).isEqualTo("0999999999");
        assertThat(result.getAddress()).isEqualTo("Da Nang");
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_WhenUserNotExists_ShouldThrowException() {
        // Given
        User updatedData = new User(null, "Test", "test@example.com", "0123456789", "Ha Noi");
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.updateUser(999L, updatedData))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found with id: 999");
        
        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        doNothing().when(userRepository).delete(user1);

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(user1);
    }

    @Test
    void deleteUser_WhenUserNotExists_ShouldThrowException() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.deleteUser(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found with id: 999");
        
        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void getUserByEmail_WhenUserExists_ShouldReturnUser() {
        // Given
        when(userRepository.findByEmail("nguyenvana@example.com")).thenReturn(Optional.of(user1));

        // When
        Optional<User> result = userService.getUserByEmail("nguyenvana@example.com");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user1);
        assertThat(result.get().getEmail()).isEqualTo("nguyenvana@example.com");
        verify(userRepository, times(1)).findByEmail("nguyenvana@example.com");
    }

    @Test
    void getUserByEmail_WhenUserNotExists_ShouldReturnEmpty() {
        // Given
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.getUserByEmail("notfound@example.com");

        // Then
        assertThat(result).isEmpty();
        verify(userRepository, times(1)).findByEmail("notfound@example.com");
    }
}
