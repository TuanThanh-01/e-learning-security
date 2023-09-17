package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.entity.image.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageDataRepository extends JpaRepository<ImageData, Integer> {
    Optional<ImageData> findByName(String name);
}
