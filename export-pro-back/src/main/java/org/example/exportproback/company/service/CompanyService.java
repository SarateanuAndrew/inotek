package org.example.exportproback.company.service;

import lombok.RequiredArgsConstructor;
import org.example.exportproback.auth.domain.User;
import org.example.exportproback.common.exception.ResourceNotFoundException;
import org.example.exportproback.company.domain.Company;
import org.example.exportproback.company.domain.CompanyStatus;
import org.example.exportproback.company.dto.CompanyDto;
import org.example.exportproback.company.dto.CreateCompanyRequest;
import org.example.exportproback.company.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyDto create(CreateCompanyRequest request, User currentUser) {
        Company company = Company.builder()
                .name(request.getName())
                .country(request.getCountry())
                .registrationNumber(request.getRegistrationNumber())
                .vatNumber(request.getVatNumber())
                .address(request.getAddress())
                .website(request.getWebsite())
                .phoneNumber(request.getPhoneNumber())
                .status(CompanyStatus.PENDING)
                .ownerId(currentUser.getId())
                .build();
        return toDto(companyRepository.save(company));
    }

    public List<CompanyDto> findByCurrentUser(User currentUser) {
        return companyRepository.findByOwnerId(currentUser.getId()).stream().map(this::toDto).toList();
    }

    public CompanyDto findById(UUID id) {
        return toDto(companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company", id)));
    }

    private CompanyDto toDto(Company c) {
        CompanyDto dto = new CompanyDto();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setCountry(c.getCountry());
        dto.setRegistrationNumber(c.getRegistrationNumber());
        dto.setVatNumber(c.getVatNumber());
        dto.setAddress(c.getAddress());
        dto.setWebsite(c.getWebsite());
        dto.setPhoneNumber(c.getPhoneNumber());
        dto.setStatus(c.getStatus());
        dto.setOwnerId(c.getOwnerId());
        dto.setCreatedAt(c.getCreatedAt());
        return dto;
    }
}
