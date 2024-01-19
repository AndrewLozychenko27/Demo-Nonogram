package ua.lozychenko.nonogram.data.service;

import java.util.List;

public interface BaseService<Entity> {
    Entity getById(Long id);

    List<Entity> getAll();

    Entity add(Entity entity);

    Entity edit(Entity source, Entity changes);

    boolean delete(Long id);
}