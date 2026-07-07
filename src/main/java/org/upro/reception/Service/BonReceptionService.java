package org.upro.reception.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.upro.reception.DTO.BonReceptionResponseDTO;
import org.upro.reception.DTO.Bon_RecptionDTO.*;
import org.upro.reception.DTO.LigneBonResponseDTO;
import org.upro.reception.db.Entity.BonReception;
import org.upro.reception.db.Entity.CustomUser;
import org.upro.reception.db.Entity.ReceptionFacture;
import org.upro.reception.db.Entity.ReceptionLigneBon;
import org.upro.reception.db.Repo.BonReceptionRepository;
import org.upro.reception.db.Repo.ReceptionLigneBonRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BonReceptionService {

    private final BonReceptionRepository bonRepo;
    private final UserAccessService userAccessService;
    private final ReceptionLigneBonRepository receptionLigneBonRepository;

    Set<String> authorizedTypes = Set.of("admin", "reception-sup");



    private CustomUser getCurrentUser() {
        return (CustomUser) userAccessService.getCurrentUser();
    }



    private boolean isSupervisor() {
        List<String> groups = userAccessService.Groupslog();

        return groups.contains("reception-sup")
                || groups.contains("admin");
    }

    @Transactional(readOnly = true)
    public BonReceptionResponseDTO getById(Integer id) {
        BonReception bon = bonRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Bon not found: " + id));

        return mapToResponse(bon);
    }

    @Transactional
    public Integer createBonReception(CreateBonReceptionResponseDTO dto) {

        CustomUser user = getCurrentUser();

        BonReception br = new BonReception();

        br.setDateReception(dto.dateReception());
        br.setFourId(dto.fourId());
        br.setFourName(dto.fourName());

        br.setCreatedBy(user.getUsername());
        br.setCreatedAt(Instant.now());

        if (dto.factures() != null) {
            for (CreateFactureReceptionDTO factureDto : dto.factures()) {

                ReceptionFacture facture = new ReceptionFacture();
                facture.setRef(factureDto.ref());
                facture.setDateFacture(factureDto.date());
                facture.setCreatedAt(Instant.now());

                facture.setBrcp(br);

                br.getReceptionFactures().add(facture);
            }
        }

        bonRepo.save(br);

        return br.getId();
    }

    @Transactional
    public Integer addLigneBon(Integer brcpId, CreateLigneBonDTO dto) {

        BonReception br = bonRepo.findById(brcpId)
                .orElseThrow(() -> new RuntimeException("BonReception not found: " + brcpId));

        CustomUser user = getCurrentUser();
        String type = user.getType() == null ? "" : user.getType().trim().toLowerCase();



        if (Boolean.TRUE.equals(br.getIsValidated()) &&  !isSupervisor()) {
            throw new IllegalStateException("Cannot add line of closed bon or user is not supervisor");
        }

        ReceptionLigneBon line = buildLigne(dto, br, user);

        ReceptionLigneBon saved = receptionLigneBonRepository.save(line);

        return saved.getId();
    }



    @Transactional
    public BonReceptionResponseDTO clotureBon(Integer brcpId) {

        CustomUser user = getCurrentUser();

        if (!isSupervisor()) {
            throw new IllegalStateException("Only supervisors can validate bon");
        }

        BonReception bon = bonRepo.findById(brcpId)
                .orElseThrow(() -> new IllegalStateException("Bon not found"));

        // prevent double validation
        if (Boolean.TRUE.equals(bon.getIsCloture()) || Boolean.FALSE.equals(bon.getIsValidated())) {
            throw new IllegalStateException("Bon is already closed or bon is not validated");
        }

        bon.setIsCloture(true);
        bon.setClotureAt(Instant.now());
        bon.setClotureBy(user.getUsername());

        BonReception saved = bonRepo.save(bon);

        return mapToResponse(saved);
    }



    @Transactional
    public BonReceptionResponseDTO validateBon(Integer brcpId) {

        CustomUser user = getCurrentUser();

        if (!isSupervisor()) {
            throw new IllegalStateException("Only supervisors can validate bon");
        }

        BonReception bon = bonRepo.findById(brcpId)
                .orElseThrow(() -> new IllegalStateException("Bon not found"));

        // prevent double validation
        if (Boolean.TRUE.equals(bon.getIsValidated())) {
            throw new IllegalStateException("Bon is already validated");
        }

        bon.setIsValidated(true);
        bon.setValidatedAt(Instant.now());
        bon.setValidatedBy(user.getUsername());

        BonReception saved = bonRepo.save(bon);

        return mapToResponse(saved);
    }

    @Transactional
    public BonReceptionResponseDTO editBonReception(Integer brcpId, EditBonReceptionDTO dto) {

        BonReception bon = bonRepo.findById(brcpId)
                .orElseThrow(() -> new IllegalStateException("Bon not found"));


        CustomUser user = getCurrentUser();
        String type = user.getType() == null ? "" : user.getType().trim().toLowerCase();

        boolean allowed = authorizedTypes.contains(type);


        if (Boolean.TRUE.equals(bon.getIsValidated()) || !allowed) {
            throw new IllegalStateException("Cannot edit validated bon");
        }



        bon.setDateReception(dto.dateReception());
        bon.setFourId(dto.fourId());
        bon.setFourName(dto.fourName());

        if (dto.factures() != null) {
            bon.getReceptionFactures().clear();

            for (ReceptionFactureDTO fDto : dto.factures()) {
                ReceptionFacture facture = new ReceptionFacture();
                facture.setBrcp(bon);
                facture.setDateFacture(fDto.date());
                facture.setRef(fDto.ref());
                facture.setCreatedAt(Instant.now());

                bon.getReceptionFactures().add(facture);
            }
        }

        BonReception saved = bonRepo.save(bon);

        return mapToResponse(saved);
    }

    @Transactional
    public void deleteBonReception(Integer brcpId) {

        BonReception bon = bonRepo.findById(brcpId)
                .orElseThrow(() -> new IllegalStateException("Bon not found"));


        CustomUser user = getCurrentUser();
        String type = user.getType() == null ? "" : user.getType().trim().toLowerCase();


        if (Boolean.TRUE.equals(bon.getIsValidated()) || !isSupervisor()) {
            throw new IllegalStateException("Cannot delete validated bon");
        }

        bonRepo.delete(bon);
    }


    private ReceptionLigneBon buildLigne(
            CreateLigneBonDTO dto,
            BonReception br,
            CustomUser user
    ) {

        ReceptionLigneBon line = new ReceptionLigneBon();

        // relationship
        line.setBrcp(br);

        // required fields
        line.setMedId(dto.medId());
        line.setLot(dto.lot());
        line.setQte(dto.qte());

        // optional fields
        line.setColis(dto.colis());
        line.setVrag(dto.vrag());
        line.setQteAbime(dto.qteAbime());

        line.setName(dto.name());

        line.setLabo(dto.labo());


        line.setDosage(dto.dosage());
        line.setForme(dto.forme());

        line.setDdp(dto.ddp());
        line.setDdf(dto.ddf());

        line.setPpa(dto.ppa());
        line.setShp(dto.shp());

        line.setColissage(dto.colissage());

        // audit
        line.setCreatedBy(user.getUsername());
        line.setCreatedAt(Instant.now());

        return line;
    }

    @Transactional
    public LigneBonResponseDTO editLigneBon(Integer ligneId, EditLigneBonDTO dto) {



        ReceptionLigneBon ligne = receptionLigneBonRepository.findById(ligneId)
                .orElseThrow(() -> new IllegalStateException("Ligne not found"));

        BonReception bon = ligne.getBrcp();

        CustomUser user = getCurrentUser();
        String type = user.getType() == null ? "" : user.getType().trim().toLowerCase();


        if (Boolean.TRUE.equals(bon.getIsValidated()) && !isSupervisor()) {
            throw new IllegalStateException("Cannot edit line of closed bon or user is not supervisor");
        }

        ligne.setLot(dto.lot());
        ligne.setQte(dto.qte());

        ligne.setColis(dto.colis());
        ligne.setVrag(dto.vrag());
        ligne.setQteAbime(dto.qteAbime());

        ligne.setDdp(dto.ddp());
        ligne.setDdf(dto.ddf());

        ligne.setPpa(dto.ppa());
        ligne.setShp(dto.shp());

        ligne.setColissage(dto.colissage());

        ReceptionLigneBon saved = receptionLigneBonRepository.save(ligne);

        return mapLigne(saved);
    }

    @Transactional
    public void deleteLigneBon(Integer ligneId) {

        ReceptionLigneBon ligne = receptionLigneBonRepository.findById(ligneId)
                .orElseThrow(() -> new IllegalStateException("Ligne not found"));

        BonReception bon = ligne.getBrcp();

        if (Boolean.TRUE.equals(bon.getIsCloture())) {
            throw new IllegalStateException("Cannot delete line of clotured bon");
        }

        receptionLigneBonRepository.delete(ligne);
    }



/////////////////////////////////////////////////////////////////////////////////
///
public List<BonReceptionResponseDTO> getAll() {
    return bonRepo.findAll().stream()
            .map(this::mapToResponse)
            .toList();
}

    public List<BonReceptionResponseDTO> getToday() {
        return bonRepo.findByDateReception(LocalDate.now())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<BonReceptionResponseDTO> getBetween(LocalDate start, LocalDate end) {
        return bonRepo.findByDateReceptionBetween(start, end)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private BonReceptionResponseDTO mapToResponse(BonReception bon) {

        List<LigneBonResponseDTO> lignes = receptionLigneBonRepository.findByBrcp_Id(bon.getId())
                .stream()
                .map(this::mapLigne)
                .toList();

        List<ReceptionFactureDTO> factures = bon.getReceptionFactures() == null
                ? List.of()
                : bon.getReceptionFactures().stream()
                .map(this::mapFacture)
                .toList();

        return new BonReceptionResponseDTO(
                bon.getId(),
                bon.getDateReception(),
                bon.getFourId(),
                bon.getFourName(),
                bon.getCreatedBy(),
                bon.getCreatedAt(),
                bon.getIsValidated(),
                bon.getValidatedAt(),
                bon.getValidatedBy(),
                bon.getIsCloture(),
                bon.getClotureAt(),
                bon.getClotureBy(),
                factures,
                lignes
        );
    }

    private LigneBonResponseDTO mapLigne(ReceptionLigneBon l) {
        return new LigneBonResponseDTO(
                l.getId(),
                l.getMedId(),
                l.getLot(),
                l.getQte(),
                l.getColis(),
                l.getVrag(),
                l.getQteAbime(),
                l.getName(),
                l.getLabo(),
                l.getDosage(),
                l.getForme(),
                l.getDdp(),
                l.getDdf(),
                l.getPpa(),
                l.getShp(),
                l.getColissage(),
                l.getCreatedBy(),
                l.getCreatedAt()
        );
    }


    private ReceptionFactureDTO mapFacture(ReceptionFacture f) {
        return new ReceptionFactureDTO(
                f.getDateFacture(),
                f.getRef(),
                f.getCreatedAt()
        );
    }



}