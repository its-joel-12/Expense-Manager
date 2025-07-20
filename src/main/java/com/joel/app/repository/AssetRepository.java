package com.joel.app.repository;

import com.joel.app.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    Optional<List<Asset>> findByUserId(Long userId);
}