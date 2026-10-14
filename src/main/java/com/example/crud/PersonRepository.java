package com.example.crud;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonRepository {
    private final List<Person> people = new ArrayList<>();

    public void add(Person person) {
        if (findById(person.getId()).isPresent()) {
            throw new IllegalArgumentException("A person with this ID already exists.");
        }
        people.add(new Person(person));
    }

    public List<Person> findAll() {
        return people.stream().map(Person::new).toList();
    }

    public Optional<Person> findById(int id) {
        return findInternalById(id).map(Person::new);
    }

    private Optional<Person> findInternalById(int id) {
        return people.stream()
                .filter(person -> person.getId() == id)
                .findFirst();
    }

    public boolean update(int id, String name, String email) {
        Optional<Person> optional = findInternalById(id);

        if (optional.isEmpty()) return false;

        Person person = optional.get();
        person.setName(name);
        person.setEmail(email);

        return true;
    }

    public boolean delete(int id) {
        return people.removeIf(person -> person.getId() == id);
    }

    public void clear() {
        people.clear();
    }
}
