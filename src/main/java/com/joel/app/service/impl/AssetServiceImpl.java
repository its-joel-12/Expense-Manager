package com.joel.app.service.impl;

import com.joel.app.dto.AssetRequestDto;
import com.joel.app.dto.AssetResponseDto;
import com.joel.app.exception.ResourceNotFoundException;
import com.joel.app.exception.UnauthorizedAccessException;
import com.joel.app.model.Asset;
import com.joel.app.model.User;
import com.joel.app.repository.AssetRepository;
import com.joel.app.repository.UserRepository;
import com.joel.app.service.AssetService;
import com.joel.app.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssetServiceImpl implements AssetService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Override
    public AssetResponseDto createAsset(AssetRequestDto assetRequestDto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with the given email id: " + userEmail));
        Asset asset = modelMapper.map(assetRequestDto, Asset.class);
        asset.setCreatedBy(user.getFirstName());
        asset.setUserId(user.getUserId());
        Asset savedAsset = assetRepository.save(asset);
        return modelMapper.map(savedAsset, AssetResponseDto.class);
    }

    @Override
    public AssetResponseDto updateAsset(Long assetId, AssetRequestDto assetRequestDto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with the given email id: " + userEmail));

        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with the given id: " + assetId));

        if (!asset.getUserId().equals(user.getUserId()) && !AuthUtil.isAdmin()) {
            throw new UnauthorizedAccessException("You are not allowed to update this asset");
        }

        asset.setAssetName(assetRequestDto.getAssetName());
        asset.setDescription(assetRequestDto.getDescription());
        asset.setUpdatedBy(user.getFirstName());

        Asset updatedAsset = assetRepository.save(asset);

        return modelMapper.map(updatedAsset, AssetResponseDto.class);
    }

    @Override
    public List<AssetResponseDto> getAllUserAssets(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with the email id: " + userEmail));
        List<Asset> assets = assetRepository.findByUserId(user.getUserId()).orElse(null);
        if (!assets.isEmpty()) {
            return assets.stream().map(a -> modelMapper.map(a, AssetResponseDto.class)).toList();
        } else {
            throw new ResourceNotFoundException("No Assets found for the user: " + userEmail);
        }
    }
}
