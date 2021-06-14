package com.homework.mikeyProject.controller;

import com.homework.mikeyProject.dto.CoinDeskTransferDTO;
import com.homework.mikeyProject.service.CoinDeskService;
import com.homework.mikeyProject.vo.CurrencyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.homework.mikeyProject.exceptions.BusinessLogicException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/v1/CoinDesk")
@Api(tags = "CoinDesk APIs")
public class CoinDeskController {

    @Autowired
    private CoinDeskService coinDeskService;

    /**
     *  取得CoinDesk資料
     * @return
     * @throws BusinessLogicException
     */
    @GetMapping
    @ApiOperation("取得CoinDesk資料")
    public HttpEntity<String> getCoinDeskData() throws BusinessLogicException {
        return new ResponseEntity<>(coinDeskService.getCoinDeskData() , HttpStatus.OK);
    }

    /**
     *  取得CoinDesk資料(資料轉換)
     * @return
     * @throws BusinessLogicException
     */
    @GetMapping(value = "/transfer")
    @ApiOperation("取得CoinDesk資料(資料轉換)")
    public HttpEntity<CoinDeskTransferDTO> getCoinDeskTransferData() throws BusinessLogicException {
        return new ResponseEntity<>(coinDeskService.getCoinDeskTransferData() , HttpStatus.OK);
    }

	/**
	 * 	新增幣別對應表資料
	 * @param currencyVo
	 * @return
	 * @throws BusinessLogicException
	 */
	@PostMapping(value = "/currency")
	@ApiOperation("新增幣別對應表資料")
	public HttpEntity<Void> addCurrency(@RequestBody CurrencyVo currencyVo) throws BusinessLogicException {
		coinDeskService.addCurrency(currencyVo);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * 	更新幣別對應表資料
	 * @param currencyVo
	 * @param id
	 * @return
	 * @throws BusinessLogicException
	 */
	@PatchMapping(value = "/{id}")
	@ApiOperation("更新幣別對應表資料")
	public HttpEntity<Void> updateCurrency(@RequestBody CurrencyVo currencyVo,
										   @PathVariable(value = "id") long id) throws BusinessLogicException {
		currencyVo.setId(id);
		coinDeskService.updateCurrency(currencyVo);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * 	刪除幣別對應表資料
	 * @param id
	 * @return
	 * @throws BusinessLogicException
	 */
	@DeleteMapping(value = "/{id}")
	@ApiOperation("刪除幣別對應表資料")
	public HttpEntity<Void> deleteCurrency(@PathVariable(value = "id") long id) throws BusinessLogicException {
		coinDeskService.deleteCurrency(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
