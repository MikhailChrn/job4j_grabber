package ru.job4j.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {
    private int id;

    private String title;

    private String link;

    private String description;

    private LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(title, post.title)
                && Objects.equals(description, post.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description);
    }

    @Override
    public String toString() {
        return "title='" + title + '\''
                + ", link='" + link + '\''
                + ", created=" + created;
    }
}
