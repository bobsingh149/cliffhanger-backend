package com.example.barter.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;
import java.util.UUID;


@Data
@Builder
public final class ProductDto {
    private final UUID id;
    private final String name;
    private final String description;
    private final String category;
    private final String image;
    private final int ranking;

    public ProductDto(UUID id, String name, String description, String category, String image, int ranking) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.image = image;
        this.ranking = ranking;
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public String category() {
        return category;
    }

    public String image() {
        return image;
    }

    public int ranking() {
        return ranking;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ProductDto) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.description, that.description) &&
                Objects.equals(this.category, that.category) &&
                Objects.equals(this.image, that.image) &&
                this.ranking == that.ranking;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, category, image, ranking);
    }

    @Override
    public String toString() {
        return "ProductDto[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "description=" + description + ", " +
                "category=" + category + ", " +
                "image=" + image + ", " +
                "ranking=" + ranking + ']';
    }

}

