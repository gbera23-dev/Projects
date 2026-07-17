package com.epam.finaltask.service;


import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.exception.userExceptions.UserInsufficientFundsException;
import com.epam.finaltask.exception.userExceptions.UserNotFoundException;
import com.epam.finaltask.exception.voucherExceptions.VoucherAlreadyPurchasedException;
import com.epam.finaltask.exception.voucherExceptions.VoucherCancelledException;
import com.epam.finaltask.exception.voucherExceptions.VoucherCouldNotBeSavedException;
import com.epam.finaltask.exception.voucherExceptions.VoucherNotFoundException;
import com.epam.finaltask.mapper.UserMapper;
import com.epam.finaltask.mapper.VoucherMapper;
import com.epam.finaltask.mapper.VoucherMapperImpl;
import com.epam.finaltask.model.*;
import com.epam.finaltask.repository.UserRepository;
import com.epam.finaltask.repository.VoucherRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class VoucherServiceImpl implements VoucherService{

    private final VoucherRepository voucherRepository;
    private final UserService userService;
    private final VoucherMapper voucherMapper;
    private final UserMapper userMapper;
    public VoucherServiceImpl(VoucherRepository voucherRepository, UserService userService,
                              VoucherMapper voucherMapper, EntityManager entityManager, UserMapper userMapper) {
        this.voucherRepository = voucherRepository;
        this.userService = userService;
        this.voucherMapper = voucherMapper;
        this.userMapper = userMapper;
    }

    @Override
    public VoucherDTO create(VoucherDTO voucherDTO) {
        log.info("Creating voucher with id {}", voucherDTO.getId());
        Voucher voucher = voucherMapper.toVoucher(voucherDTO);
        try {
            voucher = voucherRepository.save(voucher);
        } catch(Exception e) {
            log.error("Failed to save a new voucher with id {}", voucherDTO.getId());
            throw new VoucherCouldNotBeSavedException("Could not save the voucher with id "+ voucherDTO.getId());
        }
        log.info("Successfully created a new voucher with id {}", voucherDTO.getId());
        return voucherMapper.toVoucherDTO(voucher);
    }

    @Override
    @Transactional
    public VoucherDTO order(String id, String userId) {
        log.info("User with id {} started ordering a voucher with id {}", userId, id);
        Voucher voucher = purchaseNewVoucher(id, userId);
        UserDTO userDTO = userService.getUserById(UUID.fromString(userId));
        List<VoucherDTO> currentVouchers = userDTO.getVouchers();
        if(currentVouchers == null) {
            currentVouchers = new ArrayList<>();
        }
        currentVouchers.add(voucherMapper.toVoucherDTO(voucher));
        userDTO.setVouchers(currentVouchers); userDTO.setBalance(userDTO.getBalance() - voucher.getPrice());
        userService.updateUser(userDTO.getUsername(), userDTO);
        log.info("Voucher with id {} was Successfully ordered by user with id {}", id, userId);
        return voucherMapper.toVoucherDTO(voucher);
    }

    @Override
    public VoucherDTO update(String id, VoucherDTO voucherDTO) {
        log.info("Started updating the voucher with id {}", id);
        Voucher updatedVoucher = voucherMapper.toVoucher(voucherDTO);
        Voucher voucher =  null;
        try {
            voucher = voucherRepository.save(updatedVoucher);
        } catch(Exception e) {
            log.error("Could not save the updated voucher with id {}", id);
            throw new VoucherCouldNotBeSavedException("Could not save the voucher with id "+ id);
        }
        log.info("Voucher with id {} was successfully updated!", id);
         return voucherMapper.toVoucherDTO(voucher);
    }

    @Override
    public void delete(String voucherId) {
        log.info("Started deleting the voucher with id {}", voucherId);
        Voucher voucher = null;
        try {
            voucher = voucherRepository.findById(UUID.fromString(voucherId)).get();
        } catch(NoSuchElementException e){
            log.error("Could not find the voucher with id {}", voucherId);
            throw new VoucherNotFoundException("Voucher with id " + voucherId +" does not exist!");
        }
         User user = voucher.getUser();
        if(user == null) {
            log.warn("Voucher with id {} is not owned by any user!", voucherId);
        }
        else {
            deleteVoucherFromUserList(user, voucherId, voucher.getPrice());
        }
        try {
            voucherRepository.deleteById(UUID.fromString(voucherId));
        }catch(IllegalArgumentException e) {
            log.error("Could not delete the voucher with id {}", voucherId);
            throw new VoucherNotFoundException("Could not find the voucher with id "+ voucherId);
        }
        log.info("Voucher with id {} was successfully deleted!", voucherId);
    }

    @Override
    public VoucherDTO changeHotStatus(String id, VoucherDTO voucherDTO) {
        log.info("Changing hot status of voucher with id {}", id);
        Voucher voucher = null;
        try {
            voucher = voucherRepository.findById(UUID.fromString(id)).get();
        } catch(Exception e) {
            log.error("Could not find the voucher with id {}", id);
            throw new VoucherNotFoundException("Voucher with id " + id +" does not exist!");
        }
        voucher.setHot(voucherDTO.getIsHot());
        VoucherDTO updatedVoucher = null;
        try {
            updatedVoucher = voucherMapper.toVoucherDTO(voucherRepository.save(voucher));
        } catch(Exception e) {
            log.error("Could not save the voucher with id {}", id);
            throw new VoucherCouldNotBeSavedException("Could not save the voucher with id "+ id);
        }
        log.info("Successfully changed the hot status for the voucher with id {}", id);
        return updatedVoucher;
    }

    @Override
    public VoucherDTO find(String voucherId) {
        log.info("Finding the voucher with id {}", voucherId);
        Voucher voucher = null;
        try {
             voucher = voucherRepository.findById(UUID.fromString(voucherId)).get();
        } catch(Exception e) {
            log.error("Could not find voucher with id {}", voucherId);
            throw new VoucherNotFoundException("Voucher with id " + voucherId +" does not exist!");
        }
        log.info("Voucher with id {} was obtained successfully", voucherId);
        return voucherMapper.toVoucherDTO(voucher);
    }

    @Override
    public List<VoucherDTO> findAllByUserId(String userId) {
        log.info("Finding all vouchers by userId {}", userId);
        if(userId == null) {
            log.warn("userId is not provided");
            return null;
        }
        List<Voucher> list = voucherRepository.findAllByUserId(UUID.fromString(userId));
        List<VoucherDTO> DTOlist = new ArrayList<>();
        for(Voucher voucher : list) {
            DTOlist.add(voucherMapper.toVoucherDTO(voucher));
        }
        log.info("Successfully Obtained all vouchers by userId {}", userId);
        return DTOlist;
    }

    @Override
    public List<VoucherDTO> findAllByTourType(TourType tourType) {
        log.info("Finding all vouchers by tourType {}", tourType);
        if(tourType == null) {
            log.warn("tourType is not provided");
            return null;
        }
        List<Voucher> list = voucherRepository.findAllByTourType(tourType);
        List<VoucherDTO> DTOlist = new ArrayList<>();
        for(Voucher voucher : list) {
            DTOlist.add(voucherMapper.toVoucherDTO(voucher));
        }
        log.info("Successfully Obtained all vouchers by tourType {}", tourType);
        return DTOlist;
    }

    @Override
    public List<VoucherDTO> findAllByTransferType(String transferType) {
        log.info("Finding all vouchers by transferType {}", transferType);
        if(transferType == null) {
            log.warn("transferType is not provided");
            return null;
        }
        List<Voucher> list = voucherRepository.findAllByTransferType(TransferType.valueOf(transferType));
        List<VoucherDTO> DTOlist = new ArrayList<>();
        for(Voucher voucher : list) {
            DTOlist.add(voucherMapper.toVoucherDTO(voucher));
        }
        log.info("Successfully Obtained all vouchers by transferType {}", transferType);
        return DTOlist;
    }

    @Override
    public List<VoucherDTO> findAllByPrice(Double price) {
        log.info("Finding all vouchers by {}", price);
        if(price == null) {
            log.warn("price is not provided");
            return null;
        }
        List<Voucher> list = voucherRepository.findAllByPrice(price);
        List<VoucherDTO> DTOlist = new ArrayList<>();
        for(Voucher voucher : list) {
            DTOlist.add(voucherMapper.toVoucherDTO(voucher));
        }
        log.info("Successfully Obtained all vouchers by price {}", price);
        return DTOlist;
    }

    @Override
    public List<VoucherDTO> findAllByHotelType(HotelType hotelType) {
        log.info("Finding all vouchers by {}", hotelType);
        if(hotelType == null) {
            log.warn("HotelType is not provided");
            return null;
        }
        List<Voucher> list = voucherRepository.findAllByHotelType(hotelType);
        List<VoucherDTO> DTOlist = new ArrayList<>();
        for(Voucher voucher : list) {
            DTOlist.add(voucherMapper.toVoucherDTO(voucher));
        }
        log.info("Successfully Obtained all vouchers by hotelType {}", hotelType);
        return DTOlist;
    }

    @Override
    public List<VoucherDTO> findAll() {
        log.info("Finding all vouchers!");
        List<Voucher> list = voucherRepository.findAll();
        List<VoucherDTO> DTOlist = new ArrayList<>();
        for(Voucher voucher : list) {
            DTOlist.add(voucherMapper.toVoucherDTO(voucher));
        }
        log.info("All vouchers were successfully obtained!");
        return DTOlist;
    }

    @Override
    public List<VoucherDTO> filter(VouchersFilterRequest vouchersFilterRequest) {
        log.info("Filtering vouchers based on requirements");
        String tour = vouchersFilterRequest.getTourType();
        String transfer = vouchersFilterRequest.getTransferType();
        String hotel = vouchersFilterRequest.getHotelType();
        Double price = vouchersFilterRequest.getPrice();

        TourType tourType = tour != null && !tour.isEmpty() ?
                TourType.valueOf(tour) : null;

        TransferType transferType = transfer != null && !transfer.isEmpty() ?
                TransferType.valueOf(transfer) : null;

        HotelType hotelType = hotel != null && !hotel.isEmpty() ?
                HotelType.valueOf(hotel) : null;

        List<Voucher> filteredList = voucherRepository.filterVouchers(tourType, transferType, hotelType, price);
        List<VoucherDTO> DTOlist = new ArrayList<>();
        for(Voucher voucher : filteredList) {
            DTOlist.add(voucherMapper.toVoucherDTO(voucher));
        }
        DTOlist.sort(Comparator.comparing(VoucherDTO::getIsHot).reversed());
        log.info("All vouchers were successfully filtered!");
        return DTOlist;
    }

    @Override
    public VoucherDTO changeCondition(String id, VoucherDTO voucherDTO) {
        log.info("Changing condition of voucher with id {}", id);
        Voucher voucher = null;
        try {
            voucher = voucherRepository.findById(UUID.fromString(id)).get();
        } catch(Exception e) {
            log.error("Could not find the voucher with id {}", id);
            throw new VoucherNotFoundException("Voucher with id " + id +" does not exist!");
        }
        voucher.setStatus(VoucherStatus.valueOf(voucherDTO.getStatus()));
        VoucherDTO updatedVoucher = null;
        try {
            updatedVoucher = voucherMapper.toVoucherDTO(voucherRepository.save(voucher));
        } catch(Exception e) {
            log.error("Could not save the voucher with id {}", id);
            throw new VoucherCouldNotBeSavedException("Could not save the voucher with id "+ id);
        }
        log.info("Successfully changed the condition of the voucher with id {}", id);
        return updatedVoucher;
    }

    private Voucher purchaseNewVoucher(String id, String userId) {
        Voucher voucher = null;
        try {
            voucher = voucherRepository.findById(UUID.fromString(id)).get();
        } catch(Exception e) {
            log.error("Could not find the voucher with id {}", id);
            throw new VoucherNotFoundException("Voucher with id " + userId +" does not exist!");
        }
        User user = userMapper.toUser(userService.getUserById(UUID.fromString(userId)));
        double userBalance = user.getBalance().doubleValue();
        double voucherPrice = voucher.getPrice();
        if(userBalance < voucherPrice) {
            log.error("User with id {} cannot purchase voucher with id {}. Insufficient funds", userId,
                    voucher.getId());
            throw new UserInsufficientFundsException("User does not have enough funds to purchase the voucher");
        }
        if(voucher.getStatus().name().equals("CANCELED")) {
            log.error("User with id {} cannot purchase voucher with id {}. Voucher is cancelled", userId,
                    voucher.getId());
            throw new VoucherCancelledException("Voucher is cancelled. cannot be purchased");
        }
        if(voucher.getStatus().name().equals("PAID")) {
            log.error("User with id {} cannot purchase voucher with id {}. Voucher is already purchased",
                    userId, voucher.getId());
            throw new VoucherAlreadyPurchasedException("Voucher is already purchased");
        }

        voucher.setUser(user); voucher.setStatus(VoucherStatus.PAID);
        try {
            voucherRepository.save(voucher);
        } catch(Exception e) {
            log.error("Could not save the ordered voucher!");
            throw new VoucherCouldNotBeSavedException("Could not save the voucher with id "+ id);
        }
        return voucher;
    }

    private void deleteVoucherFromUserList(User user, String voucherId, double voucherPrice) {
        List<Voucher> list = user.getVouchers();
        if(list == null) {
            log.warn("User with id {} has no vouchers, yet voucher with id {} must be his", user.getId(), voucherId);
        }
        else {
            for (Voucher elem : list) {
                if(elem.getId().equals(UUID.fromString(voucherId))) {
                    list.remove(elem);
                    break;
                }
            }
        }
        double userBalance = user.getBalance().doubleValue();
        user.setBalance(BigDecimal.valueOf(userBalance + voucherPrice));
        log.info("Refunded user with amount {} for purchasing voucher with id {}", voucherPrice, voucherId);
        userService.updateUser(user.getUsername(), userMapper.toUserDTO(user));
    }
}
