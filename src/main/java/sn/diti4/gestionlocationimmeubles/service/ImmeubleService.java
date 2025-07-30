package sn.diti4.gestionlocationimmeubles.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.diti4.gestionlocationimmeubles.entity.Immeuble;
import sn.diti4.gestionlocationimmeubles.entity.Utilisateur;
import sn.diti4.gestionlocationimmeubles.repository.ImmeubleRepository;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImmeubleService {

    private final ImmeubleRepository immeubleRepository;

    public List<Immeuble> findAll() {
        return immeubleRepository.findAll();
    }

    public Optional<Immeuble> findById(Long id) {
        return immeubleRepository.findById(id);
    }

    public Immeuble save(Immeuble immeuble) {
        return immeubleRepository.save(immeuble);
    }

    public void deleteById(Long id) {
        if (!immeubleRepository.existsById(id)) {
            throw new RuntimeException("Immeuble introuvable avec l'ID : " + id);
        }
        immeubleRepository.deleteById(id);
    }

    public List<Immeuble> findByProprietaire(Utilisateur proprietaire) {
        return immeubleRepository.findByProprietaire(proprietaire);
    }

    public List<Immeuble> searchByAdresse(String adresse) {
        return immeubleRepository.findByAdresseContainingIgnoreCase(adresse);
    }

    public long count() {
        return immeubleRepository.count();
    }
}
