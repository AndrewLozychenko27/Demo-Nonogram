package ua.lozychenko.nonogram.controller.composite;

import ua.lozychenko.nonogram.data.entity.User;

public class UserEditForm extends EditForm<User> {
    public UserEditForm(User source, User changes) {
        super(source, changes);
    }
}