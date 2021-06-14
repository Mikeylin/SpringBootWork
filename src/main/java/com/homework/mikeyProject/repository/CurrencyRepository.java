package com.homework.mikeyProject.repository;

import com.homework.mikeyProject.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Optional<Currency> findById (Long id);

    Optional<Currency> findByCurrency (String currency);

}
