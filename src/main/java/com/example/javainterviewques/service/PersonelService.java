package com.example.javainterviewques.service;



import com.example.javainterviewques.model.Personel;
import com.example.javainterviewques.repository.PersonelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonelService {

    private final PersonelRepository repository;

    public PersonelService(PersonelRepository repository) {
        this.repository = repository;
    }

    public List<Personel> tumPersoneller() {
        return repository.findAll();
    }

    public Optional<Personel> personelBul(Long id) {
        return repository.findById(id);
    }

    public Personel kaydet(Personel personel) {
        return repository.save(personel);
    }

    public void sil(Long id) {
        repository.deleteById(id);
    }
}
