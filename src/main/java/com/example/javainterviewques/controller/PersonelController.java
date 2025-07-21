package com.example.javainterviewques.controller;

import com.example.javainterviewques.annotations.CurrentUser;
import com.example.javainterviewques.model.Personel;
import com.example.javainterviewques.model.User;
import com.example.javainterviewques.service.PersonelService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personel")
public class PersonelController {

    private final PersonelService service;

    public PersonelController(PersonelService service) {
        this.service = service;
    }

    @GetMapping
    public List<Personel> listele() {
        return service.tumPersoneller();
    }

    @GetMapping("/{id}")
    public Personel getir(@PathVariable Long id) {
        return service.personelBul(id)
                .orElseThrow(() -> new RuntimeException("Personel bulunamadı: " + id));
    }

    @PostMapping
    public Personel ekle(@RequestBody Personel personel) {
        return service.kaydet(personel);
    }

    @PutMapping("/{id}")
    public Personel guncelle(@PathVariable Long id, @RequestBody Personel guncel) {
        Personel eski = service.personelBul(id)
                .orElseThrow(() -> new RuntimeException("Güncellenecek personel bulunamadı"));
        eski.setAd(guncel.getAd());
        eski.setSoyad(guncel.getSoyad());
        eski.setAdres(guncel.getAdres());
        return service.kaydet(eski);
    }

    @DeleteMapping("/{id}")
    public void sil(@PathVariable Long id) {
        service.sil(id);
    }

    @GetMapping("/me")
    public List<Personel> getProfile(@CurrentUser User user) {
        if(user.getRole().equals("ROLE_USER")) {
            return service.tumPersoneller();
        }
        return null;
    }

    @GetMapping("/personel/list")
    public String personelListesi(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model
    ) {
        Page<Personel> personelPage = service.getPersonelPage(page, size, sortBy, direction);

        model.addAttribute("personelPage", personelPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", personelPage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        return "personel_list";
    }

}
