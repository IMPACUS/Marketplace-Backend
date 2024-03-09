package com.impacus.maketplace.entity.category;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "super_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SuperCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "super_category_id")
    private Long id;

    @Column(nullable = false, length = 10)
    private String name;

    public SuperCategory(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
