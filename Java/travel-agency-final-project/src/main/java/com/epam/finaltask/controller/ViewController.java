package com.epam.finaltask.controller;


import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.model.*;
import com.epam.finaltask.service.JWTService;
import com.epam.finaltask.service.UserService;
import com.epam.finaltask.service.VoucherService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class ViewController {

    VoucherService voucherService;
    UserService userService;

    public ViewController(VoucherService voucherService, UserService userService) {
        this.voucherService = voucherService;
        this.userService = userService;
    }

    @GetMapping("/auth/sign-in")
    String goToLogin(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/sign-in";
    }

    @GetMapping("/auth/sign-up")
    String goToRegistrationPage(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "auth/sign-up";
    }

    @GetMapping("/auth/sign-out")
    String signOut(Model model) {
        return "auth/sign-out";
    }

    @GetMapping("/user/mainPage")
    String goToMainPage(@ModelAttribute VouchersFilterRequest vouchersFilterRequest, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().toString().equals("[ROLE_ANONYMOUS]")) {
            return "redirect:/auth/sign-in";
        }
        List<VoucherDTO> voucherList = voucherService.filter(vouchersFilterRequest);
        model.addAttribute("vouchers", voucherList);
        return "user/dashboard";
    }

    @GetMapping("/user/profile")
    String getProfile(Model model) {
        model.addAttribute("infoUpdateRequest", new InfoUpdateRequest());
        return "user/Profile";
    }

    @GetMapping("/vouchers/order/{id}")
    String orderVoucher(Model model, @PathVariable String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().toString().equals("[ROLE_ANONYMOUS]")) {
            return "redirect:/auth/sign-in";
        }
        VoucherDTO voucher = voucherService.find(id);
        model.addAttribute("voucherInfo", voucher.toList());
        model.addAttribute("voucherId", id);
        return "voucher/order";
    }

    @GetMapping("/vouchers/edit/{id}")
    String editVoucher(Model model, @PathVariable String id) {
        System.out.println("ummmm....");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().toString().equals("[ROLE_ANONYMOUS]")) {
            return "redirect:/auth/sign-in";
        }
        VoucherDTO voucher = voucherService.find(id);
        model.addAttribute("voucherInfo", voucher.toList());
        model.addAttribute("voucherId", id);
        return "voucher/edit";
    }


    @GetMapping("/favicon.ico")
    String loadFavicon(Model model) {
        return "index";
    }

}
