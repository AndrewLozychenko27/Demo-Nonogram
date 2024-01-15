package ua.lozychenko.nonogram.data.service.impl;

import ua.lozychenko.nonogram.data.entity.Role;
import ua.lozychenko.nonogram.data.repo.RoleRepo;
import ua.lozychenko.nonogram.data.service.RoleService;

public class DefaultRoleService extends DefaultBaseService<Role> implements RoleService {
    public DefaultRoleService(RoleRepo repo) {
        super(repo);
    }
}