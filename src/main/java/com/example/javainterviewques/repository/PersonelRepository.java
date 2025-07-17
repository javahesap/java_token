package com.example.javainterviewques.repository;



import com.example.javainterviewques.model.Personel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonelRepository extends JpaRepository<Personel, Long> {
}

