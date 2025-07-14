package com.joel.app.controller;

import com.joel.app.dto.AssetRequestDto;
import com.joel.app.dto.AssetResponseDto;
import com.joel.app.service.AssetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("assets")
public class AssetController {
    @Autowired
    private AssetService assetService;

    @PostMapping
    public ResponseEntity<AssetResponseDto> createAsset(@Valid @RequestBody AssetRequestDto assetRequestDto,
                                                       @AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(assetService.createAsset(assetRequestDto, userDetails.getUsername()), HttpStatus.OK);
    }

    @PutMapping("{assetId}")
    public ResponseEntity<AssetResponseDto> updateAsset(@PathVariable Long assetId, @Valid @RequestBody AssetRequestDto assetRequestDto,
                                                        @AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(assetService.updateAsset(assetId, assetRequestDto, userDetails.getUsername()), HttpStatus.OK);
    }
}
