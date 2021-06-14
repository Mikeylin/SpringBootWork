package com.homework.mikeyProject.service;

import com.homework.mikeyProject.dto.CoinDeskTransferDTO;
import com.homework.mikeyProject.exceptions.BusinessLogicException;
import com.homework.mikeyProject.vo.CurrencyVo;

public interface CoinDeskService {

    String getCoinDeskData() throws BusinessLogicException;

    CoinDeskTransferDTO getCoinDeskTransferData() throws BusinessLogicException;

    void addCurrency(CurrencyVo currencyVo) throws BusinessLogicException;

    void updateCurrency(CurrencyVo currencyVo) throws BusinessLogicException;

    void deleteCurrency(long id) throws BusinessLogicException;

}
