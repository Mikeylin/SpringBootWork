package com.homework.mikeyProject.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CoinDeskTransferDetailDTO {

    @ApiModelProperty(value = "幣別", position = 1)
    private String currency;

    @ApiModelProperty(value = "幣別名稱", position = 2)
    private String currencyName;

    @ApiModelProperty(value = "匯率", position = 3)
    private BigDecimal rate;
}
