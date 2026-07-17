package com.epam.finaltask.mapper;

import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.model.*;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class VoucherMapperImpl implements VoucherMapper{

    EntityManager entityManager;

    public VoucherMapperImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Voucher toVoucher(VoucherDTO voucherDTO) {
        return Voucher.getNewVoucher(voucherDTO.getTitle(),UUID.fromString(voucherDTO.getId()),voucherDTO.getPrice(),
                voucherDTO.getDescription(), TransferType.valueOf(voucherDTO.getTransferType()),
                TourType.valueOf(voucherDTO.getTourType()),
                HotelType.valueOf(voucherDTO.getHotelType()), VoucherStatus.valueOf(voucherDTO.getStatus()),
                voucherDTO.getArrivalDate(), voucherDTO.getEvictionDate(),
                (voucherDTO.getUserId() == null || voucherDTO.getUserId().equals(UUID.fromString("00000000-0000-0000-0000-000000000000")))
                ? null : entityManager.find(User.class, voucherDTO.getUserId()),
                voucherDTO.getIsHot());
    }

    @Override
    public VoucherDTO toVoucherDTO(Voucher voucher) {
        return VoucherDTO.getNewVoucherDTO(voucher.getId().toString(), voucher.getTitle(), voucher.getDescription(),
                voucher.getPrice(), voucher.getTourType().toString(), voucher.getTransferType().toString(),
                voucher.getHotelType().toString(), voucher.getStatus().toString(),
                voucher.getArrivalDate(), voucher.getEvictionDate(),
                voucher.getUser() == null ? UUID.fromString("00000000-0000-0000-0000-000000000000") : voucher.getUser().getId(),
                voucher.isHot());
    }
}
