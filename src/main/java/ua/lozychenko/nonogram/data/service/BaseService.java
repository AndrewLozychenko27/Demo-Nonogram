package ua.lozychenko.nonogram.data.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BaseService<Entity> {
    Entity getById(Long id);

    List<Entity> getAll();

    Entity add(Entity entity);

    boolean delete(Long id);
}