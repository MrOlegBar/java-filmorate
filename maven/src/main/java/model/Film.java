package model;

import java.util.Date;

@lombok.Data
public class Film {
    private final int id;
    private final String name;
    private final String description;
    private final Date releaseDate;
    private final long duration;
}
