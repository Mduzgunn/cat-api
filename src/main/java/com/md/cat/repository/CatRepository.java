package com.md.cat.repository;

import com.md.cat.entity.Cat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatRepository extends JpaRepository<Cat,String> {
}
