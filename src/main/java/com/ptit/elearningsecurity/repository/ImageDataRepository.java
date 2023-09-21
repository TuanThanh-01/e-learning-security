package com.ptit.elearningsecurity.repository;

import com.ptit.elearningsecurity.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageDataRepository extends JpaRepository<ImageData, Integer> {

}
