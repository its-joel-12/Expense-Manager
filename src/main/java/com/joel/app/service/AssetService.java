package com.joel.app.service;

import com.joel.app.dto.AssetRequestDto;
import com.joel.app.dto.AssetResponseDto;
import jakarta.validation.Valid;

public interface AssetService {
    AssetResponseDto createAsset(AssetRequestDto assetRequestDto, String userEmail);

    AssetResponseDto updateAsset(Long assetId, AssetRequestDto assetRequestDto, String userEmail);
}
