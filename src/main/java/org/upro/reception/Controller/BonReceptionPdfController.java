package org.upro.reception.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.upro.reception.DTO.BonReceptionResponseDTO;
import org.upro.reception.Service.BonReceptionPdfService;
import org.upro.reception.Service.BonReceptionService;

@RestController
@RequestMapping("/bon-reception")
@RequiredArgsConstructor
public class BonReceptionPdfController {

    private final BonReceptionService bonReceptionService;
    private final BonReceptionPdfService pdfService;

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Integer id) {

        BonReceptionResponseDTO bon = bonReceptionService.getById(id);

        byte[] pdf = pdfService.generateBonReceptionPdf(bon);

        String filename = "bon-reception-" + id + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(filename)
                        .build()
        );

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}