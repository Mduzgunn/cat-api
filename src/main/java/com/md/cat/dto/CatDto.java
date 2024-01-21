package com.md.cat.dto;

import com.md.cat.enums.ContentFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CatDto {
  public String id;
  public String name;
  public ContentFormat contentFormat;
  public String imageUrl; // ?????
  // public String textValue;
   // public int customValueWidth;
   //public int customValueHeight;
    public String value;
    public String path;
    public LocalDateTime creationDate;

  public CatDto(String id, String name, String value, String path, LocalDateTime  creationDate) {
    this.id = id;
    this.name = name;
    this.value = value;
    this.path = path;
    this.creationDate = creationDate;
  }
}
