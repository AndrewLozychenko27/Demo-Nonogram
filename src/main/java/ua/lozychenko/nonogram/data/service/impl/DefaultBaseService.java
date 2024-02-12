package ua.lozychenko.nonogram.data.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.lozychenko.nonogram.data.service.BaseService;

public abstract class DefaultBaseService<Entity> implements BaseService<Entity> {
    private final JpaRepository<Entity, Long> repo;

    public DefaultBaseService(JpaRepository<Entity, Long> repo) {
        this.repo = repo;
    }

    @Override
    public Entity findById(Long id) {
        return repo.getReferenceById(id);
    }

    @Override
    public Page<Entity> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public Entity save(Entity entity) {
        return repo.save(entity);
    }

    @Override
    public Entity edit(Entity source, Entity changes) {
        return repo.save(source);
    }

    @Override
    public boolean delete(Long id) {
        boolean exists = repo.existsById(id);
        if (exists) {
            repo.deleteById(id);
        }
        return exists;
    }

    protected <T> T getIfChanged(T source, T changes) {
        return (changes != null && !source.equals(changes)) ? changes : source;
    }
}