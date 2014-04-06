package info.justaway.event;

import info.justaway.model.Row;

public class CreateFavoriteEvent {
    private final Row row;

    public CreateFavoriteEvent(Row row) {
        this.row = row;
    }

    public Row getRow() {
        return row;
    }
}
