package ua.lozychenko.nonogram.data.service;

import ua.lozychenko.nonogram.controller.composite.EditForm;

import java.util.List;

public interface BaseService<Entity> {
    Entity getById(Long id);

    List<Entity> getAll();

    Entity add(Entity entity);

    Entity edit(EditForm<Entity> editForm);

    boolean delete(Long id);
}