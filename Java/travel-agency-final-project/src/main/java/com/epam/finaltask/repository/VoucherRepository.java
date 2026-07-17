package com.epam.finaltask.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.finaltask.model.HotelType;
import com.epam.finaltask.model.TourType;
import com.epam.finaltask.model.TransferType;
import com.epam.finaltask.model.Voucher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoucherRepository extends JpaRepository<Voucher, UUID> {
    List<Voucher> findAllByUserId(UUID userId);
    List<Voucher> findAllByTourType(TourType tourType);
    List<Voucher> findAllByTransferType(TransferType transferType);
    List<Voucher> findAllByPrice(Double price);
    List<Voucher> findAllByHotelType(HotelType hotelType);
    @Query("SELECT v FROM Voucher v WHERE (:tourType IS NULL OR v.tourType = :tourType) AND (:transferType IS NULL OR v.transferType = :transferType) AND (:hotelType IS NULL OR v.hotelType = :hotelType) AND (:price IS NULL OR v.price <= :price)")
    List<Voucher> filterVouchers(@Param("tourType") TourType tourType, @Param("transferType") TransferType transferType, @Param("hotelType") HotelType hotelType, @Param("price") Double price);
}
