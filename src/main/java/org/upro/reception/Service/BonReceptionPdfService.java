package org.upro.reception.Service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;
import org.upro.reception.DTO.BonReceptionResponseDTO;
import org.upro.reception.DTO.Bon_RecptionDTO.ReceptionFactureDTO;
import org.upro.reception.DTO.LigneBonResponseDTO;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

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

            Paragraph title = new Paragraph("BON DE RÉCEPTION", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(15);
            document.add(title);

            PdfPTable infoTable = new PdfPTable(4);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(15);
            infoTable.setWidths(new float[]{2, 4, 2, 4});

            addInfoCell(infoTable, "ID", sectionFont);
            addInfoCell(infoTable, String.valueOf(bon.id()), normalFont);

            addInfoCell(infoTable, "Date réception", sectionFont);
            addInfoCell(infoTable, String.valueOf(bon.dateReception()), normalFont);

            addInfoCell(infoTable, "Fournisseur ID", sectionFont);
            addInfoCell(infoTable, String.valueOf(bon.fourId()), normalFont);

            addInfoCell(infoTable, "Fournisseur", sectionFont);
            addInfoCell(infoTable, bon.fourName(), normalFont);

            addInfoCell(infoTable, "Créé par", sectionFont);
            addInfoCell(infoTable, bon.createdBy(), normalFont);

            addInfoCell(infoTable, "Créé le", sectionFont);
            addInfoCell(infoTable, String.valueOf(bon.createdAt()), normalFont);

            addInfoCell(infoTable, "Validé", sectionFont);
            addInfoCell(infoTable, Boolean.TRUE.equals(bon.isValidated()) ? "Oui" : "Non", normalFont);

            addInfoCell(infoTable, "Validé par", sectionFont);
            addInfoCell(infoTable, bon.validatedBy(), normalFont);

            document.add(infoTable);

            Paragraph factureTitle = new Paragraph("Factures", sectionFont);
            factureTitle.setSpacingAfter(8);
            document.add(factureTitle);

            PdfPTable factureTable = new PdfPTable(3);
            factureTable.setWidthPercentage(100);
            factureTable.setSpacingAfter(15);
            factureTable.setWidths(new float[]{3, 3, 5});

            addHeaderCell(factureTable, "Date", headerFont);
            addHeaderCell(factureTable, "Référence", headerFont);
            addHeaderCell(factureTable, "Créé le", headerFont);

            if (bon.factures() != null && !bon.factures().isEmpty()) {
                for (ReceptionFactureDTO f : bon.factures()) {
                    addBodyCell(factureTable, String.valueOf(f.date()), normalFont);
                    addBodyCell(factureTable, f.ref(), normalFont);
                    addBodyCell(factureTable, String.valueOf(f.createdAt()), normalFont);
                }
            } else {
                addBodyCell(factureTable, "-", normalFont);
                addBodyCell(factureTable, "-", normalFont);
                addBodyCell(factureTable, "-", normalFont);
            }

            document.add(factureTable);

            Paragraph ligneTitle = new Paragraph("Lignes de réception", sectionFont);
            ligneTitle.setSpacingAfter(8);
            document.add(ligneTitle);

            PdfPTable ligneTable = new PdfPTable(16);
            ligneTable.setWidthPercentage(100);
            ligneTable.setWidths(new float[]{
                    1.2f, 1.5f, 4f, 3f, 2f, 2f,
                    1.7f, 1.8f, 1.8f, 1.5f,
                    1.5f, 1.7f, 1.5f, 1.5f,
                    1.7f, 1.7f
            });

            String[] headers = {
                    "ID", "Med ID", "Nom", "Labo", "Dosage", "Forme",
                    "Lot", "DDP", "DDF", "PPA",
                    "SHP", "Colissage", "Colis", "Vrac",
                    "Qte", "Abîmé"
            };

            for (String h : headers) {
                addHeaderCell(ligneTable, h, headerFont);
            }

            int totalQte = 0;
            int totalColis = 0;
            int totalVrac = 0;
            int totalAbime = 0;

            if (bon.lignes() != null && !bon.lignes().isEmpty()) {
                for (LigneBonResponseDTO l : bon.lignes()) {

                    totalQte += safeInt(l.qte());
                    totalColis += safeInt(l.colis());
                    totalVrac += safeInt(l.vrag());
                    totalAbime += safeInt(l.qteAbime());

                    addBodyCell(ligneTable, String.valueOf(l.id()), smallFont);
                    addBodyCell(ligneTable, String.valueOf(l.medId()), smallFont);
                    addBodyCell(ligneTable, l.name(), smallFont);
                    addBodyCell(ligneTable, l.labo(), smallFont);
                    addBodyCell(ligneTable, l.dosage(), smallFont);
                    addBodyCell(ligneTable, l.forme(), smallFont);
                    addBodyCell(ligneTable, l.lot(), smallFont);
                    addBodyCell(ligneTable, String.valueOf(l.ddp()), smallFont);
                    addBodyCell(ligneTable, String.valueOf(l.ddf()), smallFont);
                    addBodyCell(ligneTable, String.valueOf(l.ppa()), smallFont);
                    addBodyCell(ligneTable, String.valueOf(l.shp()), smallFont);
                    addBodyCell(ligneTable, String.valueOf(l.colissage()), smallFont);
                    addBodyCell(ligneTable, String.valueOf(l.colis()), smallFont);
                    addBodyCell(ligneTable, String.valueOf(l.vrag()), smallFont);
                    addBodyCell(ligneTable, String.valueOf(l.qte()), smallFont);
                    addBodyCell(ligneTable, String.valueOf(l.qteAbime()), smallFont);
                }
            } else {
                for (int i = 0; i < 16; i++) {
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