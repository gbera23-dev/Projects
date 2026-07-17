package com.epam.finaltask.runners;

import com.epam.finaltask.Utils.RandomVoucherGenerator;
import com.epam.finaltask.model.*;
import com.epam.finaltask.repository.VoucherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Order(3)
public class VouchersInit implements CommandLineRunner {

    private final VoucherRepository voucherRepository;

    public VouchersInit(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        for(int i = 0; i < 100; i++) {
            voucherRepository.save(RandomVoucherGenerator.generateRandomVoucher());
        }
        System.out.println("Successfully initialized 100 vouchers!");
    }
}
