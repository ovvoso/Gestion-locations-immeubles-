package sn.diti4.gestionlocationimmeubles.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sn.diti4.gestionlocationimmeubles.entity.Rapport;
import sn.diti4.gestionlocationimmeubles.repository.RapportRepository;
import sn.diti4.gestionlocationimmeubles.service.ReportService;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/rapports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final RapportRepository rapportRepository;

    @GetMapping
    public String listRapports(Model model) {
        List<Rapport> rapports = rapportRepository.findAllByOrderByGeneratedAtDesc();
        model.addAttribute("rapports", rapports);
        return "rapports/list";
    }

    @PostMapping("/generate/{type}")
    public String generateReport(@PathVariable String type, @RequestParam(required = false) String generatedBy) throws Exception {
        // TODO: Récupérer les données selon le type (à compléter)
        Map<String, Object> data = new HashMap<>();
        // ...
        reportService.generateReport(type, data, generatedBy != null ? generatedBy : "admin");
        return "redirect:/rapports";
    }

    @GetMapping("/preview/{id}")
    public ResponseEntity<InputStreamResource> preview(@PathVariable Long id) throws Exception {
        String path = reportService.getPdfPath(id);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(path));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=rapport.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> download(@PathVariable Long id) throws Exception {
        String path = reportService.getPdfPath(id);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(path));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=rapport.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}
