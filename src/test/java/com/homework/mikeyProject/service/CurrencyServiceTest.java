package com.homework.mikeyProject.service;

import com.homework.mikeyProject.entity.Currency;
import com.homework.mikeyProject.exceptions.BusinessLogicException;
import com.homework.mikeyProject.repository.CurrencyRepository;
import com.homework.mikeyProject.service.impl.CoinDeskServiceImpl;
import com.homework.mikeyProject.vo.CurrencyVo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class CurrencyServiceTest {

    final String coinDeskUrl = "http://api.coindesk.com/v1/bpi/currentprice.json";

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CoinDeskServiceImpl coinDeskServiceImpl = new CoinDeskServiceImpl(currencyRepository);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void getCoinDeskDataSuccess() throws BusinessLogicException {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("{{\"time\":{\"updated\":\"\",\"updatedISO\":\"\",\"updateduk\":\"\"}}}", HttpStatus.OK);
        Mockito.when(restTemplate.exchange(coinDeskUrl, HttpMethod.GET, null, String.class)).thenReturn(responseEntity);
        coinDeskServiceImpl.getCoinDeskData();
    }


    @Test
    public void getCoinDeskTransferDataSuccess() throws BusinessLogicException {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("{\"time\":{\"updated\":\"Jun 14, 2021 15:54:00 UTC\",\"updatedISO\":\"2021-06-14T15:54:00+00:00\",\"updateduk\":\"Jun 14, 2021 at 16:54 BST\"},\"disclaimer\":\"This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org\",\"chartName\":\"Bitcoin\",\"bpi\":{\"USD\":{\"code\":\"USD\",\"symbol\":\"&#36;\",\"rate\":\"40,663.0335\",\"description\":\"United States Dollar\",\"rate_float\":40663.0335},\"GBP\":{\"code\":\"GBP\",\"symbol\":\"&pound;\",\"rate\":\"28,805.2863\",\"description\":\"British Pound Sterling\",\"rate_float\":28805.2863},\"EUR\":{\"code\":\"EUR\",\"symbol\":\"&euro;\",\"rate\":\"33,540.1713\",\"description\":\"Euro\",\"rate_float\":33540.1713}}}", HttpStatus.OK);
        Mockito.when(restTemplate.exchange(coinDeskUrl, HttpMethod.GET, null, String.class)).thenReturn(responseEntity);
        coinDeskServiceImpl.getCoinDeskTransferData();
    }

    @Test
    public void addCurrencyTestSuccess() throws BusinessLogicException {
        CurrencyVo currencyVo =
                CurrencyVo.builder().currency("TC").currencyName("TestCurrency").rate(new BigDecimal(3.14159)).build();
        Currency currency =
                Currency.builder().id(1L).currency("TC").currencyName("TestCurrency").rate(new BigDecimal(3.14159)).createTime(LocalDateTime.now()).build();
        Mockito.when(coinDeskServiceImpl.isCurrencyExistsByCurrency(currencyVo.getCurrency())).thenReturn(Optional.empty());
        Mockito.when(currencyRepository.save(currency)).thenReturn(currency);
        verify(currencyRepository, times(1)).findByCurrency("TC");
        verify(currencyRepository, times(1)).save(isA(Currency.class));
    }

    @Test(expected = BusinessLogicException.class)
    public void addCurrencyTestIsExist() throws BusinessLogicException {
        CurrencyVo currencyVo =
                CurrencyVo.builder().currency("TC").currencyName("TestCurrency").rate(new BigDecimal(3.14159)).build();
        Mockito.when(coinDeskServiceImpl.isCurrencyExistsByCurrency(currencyVo.getCurrency())).thenReturn(Optional.of(new Currency()));
    }

    @Test
    public void updateCurrencyTestSuccess() throws BusinessLogicException {
        CurrencyVo currencyVo =
                CurrencyVo.builder().id(1L).currency("TC").currencyName("TestCurrency").rate(new BigDecimal(3.14159)).build();
        Currency currency =
                Currency.builder().id(1L).currency("TC").currencyName("TestCurrency").rate(new BigDecimal(3.14159)).createTime(LocalDateTime.now()).build();
        Mockito.when(coinDeskServiceImpl.isCurrencyExistsById(1L)).thenReturn(Optional.of(currency));
        Mockito.when(currencyRepository.save(currency)).thenReturn(currency);
        coinDeskServiceImpl.updateCurrency(currencyVo);
        verify(currencyRepository, times(1)).exists(currencyVo.getId());
        verify(currencyRepository, times(1)).save(isA(Currency.class));
    }

    @Test(expected = BusinessLogicException.class)
    public void updateCurrencyTestNotExist() throws BusinessLogicException {
        CurrencyVo currencyVo =
                CurrencyVo.builder().id(1L).currency("TC").currencyName("TestCurrency").rate(new BigDecimal(3.14159)).build();
        Mockito.when(coinDeskServiceImpl.isCurrencyExistsById(1L)).thenReturn(Optional.empty());
        coinDeskServiceImpl.updateCurrency(currencyVo);
    }

    @Test
    public void deleteCurrencyTestSuccess() throws BusinessLogicException {
        Long id = 1L;
        Mockito.when(coinDeskServiceImpl.isCurrencyExists(id)).thenReturn(true);
        coinDeskServiceImpl.deleteCurrency(id);
        verify(currencyRepository, times(1)).exists(id);
        verify(currencyRepository, times(1)).delete(id);
    }

    @Test(expected = BusinessLogicException.class)
    public void deleteCurrencyTestNotExist() throws BusinessLogicException {
        Long id = 1L;
        Mockito.when(coinDeskServiceImpl.isCurrencyExists(id)).thenReturn(false);
        coinDeskServiceImpl.deleteCurrency(id);
    }
}
