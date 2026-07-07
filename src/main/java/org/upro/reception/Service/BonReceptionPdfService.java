package org.upro.reception.Service;


import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import org.upro.reception.DTO.BonReceptionResponseDTO;
import org.upro.reception.DTO.Bon_RecptionDTO.ReceptionFactureDTO;
import org.upro.reception.DTO.LigneBonResponseDTO;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.stream.Collectors;

@Service
public class BonReceptionPdfService {

    public byte[] generateBonReceptionPdf(BonReceptionResponseDTO bon) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
            PdfWriter.getInstance(document, out);

            document.open();

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font sectionFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 10);
            Font smallFont = new Font(Font.HELVETICA, 8);
            Font headerFont = new Font(Font.HELVETICA, 8, Font.BOLD, Color.WHITE);

            Paragraph title = new Paragraph("BON DE RÉCEPTION PHYSIQUE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(15);
            document.add(title);

            // =========================
            // HEAD / INFORMATIONS
            // =========================
            PdfPTable infoTable = new PdfPTable(4);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(15);
            infoTable.setWidths(new float[]{2.2f, 4.8f, 2.2f, 4.8f});

            // Ligne 1
            addInfoCell(infoTable, "Date réception", sectionFont);
            addInfoCell(infoTable, text(bon.dateReception()), normalFont);

            addInfoCell(infoTable, "Fournisseur", sectionFont);
            addInfoCell(infoTable, text(bon.fourName()), normalFont);

            // Ligne 2
            addInfoCell(infoTable, "Créé par", sectionFont);
            addInfoCell(infoTable, text(bon.createdBy()), normalFont);

            addInfoCell(infoTable, "ID", sectionFont);
            addInfoCell(infoTable, String.valueOf(bon.id()), normalFont);

            // Ligne 3
            addInfoCell(infoTable, "Validé par", sectionFont);
            addInfoCell(infoTable, text(bon.validatedBy()), normalFont);

            addInfoCell(infoTable, "Date de validation", sectionFont);
            addInfoCell(infoTable, text(bon.validatedAt()), normalFont);

            /*

            // Ligne 4

            addInfoCell(infoTable, "Clôturé par", sectionFont);
            addInfoCell(infoTable, text(bon.clotureBy()), normalFont);

            addInfoCell(infoTable, "Date de clôture", sectionFont);
            addInfoCell(infoTable, text(bon.clotureAt()), normalFont);
            */

            document.add(infoTable);

            // =========================
            // FACTURES EN UNE SEULE LIGNE
            // =========================
            String facturesText = "-";

            if (bon.factures() != null && !bon.factures().isEmpty()) {
                facturesText = bon.factures()
                        .stream()
                        .map(ReceptionFactureDTO::ref)
                        .filter(ref -> ref != null && !ref.trim().isEmpty())
                        .collect(Collectors.joining(", "));

                if (facturesText.isBlank()) {
                    facturesText = "-";
                }
            }

            Paragraph factureParagraph = new Paragraph("Factures : " + facturesText, normalFont);
            factureParagraph.setSpacingAfter(15);
            document.add(factureParagraph);

            // =========================
            // LIGNES DE RÉCEPTION
            // =========================
            Paragraph ligneTitle = new Paragraph("Lignes de réception", sectionFont);
            ligneTitle.setSpacingAfter(8);
            document.add(ligneTitle);

            PdfPTable ligneTable = new PdfPTable(15);
            ligneTable.setWidthPercentage(100);
            ligneTable.setWidths(new float[]{
                    4.0f, 3.0f, 2.0f, 2.0f, 2.0f,
                    1.8f, 1.8f, 1.5f, 1.5f, 1.8f,
                    1.5f, 1.5f, 1.5f, 1.5f, 2.0f
            });

            String[] headers = {
                    "Nom",
                    "Labo",
                    "Dosage",
                    "Forme",
                    "Lot",
                    "DDF",
                    "DDP",
                    "PPA",
                    "SHP",
                    "Colissage",
                    "Colis",
                    "Vrac",
                    "Qte",
                    "Abîmé",
                    "Créé par"
            };

            for (String h : headers) {
                addHeaderCell(ligneTable, h, headerFont);
            }

            ligneTable.setHeaderRows(1);

            int totalQte = 0;
            int totalColis = 0;
            int totalVrac = 0;
            int totalAbime = 0;

            if (bon.lignes() != null && !bon.lignes().isEmpty()) {
                for (LigneBonResponseDTO l : bon.lignes()
                        .stream()
                        .sorted((a, b) -> text(a.name()).compareToIgnoreCase(text(b.name())))
                        .toList()) {

                    totalQte += safeInt(l.qte());
                    totalColis += safeInt(l.colis());
                    totalVrac += safeInt(l.vrag());
                    totalAbime += safeInt(l.qteAbime());

                    addBodyCell(ligneTable, text(l.name()), smallFont);
                    addBodyCell(ligneTable, text(l.labo()), smallFont);
                    addBodyCell(ligneTable, text(l.dosage()), smallFont);
                    addBodyCell(ligneTable, text(l.forme()), smallFont);
                    addBodyCell(ligneTable, text(l.lot()), smallFont);
                    addBodyCell(ligneTable, text(l.ddf()), smallFont);
                    addBodyCell(ligneTable, text(l.ddp()), smallFont);
                    addBodyCell(ligneTable, text(l.ppa()), smallFont);
                    addBodyCell(ligneTable, text(l.shp()), smallFont);
                    addBodyCell(ligneTable, text(l.colissage()), smallFont);
                    addBodyCell(ligneTable, text(l.colis()), smallFont);
                    addBodyCell(ligneTable, text(l.vrag()), smallFont);
                    addBodyCell(ligneTable, text(l.qte()), smallFont);
                    addBodyCell(ligneTable, text(l.qteAbime()), smallFont);
                    addBodyCell(ligneTable, text(l.createdBy()), smallFont);
                }
            } else {
                for (int i = 0; i < 15; i++) {
                    addBodyCell(ligneTable, "-", smallFont);
                }
            }

            document.add(ligneTable);

            Paragraph totals = new Paragraph(
                    "Total Qte: " + totalQte +
                            "    |    Total Colis: " + totalColis +
                            "    |    Total Vrac: " + totalVrac +
                            "    |    Total Abîmé: " + totalAbime,
                    sectionFont
            );

            totals.setSpacingBefore(15);
            totals.setAlignment(Element.ALIGN_RIGHT);
            document.add(totals);

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }

    private int safeInt(Integer value) {
        return value == null ? 0 : value;
    }

    private String text(Object value) {
        if (value == null) {
            return "-";
        }

        String s = String.valueOf(value);

        if (s.trim().isEmpty() || s.equalsIgnoreCase("null")) {
            return "-";
        }

        return s;
    }

    private void addInfoCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text == null ? "-" : text, font));
        cell.setPadding(6);
        cell.setBorderColor(Color.LIGHT_GRAY);
        table.addCell(cell);
    }

    private void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(new Color(34, 90, 34));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addBodyCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text == null ? "-" : text, font));
        cell.setPadding(4);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderColor(Color.LIGHT_GRAY);
        table.addCell(cell);
    }
}