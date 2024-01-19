package ua.lozychenko.nonogram.controller.composite;

public class EditForm<T> {
    private T source;
    private T changes;

    public EditForm(T source, T changes) {
        this.source = source;
        this.changes = changes;
    }

    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }

    public T getChanges() {
        return changes;
    }

    public void setChanges(T changes) {
        this.changes = changes;
    }
}
