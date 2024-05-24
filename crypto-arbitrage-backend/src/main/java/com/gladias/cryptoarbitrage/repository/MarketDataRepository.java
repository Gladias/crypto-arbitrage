package com.gladias.cryptoarbitrage.repository;

import com.gladias.cryptoarbitrage.entity.MarketDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketDataRepository extends JpaRepository<MarketDataEntity, Long> {}
