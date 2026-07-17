package com.epam.finaltask.model;

import com.epam.finaltask.dto.VoucherDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class VouchersFilterRequest {

    private String tourType;

    private String transferType;

    private Double price;

    private String hotelType;

}
