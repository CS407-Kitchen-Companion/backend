package org.cs407team7.KitchenCompanion.repository;

import org.cs407team7.KitchenCompanion.entity.Folder;
import org.cs407team7.KitchenCompanion.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FolderRepository extends CrudRepository<Folder, Long> {
    Optional<Folder> findById(long id);
}
