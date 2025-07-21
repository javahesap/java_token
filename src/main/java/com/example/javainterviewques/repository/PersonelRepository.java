package com.example.javainterviewques.repository;



import com.example.javainterviewques.model.Personel;

import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonelRepository extends JpaRepository<Personel, Long> {
	 Page<Personel> findByAdContainingIgnoreCaseOrSoyadContainingIgnoreCase(String ad, String soyad, org.springframework.data.domain.Pageable pageable);
}

