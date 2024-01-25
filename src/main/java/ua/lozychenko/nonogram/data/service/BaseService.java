package ua.lozychenko.nonogram.data.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.lozychenko.nonogram.controller.composite.EditForm;

public interface BaseService<Entity> {
    Entity findById(Long id);

    Page<Entity> findAll(Pageable pageable);

    Entity add(Entity entity);

    Entity edit(EditForm<Entity> editForm);

    boolean delete(Long id);
}