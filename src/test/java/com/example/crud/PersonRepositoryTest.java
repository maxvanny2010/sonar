package com.example.crud;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonRepositoryTest {
    private PersonRepository repository;

    @BeforeEach
    void setUp() {
        repository = new PersonRepository();
    }

    @Test
    void shouldCreateReadUpdateAndDeletePerson() {

        repository.add(new Person(1, "Alice", "alice@example.com"));
        assertEquals(1, repository.findAll().size());
        assertTrue(repository.findById(1).isPresent());

        assertTrue(repository.update(1, "Alice Smith", "alice.smith@example.com"));
        assertEquals("Alice Smith", repository.findById(1).orElseThrow().getName());

        assertTrue(repository.delete(1));
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void shouldRejectDuplicateIds() {
        repository.add(new Person(1, "Alice", "alice@example.com"));

        assertThrows(IllegalArgumentException.class,
                () -> repository.add(new Person(1, "Bob", "bob@example.com")));
    }

    @Test
    void shouldProtectRepositoryFromExternalChanges() {
        Person original = new Person(1, "Alice", "alice@example.com");

        repository.add(original);

        // Change original object after adding it
        original.setName("Changed outside");

        assertEquals(
                "Alice",
                repository.findById(1).orElseThrow().getName()
        );

        // Change object returned by findById()
        Person returnedPerson = repository.findById(1).orElseThrow();
        returnedPerson.setName("Changed through findById");

        assertEquals(
                "Alice",
                repository.findById(1).orElseThrow().getName()
        );

        // Change object returned by findAll()
        Person personFromList = repository.findAll().getFirst();
        personFromList.setName("Changed through findAll");

        assertEquals(
                "Alice",
                repository.findById(1).orElseThrow().getName()
        );
    }
}
