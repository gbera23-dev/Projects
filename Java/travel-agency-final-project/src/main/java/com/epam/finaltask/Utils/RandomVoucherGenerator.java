package com.epam.finaltask.Utils;

import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.mapper.VoucherMapper;
import com.epam.finaltask.mapper.VoucherMapperImpl;
import com.epam.finaltask.model.*;

import java.util.random.*;
import java.time.LocalDate;
import java.util.*;

import static java.lang.Math.abs;

public class RandomVoucherGenerator {
    static Random random = new Random();
    static VoucherMapper voucherMapper = new VoucherMapperImpl(null);
    static List<String> titles = List.of("Sun & Sand Paradise Pass",
        "Escape Velocity Voucher",
        "Wanderlust Wanderer Voucher",
        "Azure Horizon Getaway",
        "Cozy Corner Cabin Credit",
        "Jetset Jewel Journey",
        "Tranquil Tide Traveler",
        "Mountain Majesty Memo",
        "Nomadic Adventure Pass",
        "Rick and Morty adventure",
        "Urban Explorer Voucher",
        "Serenity Seekers Stipend",
        "Voyagers Value Voucher",
    "Paradise Found Pass",
    "Rustic Retreat Reward",
    "Roam & Roar Voucher",
    "Skyline Safari Token",
    "Dream Destination Debit",
    "Pacific Peak Pass",
    "The Wanderful Voucher");
    static List<String> descriptions = List.of("The greatest experience!",
            "For adventure lovers!", "Welcome!");
    static List<TourType> tourTypeList = List.of(TourType.values());
    static List<TransferType> transferTypeList = List.of(TransferType.values());
    static List<HotelType> hotelTypeList = List.of(HotelType.values());
    static List<VoucherStatus> voucherStatusList = List.of(VoucherStatus.values());

    public static Voucher generateRandomVoucher() {
        User user = new User(); user.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        return Voucher.getNewVoucher(
                titles.get(abs(random.nextInt()) % titles.size()),
                UUID.randomUUID(),
                abs(random.nextDouble()) + 1000,
                descriptions.get(abs(random.nextInt()) % descriptions.size()),
                transferTypeList.get(abs(random.nextInt()) % transferTypeList.size()),
                tourTypeList.get(abs(random.nextInt()) % tourTypeList.size()),
                hotelTypeList.get(abs(random.nextInt()) % hotelTypeList.size()),
                voucherStatusList.get(abs(random.nextInt()) % voucherStatusList.size()),
                LocalDate.now(), LocalDate.now().plusDays(abs(random.nextInt()) % 100),
                user, random.nextBoolean());
    }

    public static VoucherDTO generateRandomVoucherDTO() {
        return voucherMapper.toVoucherDTO(generateRandomVoucher());
    }

    public static List<Object> generateVoucherAndVoucherDTO() {
        Voucher voucher = generateRandomVoucher();
        return List.of(voucher, voucherMapper.toVoucherDTO(voucher));
    }

}
