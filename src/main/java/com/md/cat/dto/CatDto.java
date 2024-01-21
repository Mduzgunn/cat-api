package com.md.cat.dto;

import com.md.cat.enums.ContentFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CatDto {
  public String id;
  public String name;
  public ContentFormat contentFormat;
  private String value;
  private String path;
  private LocalDateTime  creationDate;

  public CatDto(String id, String name, String value, String path, LocalDateTime  creationDate) {
    this.id = id;
    this.name = name;
    this.value = value;
    this.path = path;
    this.creationDate = creationDate;
  }
}
