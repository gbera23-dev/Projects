package com.oop.web_project;

import com.oop.web_project.annotations.ActivityCheckRequired;
import com.oop.web_project.aspect.activity.ActivityCheckAspect;
import com.oop.web_project.entities.CheckActivityTarget;
import com.oop.web_project.exceptions.accountExceptions.AccountIsNotActiveException;
import com.oop.web_project.exceptions.cardExceptions.CardIsNotActiveException;
import com.oop.web_project.exceptions.customerExceptions.CustomerIsNotActiveException;
import com.oop.web_project.persistence.AccountRepository;
import com.oop.web_project.persistence.CardRepository;
import com.oop.web_project.persistence.CustomerRepository;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivityCheckAspectTest {

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void setUpSecurityContext(String email) {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(email);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
    }

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private ActivityCheckAspect activityCheckAspect;

    private JoinPoint joinPoint;
    private ActivityCheckRequired activityCheckRequired;

    @BeforeEach
    void setUp() {
        joinPoint = mock(JoinPoint.class);
        activityCheckRequired = mock(ActivityCheckRequired.class);
    }

    @Test
    void testCheckEntityActivityAccountActiveEntityExists() {
        long id = 1L;
        when(activityCheckRequired.checkActivityTarget()).thenReturn(CheckActivityTarget.ACCOUNT);
        when(joinPoint.getArgs()).thenReturn(new Object[]{id});
        when(accountRepository.existsByIdAndIsActiveTrue(id)).thenReturn(true);

        assertDoesNotThrow(() -> activityCheckAspect.checkEntityActivity(joinPoint, activityCheckRequired));
    }

    @Test
    void testCheckEntityActivityAccountNotActiveEntityThrows() {
        long id = 1L;
        when(activityCheckRequired.checkActivityTarget()).thenReturn(CheckActivityTarget.ACCOUNT);
        when(joinPoint.getArgs()).thenReturn(new Object[]{id});
        when(accountRepository.existsByIdAndIsActiveTrue(id)).thenReturn(false);

        assertThrows(AccountIsNotActiveException.class,
                () -> activityCheckAspect.checkEntityActivity(joinPoint, activityCheckRequired));
    }

    @Test
    void testCheckEntityActivityCardActiveEntityExists() {
        long id = 2L;
        when(activityCheckRequired.checkActivityTarget()).thenReturn(CheckActivityTarget.CARD);
        when(joinPoint.getArgs()).thenReturn(new Object[]{id});
        when(cardRepository.existsByIdAndIsActiveTrue(id)).thenReturn(true);

        assertDoesNotThrow(() -> activityCheckAspect.checkEntityActivity(joinPoint, activityCheckRequired));
    }

    @Test
    void testCheckEntityActivityCardNotActiveEntityThrows() {
        long id = 2L;
        when(activityCheckRequired.checkActivityTarget()).thenReturn(CheckActivityTarget.CARD);
        when(joinPoint.getArgs()).thenReturn(new Object[]{id});
        when(cardRepository.existsByIdAndIsActiveTrue(id)).thenReturn(false);

        assertThrows(CardIsNotActiveException.class,
                () -> activityCheckAspect.checkEntityActivity(joinPoint, activityCheckRequired));
    }

    @Test
    void testCheckEntityActivityCustomerActiveEntityExists() {
        setUpSecurityContext("active@example.com");
        when(activityCheckRequired.checkActivityTarget()).thenReturn(CheckActivityTarget.CUSTOMER);
        when(customerRepository.existsByEmailAndIsActiveTrue("active@example.com")).thenReturn(true);

        assertDoesNotThrow(() -> activityCheckAspect.checkEntityActivity(joinPoint, activityCheckRequired));
    }

    @Test
    void testCheckEntityActivityCustomerNotActiveEntityThrows() {
        setUpSecurityContext("inactive@example.com");
        when(activityCheckRequired.checkActivityTarget()).thenReturn(CheckActivityTarget.CUSTOMER);
        when(customerRepository.existsByEmailAndIsActiveTrue("inactive@example.com")).thenReturn(false);

        assertThrows(CustomerIsNotActiveException.class,
                () -> activityCheckAspect.checkEntityActivity(joinPoint, activityCheckRequired));
    }

    @Test
    void testCheckEntityActivityNullTargetThrows() {
        when(activityCheckRequired.checkActivityTarget()).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> activityCheckAspect.checkEntityActivity(joinPoint, activityCheckRequired));
    }

}