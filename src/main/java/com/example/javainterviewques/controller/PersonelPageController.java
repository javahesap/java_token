package com.example.javainterviewques.controller;


import com.example.javainterviewques.service.PersonelService;
import com.example.javainterviewques.model.Personel;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/personelpage")
public class PersonelPageController {

    private final PersonelService service;

    public PersonelPageController(PersonelService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public String personelListesi(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String keyword,
            Model model
    ) {
        Page<Personel> personelPage = service.searchPersonel(keyword, page, size, sortBy, direction);

        model.addAttribute("personelPage", personelPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", personelPage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("keyword", keyword); // arama kelimesini sakla

        return "personel_list";
    }
}
