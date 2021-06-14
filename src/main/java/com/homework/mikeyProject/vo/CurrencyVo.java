package com.homework.mikeyProject.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class CurrencyVo {

	@ApiModelProperty(value = "ID", hidden = true)
	private long id;

	@ApiModelProperty(value = "幣別", position = 1)
	private String currency;

	@ApiModelProperty(value = "幣別名稱", position = 2)
	private String currencyName;

	@ApiModelProperty(value = "匯率", position = 3)
	private BigDecimal rate;

}
