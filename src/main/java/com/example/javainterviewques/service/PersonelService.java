package com.example.javainterviewques.service;

import com.example.javainterviewques.model.Personel;
import com.example.javainterviewques.repository.PersonelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // ✅ Pagination ve Sorting eklenen yeni metot
    public Page<Personel> getPersonelPage(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findAll(pageable);
    }
    
    public Page<Personel> searchPersonel(String keyword, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (keyword == null || keyword.isEmpty()) {
            return repository.findAll(pageable);
        }

        return repository.findByAdContainingIgnoreCaseOrSoyadContainingIgnoreCase(keyword, keyword, pageable);
    }
    
}
