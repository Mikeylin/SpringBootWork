package com.homework.mikeyProject.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CoinDeskTransferDTO {

	@ApiModelProperty(value = "更新時間(UTC)", position = 1)
	private String updateTimeUTC;

	@ApiModelProperty(value = "幣別相關資訊" , position = 2)
	private List<CoinDeskTransferDetailDTO> coinDeskDetail;

}
