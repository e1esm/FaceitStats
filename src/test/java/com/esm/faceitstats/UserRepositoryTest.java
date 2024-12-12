package com.esm.faceitstats;


import com.esm.faceitstats.entity.Role;
import com.esm.faceitstats.entity.User;
import com.esm.faceitstats.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.Assert;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.Instant;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class UserRepositoryTest {
    @Container
    static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:15")
            .withDatabaseName("test")
            .withUsername("postgres")
            .withPassword("root");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",() -> String.format("jdbc:tc:postgresql://localhost:%s/%s", postgresqlContainer.getFirstMappedPort(), postgresqlContainer.getDatabaseName()));
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

    @Autowired
    private UserRepository repository;

    @AfterAll
    static void afterAll() {
        postgresqlContainer.stop();
    }

    @Test
    public void getSavedUser() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        user.setPassword("some-password");
        user.setRole(Role.ROLE_USER);
        user.setFaceitLink("https://www.faceit.com");
        user.setCreatedAt(Instant.now());

        var savedUser = repository.save(user);

        Assertions.assertThat(savedUser.getUsername()).isEqualTo(user.getUsername());
        Assert.notNull(savedUser.getId(), "Id should not be null!");
    }

    @Test
    public void getUserByUsernameFailure() {
        String username = "root";

        var u = this.repository.findByUsername(username);

        Assertions.assertThat(u).isEmpty();
    }

    @Test
    public void getUserByUsernameSuccess() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        user.setPassword("some-password");
        user.setRole(Role.ROLE_USER);
        user.setFaceitLink("https://www.faceit.com");
        user.setCreatedAt(Instant.now());

        var savedUser = repository.save(user);

        String username = "admin";
        var u = this.repository.findByUsername(username);
        Assertions.assertThat(u.get().getUsername()).isEqualTo(savedUser.getUsername());
    }

    @Test
    public void existsUserByUsernameSuccess() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        user.setPassword("some-password");
        user.setRole(Role.ROLE_USER);
        user.setFaceitLink("https://www.faceit.com");
        user.setCreatedAt(Instant.now());

        var savedUser = repository.save(user);
        var u = this.repository.existsByUsername(savedUser.getUsername());

        Assertions.assertThat(u).isTrue();
    }

    @Test
    public void existsUserByUsernameFailure() {
        String username = "hey";
        var u = this.repository.existsByUsername(username);

        Assertions.assertThat(u).isFalse();
    }

    @Test
    public void deleteBydIdSuccess() {
        Long id = 1L;

        this.repository.deleteById(id);

        var u = this.repository.findById(id);

        Assertions.assertThat(u).isEmpty();
    }

    @Test
    public void getAllUsersContainingUsernameSuccessEqualsTwo(){
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        user.setPassword("some-password");
        user.setRole(Role.ROLE_USER);
        user.setFaceitLink("https://www.faceit.com");
        user.setCreatedAt(Instant.now());

        User user1 = new User();
        user1.setUsername("admin-10");
        user1.setPassword("admin");
        user1.setRole(Role.ROLE_USER);
        user1.setFaceitLink("https://www.faceit/1.com");
        user1.setCreatedAt(Instant.now());

        User user2 = new User();
        user2.setUsername("elesm");
        user2.setPassword("some-password");
        user2.setRole(Role.ROLE_USER);
        user2.setFaceitLink("https://www.faceit/2.com");
        user2.setCreatedAt(Instant.now());

        repository.save(user);
        repository.save(user1);
        repository.save(user2);

        var users = this.repository.findByUsernameContaining("admin");

        Assertions.assertThat(users.size()).isEqualTo(2);
    }

    @Test
    public void getAllUsersContainingUsernameEqualsZero(){
        User user = new User();
        user.setUsername("toxic");
        user.setPassword("admin");
        user.setPassword("some-password");
        user.setRole(Role.ROLE_USER);
        user.setFaceitLink("https://www.faceit.com");
        user.setCreatedAt(Instant.now());

        User user1 = new User();
        user1.setUsername("veykl");
        user1.setPassword("admin");
        user1.setRole(Role.ROLE_USER);
        user1.setFaceitLink("https://www.faceit/1.com");
        user1.setCreatedAt(Instant.now());

        User user2 = new User();
        user2.setUsername("elesm");
        user2.setPassword("some-password");
        user2.setRole(Role.ROLE_USER);
        user2.setFaceitLink("https://www.faceit/2.com");
        user2.setCreatedAt(Instant.now());

        repository.save(user);
        repository.save(user1);
        repository.save(user2);

        var users = this.repository.findByUsernameContaining("admin");

        Assertions.assertThat(users.size()).isEqualTo(0);
    }

}
