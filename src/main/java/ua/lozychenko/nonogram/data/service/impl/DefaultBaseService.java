package ua.lozychenko.nonogram.data.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.lozychenko.nonogram.controller.composite.EditForm;
import ua.lozychenko.nonogram.data.service.BaseService;

public abstract class DefaultBaseService<Entity> implements BaseService<Entity> {
    private JpaRepository<Entity, Long> repo;

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
    public Entity edit(EditForm<Entity> editForm) {
        return repo.save(editForm.getSource());
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