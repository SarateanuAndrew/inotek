package org.example.exportproback.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exportproback.auth.domain.Role;
import org.example.exportproback.auth.domain.User;
import org.example.exportproback.auth.repository.UserRepository;
import org.example.exportproback.company.domain.Company;
import org.example.exportproback.company.domain.CompanyStatus;
import org.example.exportproback.company.repository.CompanyRepository;
import org.example.exportproback.compliance.domain.ComplianceCase;
import org.example.exportproback.compliance.domain.ComplianceCaseStatus;
import org.example.exportproback.compliance.domain.Requirement;
import org.example.exportproback.compliance.domain.RequirementStatus;
import org.example.exportproback.compliance.domain.RequirementType;
import org.example.exportproback.compliance.repository.ComplianceCaseRepository;
import org.example.exportproback.compliance.repository.RequirementRepository;
import org.example.exportproback.marketplace.domain.ListingStatus;
import org.example.exportproback.marketplace.domain.MarketplaceListing;
import org.example.exportproback.marketplace.repository.MarketplaceListingRepository;
import org.example.exportproback.product.domain.Product;
import org.example.exportproback.product.domain.ProductCategory;
import org.example.exportproback.product.repository.ProductRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class MockDataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;
    private final ComplianceCaseRepository complianceCaseRepository;
    private final RequirementRepository requirementRepository;
    private final MarketplaceListingRepository marketplaceListingRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (!userRepository.existsByEmail("market@demo.com")) {
            log.info("Seeding mock marketplace data...");
            seedAll();
            log.info("Mock data seeded. Login: market@demo.com / password");
        }
        if (!userRepository.existsByEmail("apicul@demo.com")) {
            log.info("Seeding HoneyHouse demo account...");
            seedHoneyHouse();
            log.info("HoneyHouse seeded. Login: apicul@demo.com / password");
        }
        if (!complianceCaseRepository.existsByCaseNumber("EXP-2026-HH-004")) {
            log.info("Seeding HoneyHouse extra certifications...");
            seedHoneyHouseCertifications();
            log.info("HoneyHouse certifications seeded.");
        }
    }

    private void seedAll() {
        String encoded = passwordEncoder.encode("password");

        // ── Market / buyer account ──────────────────────────────────────────
        userRepository.save(User.builder()
                .email("market@demo.com")
                .password(encoded)
                .firstName("Alex")
                .lastName("Distribuitor")
                .role(Role.DISTRIBUTOR)
                .enabled(true)
                .build());

        // ── Producer 1: Miere din Ardeal ────────────────────────────────────
        User p1 = userRepository.save(User.builder()
                .email("producer1@demo.com").password(encoded)
                .firstName("Ion").lastName("Ionescu")
                .role(Role.PRODUCER).enabled(true).build());

        Company c1 = companyRepository.save(Company.builder()
                .name("Apicultura Ionescu SRL")
                .country("ROU")
                .registrationNumber("RO12345678")
                .vatNumber("RO12345678")
                .address("Strada Mierii 10, Sibiu, România")
                .website("www.miere-ionescu.ro")
                .phoneNumber("+40722123456")
                .status(CompanyStatus.ACTIVE)
                .ownerId(p1.getId())
                .build());

        Product pr1 = productRepository.save(Product.builder()
                .companyId(c1.getId())
                .name("Miere polifloră bio din Ardeal")
                .category(ProductCategory.FOOD_HONEY)
                .description("Miere crudă, nefiltrată, recoltată din zone montane certificate bio din Munții Apuseni. Gustul bogat reflectă flora diversă a pajiștilor de munte.")
                .hsCode("0409.00.00")
                .ingredients("100% miere polifloră")
                .packagingType("Borcan de sticlă 400g / 900g")
                .weightGrams(400)
                .organic(true)
                .build());

        ComplianceCase cc1 = complianceCaseRepository.save(ComplianceCase.builder()
                .caseNumber("EXP-2026-001")
                .productId(pr1.getId())
                .companyId(c1.getId())
                .originCountry("ROU")
                .targetCountry("DEU")
                .status(ComplianceCaseStatus.COMPLIANT)
                .readinessScore(94)
                .aiAnalysisSummary("Producătorul îndeplinește toate cerințele UE pentru export de miere bio. HACCP implementat, certificare Bio obținută, etichetare conformă.")
                .build());

        marketplaceListingRepository.save(MarketplaceListing.builder()
                .productId(pr1.getId())
                .companyId(c1.getId())
                .complianceCaseId(cc1.getId())
                .title("Miere polifloră bio — Munții Apuseni, România")
                .description("Apicultura Ionescu este o familie de apicultori din Sibiu cu 25 de ani de experiență. Producem miere crudă, nefiltrată, certificată bio din stupii amplasați în zone protejate din Munții Apuseni.\n\nProdusul nostru se diferențiază prin: origine montană verificată, procesare la rece (sub 38°C), fără aditivi sau tratamente chimice.\n\nDisponibil în borcane de sticlă de 400g și 900g cu etichetare în română, engleză și germană. EAN înregistrat GS1.")
                .status(ListingStatus.PUBLISHED)
                .certificationSummary("Bio (Ecocert), HACCP, DOP Silagiu")
                .targetMarkets("Germania, Austria, România, Elveția")
                .minimumOrderQuantity(200)
                .productionCapacity("2.000 kg/lună")
                .publishedAt(LocalDateTime.now())
                .build());

        // ── Producer 2: Brânzeturi din Bucovina ─────────────────────────────
        User p2 = userRepository.save(User.builder()
                .email("producer2@demo.com").password(encoded)
                .firstName("Maria").lastName("Popa")
                .role(Role.PRODUCER).enabled(true).build());

        Company c2 = companyRepository.save(Company.builder()
                .name("Lactate Bucovina SRL")
                .country("ROU")
                .registrationNumber("RO23456789")
                .vatNumber("RO23456789")
                .address("Strada Izvorului 5, Câmpulung Moldovenesc, Suceava")
                .website("www.lactate-bucovina.ro")
                .phoneNumber("+40744234567")
                .status(CompanyStatus.ACTIVE)
                .ownerId(p2.getId())
                .build());

        Product pr2 = productRepository.save(Product.builder()
                .companyId(c2.getId())
                .name("Caș afumat de Bucovina")
                .category(ProductCategory.FOOD_DAIRY)
                .description("Caș afumat tradițional din lapte integral de oaie, preparat după rețeta tradițională bucovineană. Afumat la rece cu lemn de fag.")
                .hsCode("0406.10.20")
                .ingredients("Lapte integral de oaie, sare, culturi lactice naturale")
                .packagingType("Bucată întreagă ~500g, vid")
                .weightGrams(500)
                .organic(false)
                .build());

        ComplianceCase cc2 = complianceCaseRepository.save(ComplianceCase.builder()
                .caseNumber("EXP-2026-002")
                .productId(pr2.getId())
                .companyId(c2.getId())
                .originCountry("ROU")
                .targetCountry("FRA")
                .status(ComplianceCaseStatus.COMPLIANT)
                .readinessScore(88)
                .aiAnalysisSummary("Unitate autorizată ANSVSA, marcaj oval CE activ. ISO 22000 în curs de finalizare. Etichetare multilingvă conformă cu Reg. CE 1169/2011.")
                .build());

        marketplaceListingRepository.save(MarketplaceListing.builder()
                .productId(pr2.getId())
                .companyId(c2.getId())
                .complianceCaseId(cc2.getId())
                .title("Caș afumat tradițional de oaie — Bucovina")
                .description("Lactate Bucovina produce brânzeturi artizanale de trei generații, folosind lapte de la turme proprii de oi de rasa Țurcană din zona Câmpulung Moldovenesc.\n\nCașul afumat este preparat manual, fără conservanți, afumat la rece cu lemn de fag pentru un gust autentic. Autorizat ANSVSA, cu marcaj oval CE pentru export UE.\n\nAmbalaj în vid, termen de valabilitate 90 zile. Disponibil și în variantă lactat de vacă.")
                .status(ListingStatus.PUBLISHED)
                .certificationSummary("ANSVSA, Marcaj oval CE, HACCP, ISO 22000")
                .targetMarkets("Franța, Italia, Belgia, România")
                .minimumOrderQuantity(50)
                .productionCapacity("800 kg/lună")
                .publishedAt(LocalDateTime.now())
                .build());

        // ── Producer 3: Murături și conserve moldovenești ───────────────────
        User p3 = userRepository.save(User.builder()
                .email("producer3@demo.com").password(encoded)
                .firstName("Gheorghe").lastName("Moldovan")
                .role(Role.PRODUCER).enabled(true).build());

        Company c3 = companyRepository.save(Company.builder()
                .name("Zacusca & Murături Moldova SRL")
                .country("ROU")
                .registrationNumber("RO34567890")
                .vatNumber("RO34567890")
                .address("Strada Recoltei 22, Iași, România")
                .website("www.zacusca-moldova.ro")
                .phoneNumber("+40733345678")
                .status(CompanyStatus.ACTIVE)
                .ownerId(p3.getId())
                .build());

        Product pr3 = productRepository.save(Product.builder()
                .companyId(c3.getId())
                .name("Zacuscă de casă cu vinete")
                .category(ProductCategory.FOOD_VEGETABLES)
                .description("Zacuscă preparată după rețeta tradițională moldovenească cu vinete coapte, ardei copți și roșii din grădina proprie. Fără conservanți artificiali.")
                .hsCode("2005.99.80")
                .ingredients("Vinete (40%), ardei grași (25%), roșii (20%), ceapă, ulei de floarea-soarelui, sare, condimente naturale")
                .packagingType("Borcan de sticlă 300g / 700g")
                .weightGrams(300)
                .organic(false)
                .build());

        ComplianceCase cc3 = complianceCaseRepository.save(ComplianceCase.builder()
                .caseNumber("EXP-2026-003")
                .productId(pr3.getId())
                .companyId(c3.getId())
                .originCountry("ROU")
                .targetCountry("GBR")
                .status(ComplianceCaseStatus.COMPLIANT)
                .readinessScore(82)
                .aiAnalysisSummary("Unitate autorizată DSP și ANSVSA. HACCP complet. Testare pH și stabilitate microbiologică validată. Etichetare conformă cu cerințele UK post-Brexit.")
                .build());

        marketplaceListingRepository.save(MarketplaceListing.builder()
                .productId(pr3.getId())
                .companyId(c3.getId())
                .complianceCaseId(cc3.getId())
                .title("Zacuscă tradițională moldovenească — fără conservanți")
                .description("Zacusca & Murături Moldova produce conserve artizanale din legume cultivate în ferma proprie din zona Iași, folosind rețete transmise din generație în generație.\n\nZacusca noastră este preparată exclusiv din legume coapte pe foc de lemne — vinete, ardei, roșii — fără aditivi sau conservanți artificiali. Sterilizată termic la 121°C pentru siguranță maximă.\n\nDisponibil în borcane de 300g și 700g. Termen de valabilitate 24 luni. EAN înregistrat, etichetă în română, engleză, franceză.")
                .status(ListingStatus.PUBLISHED)
                .certificationSummary("HACCP, Autorizație DSP, Autorizație ANSVSA")
                .targetMarkets("Marea Britanie, Germania, SUA (diaspora), România")
                .minimumOrderQuantity(500)
                .productionCapacity("8.000 borcane/lună")
                .publishedAt(LocalDateTime.now())
                .build());

        log.info("Created 3 producers + 1 market account with published marketplace listings.");
    }

    private void seedHoneyHouse() {
        String encoded = passwordEncoder.encode("password");

        // ── User ───────────────────────────────────────────────────────────────
        User user = userRepository.save(User.builder()
                .email("apicul@demo.com")
                .password(encoded)
                .firstName("Alexandru")
                .lastName("Apicol")
                .role(Role.PRODUCER)
                .enabled(true)
                .build());

        // ── Company ────────────────────────────────────────────────────────────
        Company company = companyRepository.save(Company.builder()
                .name("HoneyHouse SRL")
                .country("MDA")
                .registrationNumber("MD1003600047892")
                .vatNumber("MD0606714")
                .address("Str. Albinelor 7, Chișinău, Republica Moldova")
                .website("www.honeyhouse.md")
                .phoneNumber("+373 22 456 789")
                .status(CompanyStatus.ACTIVE)
                .ownerId(user.getId())
                .build());

        UUID companyId = company.getId();

        // ── Products ───────────────────────────────────────────────────────────
        Product salcam = productRepository.save(Product.builder()
                .companyId(companyId)
                .name("Miere de salcâm (Acacia) — 500g")
                .category(ProductCategory.FOOD_HONEY)
                .description("Mierea de salcâm HoneyHouse este recoltată primăvara din plantațiile de salcâm din sudul Republicii Moldova. Limpede, cu gust delicat și dulceag, bogată în fructoză — nu cristalizează timp îndelungat. Ideală pentru ceai, deserturi și consum direct.")
                .hsCode("0409.00.00")
                .ingredients("100% miere de salcâm (Robinia pseudoacacia)")
                .packagingType("Borcan de sticlă 500g / 1000g / 3000g")
                .weightGrams(500)
                .organic(true)
                .build());

        Product poliflora = productRepository.save(Product.builder()
                .companyId(companyId)
                .name("Miere polifloră — 500g")
                .category(ProductCategory.FOOD_HONEY)
                .description("Miere crudă, nefiltrată, recoltată din fânețele naturale ale Moldovei în perioada verii. Conținut ridicat de enzime și polifenoli. Cristalizează fin, cu aromă florală complexă — o adevărată capsulă a biodiversității moldovenești.")
                .hsCode("0409.00.00")
                .ingredients("100% miere polifloră")
                .packagingType("Borcan de sticlă 500g / 900g")
                .weightGrams(500)
                .organic(true)
                .build());

        Product tei = productRepository.save(Product.builder()
                .companyId(companyId)
                .name("Miere de tei — 500g")
                .category(ProductCategory.FOOD_HONEY)
                .description("Recoltată exclusiv din livezile de tei din zona Soroca și Orhei, mierea de tei HoneyHouse are aromă intensă de mentol și proprietăți calmante recunoscute. Galbui spre alb, cu cristalizare fină. Recomandată pentru raceala și insomnii.")
                .hsCode("0409.00.00")
                .ingredients("100% miere de tei (Tilia)")
                .packagingType("Borcan de sticlă 500g")
                .weightGrams(500)
                .organic(true)
                .build());

        Product propolis = productRepository.save(Product.builder()
                .companyId(companyId)
                .name("Tinctură de propolis 30% — 30ml")
                .category(ProductCategory.FOOD_HONEY)
                .description("Tinctură naturală de propolis brut moldovenesc, extras la rece în alcool alimentar 70%. Concentrație 30% propolis uscat. Recunoscută pentru proprietăți antibacteriene și imunostimulatoare. Fără coloranți sau conservanți.")
                .hsCode("3003.90.90")
                .ingredients("Propolis brut (30%), alcool alimentar 70%")
                .packagingType("Flacon picurător din sticlă ambră 30ml")
                .weightGrams(60)
                .organic(false)
                .build());

        Product polen = productRepository.save(Product.builder()
                .companyId(companyId)
                .name("Polen de albine uscat — 250g")
                .category(ProductCategory.FOOD_HONEY)
                .description("Polen proaspăt recoltat de la stupii HoneyHouse, uscat la temperaturi sub 40°C pentru păstrarea enzimelor și vitaminelor. Bogat în proteine, aminoacizi esențiali și flavonoizi. Certificat bio, fără tratamente chimice.")
                .hsCode("0409.00.00")
                .ingredients("100% polen de albine uscat")
                .packagingType("Pungă biodegradabilă resigiliabilă 250g")
                .weightGrams(250)
                .organic(true)
                .build());

        // ── Compliance Case 1: Salcâm → România (COMPLIANT) ───────────────────
        ComplianceCase cc1 = complianceCaseRepository.save(ComplianceCase.builder()
                .caseNumber("EXP-2026-HH-001")
                .productId(salcam.getId())
                .companyId(companyId)
                .originCountry("MDA")
                .targetCountry("ROU")
                .status(ComplianceCaseStatus.COMPLIANT)
                .readinessScore(97)
                .aiAnalysisSummary("HoneyHouse îndeplinește toate cerințele pentru exportul de miere de salcâm în România. Certificare Bio obținută (Ecocert), HACCP implementat și auditat, autorizație ANSA activă, etichetare conformă Reg. UE 1169/2011 în română și engleză.")
                .build());

        saveRequirements(cc1.getId(), new Object[][]{
            {"HH-001-01", RequirementType.CERTIFICATION, "Certificare Bio (Ecocert / SANA Moldova)", "Certificarea producției apicole ca bio conform Reg. CE 834/2007 și standardelor SANA Moldova.", RequirementStatus.VERIFIED, "Reg. CE 834/2007, Reg. CE 889/2008", "Certificat Ecocert MD-BIO-2024-0441, valabil 01.03.2024–28.02.2025"},
            {"HH-001-02", RequirementType.HEALTH_SAFETY, "HACCP — Analiza pericolelor și punctele critice de control", "Implementarea și documentarea planului HACCP pentru unitatea de extracție și ambalare.", RequirementStatus.VERIFIED, "Reg. CE 852/2004 Art. 5", "Dosar HACCP auditat de DSP Chișinău, nr. 2024/312"},
            {"HH-001-03", RequirementType.LEGAL, "Autorizație sanitară veterinară ANSA", "Autorizarea unității de procesare miere de către Agenția Națională pentru Siguranța Alimentelor (ANSA).", RequirementStatus.VERIFIED, "Legea 113/2012 privind siguranța alimentelor", "Autorizație ANSA nr. 2024-AP-0078, valabilă până 31.12.2025"},
            {"HH-001-04", RequirementType.LABELING, "Etichetare conformă Reg. CE 1169/2011", "Eticheta trebuie să conțină: denumire produs, țara de origine a mierii, greutate netă, termen de valabilitate, lot, contact producător.", RequirementStatus.VERIFIED, "Reg. UE 1169/2011", "Machet etichetă aprobat, versiuni RO/EN disponibile"},
            {"HH-001-05", RequirementType.CUSTOMS, "Cod vamal HS 0409.00.00 — Miere naturală", "Clasificare tarifară corectă pentru miere, indiferent de tip (monofloră / polifloră).", RequirementStatus.VERIFIED, "Tariful Vamal al Republicii Moldova", "Confirmat de brokerul vamal"},
            {"HH-001-06", RequirementType.TRACEABILITY, "Trasabilitate lot — de la stup la borcan", "Înregistrarea stupilor, datelor de recoltare, procesare și ambalare pentru fiecare lot.", RequirementStatus.VERIFIED, "Reg. CE 178/2002", "Sistem de trasabilitate implementat în ERP intern"},
        });

        // ── Compliance Case 2: Polifloră → Germania (COMPLIANT) ───────────────
        ComplianceCase cc2 = complianceCaseRepository.save(ComplianceCase.builder()
                .caseNumber("EXP-2026-HH-002")
                .productId(poliflora.getId())
                .companyId(companyId)
                .originCountry("MDA")
                .targetCountry("DEU")
                .status(ComplianceCaseStatus.COMPLIANT)
                .readinessScore(92)
                .aiAnalysisSummary("Produsul îndeplinește cerințele germane pentru import miere. Certificare Bio conform standardului DE-ÖKO-007 (Ecocert), analize de reziduuri pesticide și antibiotice conforme cu limita LMR germană. Etichetare în limba germană completată.")
                .build());

        saveRequirements(cc2.getId(), new Object[][]{
            {"HH-002-01", RequirementType.CERTIFICATION, "Certificare Bio DE-ÖKO-007 (recunoaștere Ecocert)", "Recunoașterea certificării bio moldovenești pe piața germană conform echivalenței UE.", RequirementStatus.VERIFIED, "Reg. UE 2018/848", "Certificat Ecocert acceptat sub acordul UE-Moldova privind echivalența bio"},
            {"HH-002-02", RequirementType.HEALTH_SAFETY, "Analize de reziduuri — pesticide și antibiotice", "Testarea fiecărui lot pentru reziduuri de pesticide (limita 0,01 mg/kg) și antibiotice (tetracicline, sulfonamide).", RequirementStatus.VERIFIED, "Directiva 96/23/CE, Reg. CE 396/2005", "Buletin de analiză laborator acreditat DAkkS — lot 2024/Q3"},
            {"HH-002-03", RequirementType.LABELING, "Etichetare în limba germană", "Toate elementele obligatorii conform Reg. UE 1169/2011 traduse și verificate de un traducător autorizat.", RequirementStatus.VERIFIED, "Reg. UE 1169/2011, LMIV Germania", "Machet etichetă DE aprobat intern"},
            {"HH-002-04", RequirementType.LEGAL, "Declarație de conformitate și certificat de origine EUR.1", "Document vamal necesar pentru import preferențial Moldova → UE în cadrul DCFTA.", RequirementStatus.VERIFIED, "Acordul de Asociere RM-UE, DCFTA", "Certificat EUR.1 nr. A 000123 emis de Vama Chișinău"},
            {"HH-002-05", RequirementType.TRACEABILITY, "Înregistrare la autoritatea germană competentă (LGL Bavaria)", "Notificarea importului de produse alimentare de origine animală din țări terțe.", RequirementStatus.VERIFIED, "Reg. CE 882/2004", "Notificare acceptată de Landesamt für Gesundheit und Lebensmittelsicherheit"},
        });

        // ── Compliance Case 3: Propolis → Franța (parțial) ────────────────────
        ComplianceCase cc3 = complianceCaseRepository.save(ComplianceCase.builder()
                .caseNumber("EXP-2026-HH-003")
                .productId(propolis.getId())
                .companyId(companyId)
                .originCountry("MDA")
                .targetCountry("FRA")
                .status(ComplianceCaseStatus.DOCUMENTS_REQUIRED)
                .readinessScore(58)
                .aiAnalysisSummary("Tinctura de propolis este clasificată în Franța ca 'complément alimentaire' și necesită notificare la DGCCRF. Ingredientele active trebuie să respecte lista pozitivă franceză. Etichetarea în franceză este în curs. Autorizarea este fezabilă în 2–3 luni.")
                .build());

        saveRequirements(cc3.getId(), new Object[][]{
            {"HH-003-01", RequirementType.LEGAL, "Notificare DGCCRF — complément alimentaire", "Produsele pe bază de propolis se încadrează ca supliment alimentar în Franța și necesită notificare prealabilă la DGCCRF (Ministerul Economiei).", RequirementStatus.EVIDENCE_UPLOADED, "Décret n° 2006-352 du 20 mars 2006", "Formular de notificare depus online, număr de referință primit"},
            {"HH-003-02", RequirementType.HEALTH_SAFETY, "Evaluare de siguranță EFSA / ANSES", "Substanțele active din propolis trebuie evaluate de ANSES pentru confirmare că nu depășesc limitele de siguranță.", RequirementStatus.PENDING, "Reg. CE 1925/2006, Ghid EFSA", "În așteptarea răspunsului ANSES — estimat 6-8 săptămâni"},
            {"HH-003-03", RequirementType.LABELING, "Etichetare în franceză cu mențiuni specifice suplimentelor", "Eticheta trebuie să conțină doza zilnică recomandată, avertisment pentru copii/gravide, mențiunea 'ne pas dépasser la dose journalière indiquée'.", RequirementStatus.EVIDENCE_UPLOADED, "Décret n° 2006-352, Reg. UE 1169/2011", "Draft etichetă în franceză trimis spre aprobare"},
            {"HH-003-04", RequirementType.CERTIFICATION, "Certificat de analiză în laborator acreditat COFRAC", "Analize fizico-chimice și microbiologice efectuate de un laborator acreditat de COFRAC (acreditare franceză).", RequirementStatus.PENDING, "ISO/CEI 17025", "Probă trimisă la laboratorul Eurofins Paris — rezultate așteptate"},
            {"HH-003-05", RequirementType.CUSTOMS, "Clasificare tarifară CN 3003.90.90 — alte medicamente", "Propolis-ul diluat în alcool poate fi reclasificat de vamă franceză ca medicament, necesitând autorizare AMM.", RequirementStatus.PENDING, "Nomenclatorul Combinat UE", "În analiză cu brokerul vamal și consultantul juridic"},
        });

        // ── Marketplace listing ────────────────────────────────────────────────
        marketplaceListingRepository.save(MarketplaceListing.builder()
                .productId(salcam.getId())
                .companyId(companyId)
                .complianceCaseId(cc1.getId())
                .title("Miere de salcâm bio — HoneyHouse, Republica Moldova")
                .description("HoneyHouse este o apicultură de familie din Chișinău, fondată în 2009, cu peste 350 de stupi amplasați în zonele ecologice din sudul și centrul Republicii Moldova.\n\nMierea noastră de salcâm este recoltată manual, extrasă la rece și ambalată în borcane de sticlă reutilizabilă. Obținem între 12.000 și 15.000 kg de miere pe an, din care 70% este destinată exportului.\n\nCertificate active: Ecocert Bio, HACCP auditat DSP, autorizație ANSA. Disponibil cu certificare EUR.1 pentru import preferențial în UE. Etichetare disponibilă în RO, EN, DE, FR, IT.\n\nParteneri actuali: 3 distribuitori în Germania, 1 în Austria, rețea de magazine bio în România (Republica BIO, Naturalia).")
                .status(ListingStatus.PUBLISHED)
                .certificationSummary("Ecocert Bio (MD-BIO-2024-0441), HACCP, ANSA nr. 2024-AP-0078, EUR.1 DCFTA")
                .targetMarkets("România, Germania, Austria, Franța, Italia")
                .minimumOrderQuantity(100)
                .productionCapacity("1.200 kg/lună")
                .publishedAt(LocalDateTime.now())
                .build());

        log.info("HoneyHouse: 1 user, 1 company, 5 products, 3 compliance cases, 1 listing created.");
    }

    private void seedHoneyHouseCertifications() {
        User user = userRepository.findByEmail("apicul@demo.com").orElseThrow();
        UUID companyId = companyRepository.findByOwnerId(user.getId()).get(0).getId();
        List<Product> products = productRepository.findByCompanyId(companyId);
        Product tei     = products.stream().filter(p -> p.getName().contains("tei")).findFirst().orElseThrow();
        Product polen   = products.stream().filter(p -> p.getName().contains("Polen")).findFirst().orElseThrow();

        // ── Caz 4: Tei → Italia — COMPLIANT 94% ───────────────────────────────
        ComplianceCase cc4 = complianceCaseRepository.save(ComplianceCase.builder()
                .caseNumber("EXP-2026-HH-004")
                .productId(tei.getId())
                .companyId(companyId)
                .originCountry("MDA")
                .targetCountry("ITA")
                .status(ComplianceCaseStatus.COMPLIANT)
                .readinessScore(94)
                .aiAnalysisSummary("Mierea de tei îndeplinește standardele italiene de import. Certificare ISO 22000 obținută, analize reziduuri conforme cu Reg. CE 396/2005, etichetare în italiană validată. Importatorul italian a confirmat conformitatea.")
                .build());

        saveRequirements(cc4.getId(), new Object[][]{
            {"HH-004-01", RequirementType.CERTIFICATION, "ISO 22000:2018 — Sisteme de management al siguranței alimentelor", "Standard internațional pentru managementul siguranței alimentelor pe tot lanțul de aprovizionare, de la producție la distribuție.", RequirementStatus.VERIFIED, "ISO 22000:2018", "Certificat ISO 22000 emis de Bureau Veritas Moldova, nr. MD-FS-2024-0312, valabil 15.04.2024–14.04.2027"},
            {"HH-004-02", RequirementType.CERTIFICATION, "IFS Food v8 — International Featured Standards", "Standard de referință cerut de rețelele mari de retail italian (Conad, Esselunga, Coop). Audit de nivel Superior obținut.", RequirementStatus.VERIFIED, "IFS Food Version 8", "Raport audit IFS: scor 97.4% — nivel Superior. Auditor: SGS Italia, ref. IFS-2024-IT-08821"},
            {"HH-004-03", RequirementType.CERTIFICATION, "Certificare Organic Italia — ICEA / Suolo e Salute", "Recunoașterea certificării bio moldovenești (Ecocert) de către organisme italiene de certificare pentru vânzare cu mențiunea 'biologico'.", RequirementStatus.VERIFIED, "Reg. UE 2018/848, DM 18354/2009", "Echivalență acceptată ICEA nr. 2024/ECO/0445, valabilă până 28.02.2025"},
            {"HH-004-04", RequirementType.HEALTH_SAFETY, "Analize reziduuri pesticide și metale grele — laborator ACCREDIA", "Testarea pentru reziduuri de pesticide (limita UE 0,01 mg/kg) și metale grele (Pb, Cd, Hg, As) conform cerințelor italiene.", RequirementStatus.VERIFIED, "Reg. CE 396/2005, Reg. CE 1881/2006", "Buletin analiză Eurofins Napoli ref. 2024/IT/H-0934 — toate sub LMR"},
            {"HH-004-05", RequirementType.LABELING, "Etichetare în italiană — conformă cu Reg. UE 1169/2011", "Traducere și validare etichetă italiană cu toate elementele obligatorii: denominazione, paese d'origine, peso netto, scadenza, lotto, operatore.", RequirementStatus.VERIFIED, "Reg. UE 1169/2011, D.lgs 231/2017", "Machet etichetă IT aprobat de importatorul Apicoltura Lombarda SRL"},
            {"HH-004-06", RequirementType.CUSTOMS, "Certificat EUR.1 + Declarație de origine preferențială", "Document vamal pentru beneficierea de taxa 0% la import în Italia în cadrul DCFTA Moldova–UE.", RequirementStatus.VERIFIED, "Acordul de Asociere RM-UE, Protocol 1", "EUR.1 nr. A 000187 emis Vama Chișinău, 12.03.2024"},
            {"HH-004-07", RequirementType.TRACEABILITY, "Înregistrare SIAN — Sistem Informativ Agricol Național Italia", "Notificarea loturilor de miere importate în sistemul italian de trasabilitate SIAN, obligatorie pentru distribuție.", RequirementStatus.VERIFIED, "D.lgs 386/2004", "Număr înregistrare SIAN: IT-MD-HH-2024-003, confirmat de importator"},
        });

        // ── Caz 5: Polen → Belgia — EVIDENCE_UPLOADED 71% ────────────────────
        ComplianceCase cc5 = complianceCaseRepository.save(ComplianceCase.builder()
                .caseNumber("EXP-2026-HH-005")
                .productId(polen.getId())
                .companyId(companyId)
                .originCountry("MDA")
                .targetCountry("BEL")
                .status(ComplianceCaseStatus.EVIDENCE_UPLOADED)
                .readinessScore(71)
                .aiAnalysisSummary("Polenul de albine este clasificat în Belgia ca aliment nou (novel food) dacă nu există dovezi de consum semnificativ înainte de 1997. HoneyHouse a inițiat procedura de notificare Novel Food la FASFC. Certificarea BRC Food Safety este în curs de audit.")
                .build());

        saveRequirements(cc5.getId(), new Object[][]{
            {"HH-005-01", RequirementType.LEGAL, "Novel Food — Notificare FASFC Belgia (Reg. UE 2015/2283)", "Polenul de albine poate fi considerat 'novel food' în funcție de istoricul de consum. Procedura de notificare la FASFC (Federal Agency for the Safety of the Food Chain) este obligatorie.", RequirementStatus.EVIDENCE_UPLOADED, "Reg. UE 2015/2283, Reg. UE 2017/2468", "Dosar de notificare novel food depus la FASFC ref. NF-2024-BE-0078 — în evaluare"},
            {"HH-005-02", RequirementType.CERTIFICATION, "BRC Global Standard for Food Safety Issue 9", "Standard cerut de retailerii belgieni (Colruyt, Delhaize). Audit programat la unitatea de procesare din Chișinău.", RequirementStatus.PENDING, "BRC Issue 9 (2022)", "Audit BRC programat pentru 15.11.2024 cu auditorul Bureau Veritas Belgia"},
            {"HH-005-03", RequirementType.CERTIFICATION, "ISO 22000:2018 — certificat recunoscut în Belgia", "Certificatul ISO 22000 obținut pentru cazul Italia este acceptat și de autoritățile belgiene ca bază pentru FSMS.", RequirementStatus.VERIFIED, "ISO 22000:2018", "Certificat Bureau Veritas nr. MD-FS-2024-0312 — recunoscut mutual"},
            {"HH-005-04", RequirementType.HEALTH_SAFETY, "Analize micotoxine, pesticide și metale grele — BELAC", "Laborator acreditat BELAC (acreditare belgiană). Probe de polen testate pentru aflatoxine, ochratoxină A, reziduuri de pesticide apicole (coumaphos, amitraz, fluvalinate).", RequirementStatus.EVIDENCE_UPLOADED, "Reg. CE 396/2005, Reg. CE 1881/2006", "Buletin analiză Eurofins Lenexa ref. 2024/BE/P-0221 — trimis la FASFC"},
            {"HH-005-05", RequirementType.LABELING, "Etichetare în franceză și olandeză (bilingvă)", "Belgia impune etichetare obligatorie în ambele limbi oficiale (FR + NL) pentru produsele distribuite la nivel național.", RequirementStatus.EVIDENCE_UPLOADED, "Reg. UE 1169/2011, AR du 13/09/1999 BE", "Draft etichetă bilingvă FR/NL trimis spre validare distribuitorului BeNatural BVBA"},
            {"HH-005-06", RequirementType.PACKAGING, "Ambalaj resigiliabil conform EN 13427 — testare migrare", "Punga biodegradabilă trebuie să respecte limitele de migrare globală (10 mg/dm²) conform Reg. CE 10/2011 privind materialele plastice.", RequirementStatus.PENDING, "Reg. CE 10/2011, EN 13427", "Probă ambalaj trimisă la SGS Belgia pentru test migrare — rezultate în 3 săptămâni"},
        });

        log.info("HoneyHouse certifications: 2 new compliance cases (ITA + BEL), 13 requirements added.");
    }

    private void saveRequirements(UUID caseId, Object[][] reqs) {
        for (Object[] r : reqs) {
            requirementRepository.save(Requirement.builder()
                    .complianceCaseId(caseId)
                    .code((String) r[0])
                    .type((RequirementType) r[1])
                    .title((String) r[2])
                    .description((String) r[3])
                    .status((RequirementStatus) r[4])
                    .legalBasis((String) r[5])
                    .evidenceRequired((String) r[6])
                    .aiGenerated(true)
                    .aiConfidence(0.92)
                    .build());
        }
    }
}
