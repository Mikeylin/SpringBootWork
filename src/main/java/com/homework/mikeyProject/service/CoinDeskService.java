package com.homework.mikeyProject.service;

import com.homework.mikeyProject.dto.CoinDeskTransferDTO;
import com.homework.mikeyProject.exceptions.BusinessLogicException;
import com.homework.mikeyProject.vo.CurrencyVo;

import java.util.List;

public interface CoinDeskService {

    String getCoinDeskData() throws BusinessLogicException;

    CoinDeskTransferDTO getCoinDeskTransferData() throws BusinessLogicException;

    List<CurrencyVo> getCurrency() throws BusinessLogicException;

    CurrencyVo addCurrency(CurrencyVo currencyVo) throws BusinessLogicException;

    CurrencyVo updateCurrency(CurrencyVo currencyVo) throws BusinessLogicException;

    void deleteCurrency(long id) throws BusinessLogicException;

}
