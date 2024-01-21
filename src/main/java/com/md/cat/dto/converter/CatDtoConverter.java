package com.md.cat.dto.converter;

import com.md.cat.dto.CatDto;
import com.md.cat.entity.Cat;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CatDtoConverter {
    public CatDto convert(Cat from) {
        return new CatDto(
                from.getId(),
                from.getName(),
                from.getContentFormat(),
                from.getValue(),
                from.getPath(),
                from.getCreationDate()
        );
    }

    public List<CatDto> convertToCatDtoList(List<Cat> matchList) {
        return matchList
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
