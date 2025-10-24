package ma.projet.web;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.projet.service.FemmeService;
import ma.projet.beans.Femme;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final FemmeService femmeService = new FemmeService(); // Using existing service (HibernateUtil)

    @Operation(summary = "Vérifier que l'API est démarrée")
    @GetMapping("/ping")
    public String ping() {
        return "ok";
    }

    @Operation(summary = "Récupérer la liste des femmes (id, nom, prénom)")
    @GetMapping("/femmes")
    public List<FemmeDto> getFemmes() {
        List<Femme> femmes = femmeService.findAll();
        return femmes.stream()
                .map(f -> new FemmeDto(f.getId(), f.getNom(), f.getPrenom()))
                .collect(Collectors.toList());
    }

    public record FemmeDto(Long id, String nom, String prenom) {}
}
