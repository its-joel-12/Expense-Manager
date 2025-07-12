package com.joel.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AssetRequestDto {
    @NotEmpty
    @NotBlank
    @Size(min = 3)
    private String assetName;
    @NotNull
    private BigDecimal balance;
    private String currency;
    private String description;
}
