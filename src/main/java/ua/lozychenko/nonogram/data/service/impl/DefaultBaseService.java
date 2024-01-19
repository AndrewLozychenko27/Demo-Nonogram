package ua.lozychenko.nonogram.data.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.lozychenko.nonogram.data.service.BaseService;

import java.util.List;

public abstract class DefaultBaseService<Entity> implements BaseService<Entity> {
    private JpaRepository<Entity, Long> repo;

    public DefaultBaseService(JpaRepository<Entity, Long> repo) {
        this.repo = repo;
    }

    @Override
    public Entity getById(Long id) {
        return repo.getReferenceById(id);
    }

    @Override
    public List<Entity> getAll() {
        return repo.findAll();
    }

    @Override
    public Entity add(Entity entity) {
        return repo.save(entity);
    }

    @Override
    public Entity edit(Entity source, Entity changes) {
        return repo.save(changes);
    }

    @Override
    public boolean delete(Long id) {
        boolean exists = repo.existsById(id);
        if (exists) {
            repo.deleteById(id);
        }
        return exists;
    }
}