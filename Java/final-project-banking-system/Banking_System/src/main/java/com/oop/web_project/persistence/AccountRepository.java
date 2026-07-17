package com.oop.web_project.persistence;

import com.oop.web_project.entities.Account;
import jakarta.persistence.LockModeType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

    @Query("SELECT DISTINCT a FROM Account a " +
            "LEFT JOIN FETCH a.cards " +
            "WHERE a.id = :id")
    Optional<Account> findByIdWithCards(@Param("id") Long id);

    @Query("SELECT DISTINCT a FROM Account a " +
            "LEFT JOIN FETCH a.transactions " +
            "WHERE a.id = :id")
    Optional<Account> findByIdWithTransactions(@Param("id") Long id);

    @Query("SELECT DISTINCT a FROM Account a " +
            "LEFT JOIN FETCH a.customers c " +
            "WHERE a.id = :id")
    Optional<Account> findByIdWithCustomers(@Param("id") Long id);

    List<Account> findAllByCustomersEmail(String email);

    List<Account> findAllByCustomersId(long id);

    Optional<Account> findByCardsId(long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findWithLockById(long id);

    boolean existsByIdAndIsActiveTrue(long accountId);

}
