package com.epam.finaltask.model;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
public class Voucher {
    @Id
    private UUID id;

    private String title;

    private String description;

    private Double price;

    private TourType tourType;

    private TransferType transferType;

    private HotelType hotelType;

    private VoucherStatus status;

    private LocalDate arrivalDate;

    private LocalDate evictionDate;

    @ManyToOne
    private User user;

    private boolean isHot;

    public static Voucher getNewVoucher(String title, UUID id, Double price, String description,
                   TransferType transferType, TourType tourType, HotelType hotelType,
                   VoucherStatus status, LocalDate arrivalDate, LocalDate evictionDate,
                   User user, boolean isHot) {
        Voucher voucher = new Voucher();
        voucher.title = title;
        voucher.id = id;
        voucher.price = price;
        voucher.description = description;
        voucher.transferType = transferType;
        voucher.tourType = tourType;
        voucher.hotelType = hotelType;
        voucher.status = status;
        voucher.arrivalDate = arrivalDate;
        voucher.evictionDate = evictionDate;
        voucher.user = user;
        voucher.isHot = isHot;
        return voucher;
    }

}
