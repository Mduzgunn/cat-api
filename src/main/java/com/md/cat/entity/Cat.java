package com.md.cat.entity;

import com.md.cat.enums.ContentFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cat {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private String name;
    private ContentFormat contentFormat;
    private String value;
    private String path;
    private LocalDateTime  creationDate = LocalDateTime.now();

    public Cat(String name, ContentFormat contentFormat, String value, LocalDateTime  creationDate, String path) {
        this.name = name;
        this.contentFormat = contentFormat;
        this.value = value;
        this.creationDate = creationDate;
        this.path = path;
    }

}
