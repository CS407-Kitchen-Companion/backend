package org.cs407team7.KitchenCompanion.repository;

import org.cs407team7.KitchenCompanion.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(long id);

    Optional<User> findByUsername(String name);
}
