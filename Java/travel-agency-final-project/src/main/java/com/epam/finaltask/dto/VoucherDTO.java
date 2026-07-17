package com.epam.finaltask.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@ToString
public class VoucherDTO {

    private String id;

    private String title;

    private String description;

    private Double price;

    private String tourType;

    private String transferType;

    private String hotelType;

    private String status;

    private LocalDate arrivalDate;

    private LocalDate evictionDate;

    private UUID userId;

    private Boolean isHot;

    public static VoucherDTO getNewVoucherDTO(String id, String title, String description, Double price,
									   String tourType, String transferType, String hotelType, String status,
									   LocalDate arrivalDate, LocalDate evictionDate, UUID userId, Boolean isHot) {
		VoucherDTO voucherDTO = new VoucherDTO();
		voucherDTO.id = id == null ? UUID.randomUUID().toString() : id;
		voucherDTO.title = title;
		voucherDTO.description = description;
		voucherDTO.price = price;
		voucherDTO.tourType = tourType;
		voucherDTO.transferType = transferType;
		voucherDTO.hotelType = hotelType;
		voucherDTO.status = status;
		voucherDTO.arrivalDate = arrivalDate;
		voucherDTO.evictionDate = evictionDate;
		voucherDTO.userId = userId;
		voucherDTO.isHot = isHot;
		return voucherDTO;
	}
	public List<String> toList() {
		return List.of(
				"ID:    " +this.id,
				"TITLE:    "+this.title,
				"DESCRIPTION:    "+this.description,
				"PRICE(DOLLAR):    "+this.price.toString(),
				"TOUR TYPE:    "+this.tourType,
				"TRANSFER TYPE:    "+this.transferType,
				"HOTEL TYPE:    "+this.hotelType,
				"STATUS:    "+this.status,
				"ARRIVAL DATE:    "+this.arrivalDate.toString(),
				"EVICTION DATE:    "+this.evictionDate.toString(),
				"USER:    "+this.userId
		);
	}
}
