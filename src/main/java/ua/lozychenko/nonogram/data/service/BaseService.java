package ua.lozychenko.nonogram.data.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaseService<Entity> {
    Entity findById(Long id);

    Page<Entity> findAll(Pageable pageable);

    Entity save(Entity entity);

    Entity edit(Entity source, Entity changes);

    boolean delete(Long id);
}