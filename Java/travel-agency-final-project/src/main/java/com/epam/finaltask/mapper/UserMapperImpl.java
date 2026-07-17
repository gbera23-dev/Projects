package com.epam.finaltask.mapper;

import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.model.Role;
import com.epam.finaltask.model.User;
import com.epam.finaltask.model.Voucher;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class UserMapperImpl implements UserMapper{
    VoucherMapper voucherMapper;

    public UserMapperImpl(VoucherMapper voucherMapper) {
        this.voucherMapper = voucherMapper;
    }
    private List<VoucherDTO> getVoucherDTOList(List<Voucher> list) {
        if(list == null)return null;
        List<VoucherDTO> DTOlist = new ArrayList<>();
        for(Voucher voucher : list)DTOlist.add(voucherMapper.toVoucherDTO(voucher));
        return DTOlist;
    }
    private List<Voucher> getVoucherList(List<VoucherDTO> DTOlist) {
        if(DTOlist == null)return null;
        List<Voucher> list = new ArrayList<>();
        for(VoucherDTO voucherDTO : DTOlist)list.add(voucherMapper.toVoucher(voucherDTO));
        return list;
    }
    @Override
    public User toUser(UserDTO userDTO) {
        return User.getNewUser(UUID.fromString(userDTO.getId()), userDTO.getUsername(),
                Role.valueOf(userDTO.getRole()), userDTO.getPassword(),
                getVoucherList(userDTO.getVouchers()),
                userDTO.getPhoneNumber(), BigDecimal.valueOf(userDTO.getBalance()), userDTO.isActive());
    }

    @Override
    public UserDTO toUserDTO(User user) {
        return UserDTO.getNewUserDTO(user.getId().toString(), user.getUsername(), user.getPassword(), user.getRole().toString(),
                getVoucherDTOList(user.getVouchers()),
                user.getPhoneNumber(), user.getBalance().doubleValue(), user.isActive());
    }
}
