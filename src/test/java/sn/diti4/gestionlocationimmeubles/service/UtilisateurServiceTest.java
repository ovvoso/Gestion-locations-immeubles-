package sn.diti4.gestionlocationimmeubles.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sn.diti4.gestionlocationimmeubles.entity.Utilisateur;
import sn.diti4.gestionlocationimmeubles.repository.UtilisateurRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UtilisateurServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private UtilisateurService utilisateurService;

    private Utilisateur utilisateur;

    @BeforeEach
    void setUp() {
        utilisateur = Utilisateur.builder()
                .id(1L)
                .nom("Doe")
                .prenom("John")
                .email("john.doe@email.com")
                .motDePasse("password123")
                .build();
    }

    @Test
    void testFindAll() {
        // Given
        List<Utilisateur> utilisateurs = Arrays.asList(utilisateur);
        when(utilisateurRepository.findAll()).thenReturn(utilisateurs);

        // When
        List<Utilisateur> result = utilisateurService.findAll();

        // Then
        assertEquals(1, result.size());
        assertEquals(utilisateur.getEmail(), result.get(0).getEmail());
    }

    @Test
    void testSaveNewUtilisateur() {
        // Given
        Utilisateur newUtilisateur = Utilisateur.builder()
                .nom("Doe")
                .prenom("Jane")
                .email("jane.doe@email.com")
                .motDePasse("password123")
                .build();
        
        when(utilisateurRepository.existsByEmail(newUtilisateur.getEmail())).thenReturn(false);
        when(utilisateurRepository.save(newUtilisateur)).thenReturn(newUtilisateur);

        // When
        Utilisateur result = utilisateurService.save(newUtilisateur);

        // Then
        assertEquals(newUtilisateur.getEmail(), result.getEmail());
        verify(utilisateurRepository).save(newUtilisateur);
    }

    @Test
    void testSaveUtilisateurWithExistingEmail() {
        // Given
        Utilisateur newUtilisateur = Utilisateur.builder()
                .email("existing@email.com")
                .build();
        
        when(utilisateurRepository.existsByEmail(newUtilisateur.getEmail())).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> utilisateurService.save(newUtilisateur));
        
        assertTrue(exception.getMessage().contains("Email déjà utilisé"));
    }
}
