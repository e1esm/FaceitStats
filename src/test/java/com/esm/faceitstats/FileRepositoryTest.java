package com.esm.faceitstats;

import com.esm.faceitstats.entity.File;
import com.esm.faceitstats.repository.FileRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.assertj.core.api.Assertions;

import java.time.Instant;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class FileRepositoryTest {
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
    private FileRepository repository;

    @Test
    public void testSaveSuccess(){
        File file = File.builder()
                .fileName("first.txt")
                .createdAt(Instant.now())
                .build();

       var savedFile =  this.repository.save(file);
        Assertions.assertThat(savedFile).isNotNull();
        Assertions.assertThat(savedFile.getCreatedAt()).isNotNull();
        Assertions.assertThat(savedFile.getFileName()).isEqualTo(file.getFileName());
        Assertions.assertThat(savedFile.getId()).isGreaterThan(0);
    }

    @Test
    public void findByIdSuccess(){
        File file = File.builder()
                .fileName("first.txt")
                .createdAt(Instant.now())
                .build();

        var savedFile = this.repository.save(file);

        var foundFile = this.repository.findById(savedFile.getId());

        Assertions.assertThat(savedFile.getId()).isEqualTo(foundFile.get().getId());
        Assertions.assertThat(savedFile.getFileName()).isEqualTo(foundFile.get().getFileName());
    }

    @Test
    public void findByIdFailure(){
        File file = File.builder()
                .fileName("first.txt")
                .createdAt(Instant.now())
                .build();

        this.repository.save(file);

        var foundFile = this.repository.findById(2L);

        Assertions.assertThat(foundFile).isEmpty();
    }

    @Test
    public void deleteByPathSuccess(){
        File file = File.builder()
                .fileName("first.txt")
                .createdAt(Instant.now())
                .build();

        var savedFile = this.repository.save(file);

        this.repository.deleteByPath(savedFile.getFileName());

        var found = this.repository.findById(savedFile.getId());

        Assertions.assertThat(found).isNotEmpty();
    }

    @Test
    public void findAllSuccessEqualsThree(){
        File file = File.builder()
                .fileName("first.txt")
                .createdAt(Instant.now())
                .build();

        File file1 = File.builder()
                .fileName("second.txt")
                .createdAt(Instant.now())
                .build();

        File file2 = File.builder()
                .fileName("third.txt")
                .createdAt(Instant.now())
                .build();

        this.repository.save(file);
        this.repository.save(file1);
        this.repository.save(file2);

        var found = this.repository.findAll();

        Assertions.assertThat(found.size()).isEqualTo(3);
    }

    @Test
    public void findAllSuccessEqualsZero(){
        var found = this.repository.findAll();

        Assertions.assertThat(found.size()).isEqualTo(0);
    }


    @AfterAll
    static void afterAll() {
        postgresqlContainer.stop();
    }
}
