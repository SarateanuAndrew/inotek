package org.example.exportproback.distributor.service;

import lombok.RequiredArgsConstructor;
import org.example.exportproback.auth.domain.User;
import org.example.exportproback.common.exception.ResourceNotFoundException;
import org.example.exportproback.distributor.domain.Distributor;
import org.example.exportproback.distributor.domain.DistributorStatus;
import org.example.exportproback.distributor.dto.DistributorDto;
import org.example.exportproback.distributor.dto.RegisterDistributorRequest;
import org.example.exportproback.distributor.repository.DistributorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DistributorService {

    private final DistributorRepository distributorRepository;

    public DistributorDto register(RegisterDistributorRequest request, User user) {
        if (distributorRepository.existsByCui(request.getCui())) {
            throw new IllegalArgumentException("CUI already registered");
        }
        Distributor distributor = Distributor.builder()
                .userId(user.getId())
                .companyName(request.getCompanyName())
                .cui(request.getCui())
                .vatNumber(request.getVatNumber())
                .status(DistributorStatus.PENDING)
                .build();
        return toDto(distributorRepository.save(distributor));
    }

    public DistributorDto verify(UUID id) {
        Distributor distributor = distributorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distributor", id));
        distributor.setStatus(DistributorStatus.VERIFIED);
        distributor.setVerifiedAt(LocalDateTime.now());
        return toDto(distributorRepository.save(distributor));
    }

    public DistributorDto reject(UUID id, String reason) {
        Distributor distributor = distributorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distributor", id));
        distributor.setStatus(DistributorStatus.REJECTED);
        distributor.setRejectionReason(reason);
        return toDto(distributorRepository.save(distributor));
    }

    public java.util.Optional<DistributorDto> findByUserId(UUID userId) {
        return distributorRepository.findByUserId(userId).map(this::toDto);
    }

    private DistributorDto toDto(Distributor d) {
        DistributorDto dto = new DistributorDto();
        dto.setId(d.getId());
        dto.setUserId(d.getUserId());
        dto.setCompanyName(d.getCompanyName());
        dto.setCui(d.getCui());
        dto.setVatNumber(d.getVatNumber());
        dto.setStatus(d.getStatus());
        dto.setRejectionReason(d.getRejectionReason());
        dto.setVerifiedAt(d.getVerifiedAt());
        dto.setCreatedAt(d.getCreatedAt());
        return dto;
    }
}
