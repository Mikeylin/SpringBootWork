package com.homework.mikeyProject.service.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.homework.mikeyProject.dto.CoinDeskTransferDTO;
import com.homework.mikeyProject.dto.CoinDeskTransferDetailDTO;
import com.homework.mikeyProject.entity.Currency;
import com.homework.mikeyProject.exceptions.BusinessLogicException;
import com.homework.mikeyProject.repository.CurrencyRepository;
import com.homework.mikeyProject.service.CoinDeskService;
import com.homework.mikeyProject.vo.CurrencyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CoinDeskServiceImpl implements CoinDeskService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CurrencyRepository currencyRepository;

    final String coinDeskUrl = "http://api.coindesk.com/v1/bpi/currentprice.json";

    public CoinDeskServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public String getCoinDeskData() throws BusinessLogicException{
        try{
            ResponseEntity<String> results = restTemplate.exchange(coinDeskUrl, HttpMethod.GET, null, String.class);
            if(null != results && HttpStatus.OK.equals(results.getStatusCode())){
                return results.getBody();
            }else{
                throw new BusinessLogicException("調用 getCoinDeskData 非預期錯誤", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            throw new BusinessLogicException("調用 getCoinDeskData 失敗 , 錯誤訊息 : " + ex.getMessage() , HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public CoinDeskTransferDTO getCoinDeskTransferData() throws BusinessLogicException{
        try{
            ResponseEntity<String> results = restTemplate.exchange(coinDeskUrl, HttpMethod.GET, null, String.class);
            if(null != results && HttpStatus.OK.equals(results.getStatusCode())){
                return transferCoinDeskData(results.getBody());
            }else{
                throw new BusinessLogicException("調用 getCoinDeskTransferData 非預期錯誤", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            throw new BusinessLogicException("調用 getCoinDeskTransferData 失敗 , 錯誤訊息 : " + ex.getMessage() , HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<CurrencyVo> getCurrency() throws BusinessLogicException {
        List<Currency> list = currencyRepository.findAll();
        if(!list.isEmpty()){
            return currencyRepository.findAll().stream().map(currency ->
                    CurrencyVo.builder().id(currency.getId()).currency(currency.getCurrency()).currencyName(currency.getCurrencyName()).rate(currency.getRate()).build())
                    .collect(Collectors.toList());
        }else{
            throw new BusinessLogicException("幣別資料為空" , HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public CurrencyVo addCurrency(CurrencyVo currencyVo) throws BusinessLogicException {
        if(!isCurrencyExistsByCurrency(currencyVo.getCurrency()).isPresent()) {
            Currency currency = currencyRepository.save(Currency.builder().currency(currencyVo.getCurrency())
                    .currencyName(currencyVo.getCurrencyName()).rate(currencyVo.getRate()).createTime(LocalDateTime.now()).build());
            return CurrencyVo.builder().id(currency.getId()).currency(currency.getCurrency()).currencyName(currency.getCurrencyName()).rate(currency.getRate()).build();
        }else {
            throw new BusinessLogicException("幣別資料已存在", HttpStatus.CONFLICT);
        }
    }

    @Override
    public CurrencyVo updateCurrency(CurrencyVo currencyVo) throws BusinessLogicException {
        Optional<Currency> currencyOptional = isCurrencyExistsById(currencyVo.getId());
        if(currencyOptional.isPresent()) {
            Currency currency = currencyOptional.get();
            currency.setCurrency(null != currencyVo.getCurrency() ? currencyVo.getCurrency() : currency.getCurrency());
            currency.setCurrencyName(null != currencyVo.getCurrencyName() ? currencyVo.getCurrencyName() : currency.getCurrencyName());
            currency.setRate(null != currencyVo.getRate() ? currencyVo.getRate() : currency.getRate());
            currency.setUpdateTime(LocalDateTime.now());
            currencyRepository.save(currency);
            return CurrencyVo.builder().id(currency.getId()).currency(currency.getCurrency()).currencyName(currency.getCurrencyName()).rate(currency.getRate()).build();
        }else {
            //部門資料不存在
            throw new BusinessLogicException("幣別資料不存在", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteCurrency(long id) throws BusinessLogicException {
        if(isCurrencyExists(id)) {
            currencyRepository.delete(id);
        }else {
            throw new BusinessLogicException("幣別資料不存在", HttpStatus.NOT_FOUND);
        }
    }

    private CoinDeskTransferDTO transferCoinDeskData(String jsonString){
        CoinDeskTransferDTO coinDeskTransferDTO = new CoinDeskTransferDTO();
        List<CoinDeskTransferDetailDTO> coinDeskTransferDetailDTOList = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonObject jsonObject  = parser.parse(jsonString).getAsJsonObject();
        // Time
        JsonObject timeObject = jsonObject.get("time").getAsJsonObject();
        String updatedObject = timeObject.get("updatedISO").getAsString();
        LocalDateTime datetimeISO = LocalDateTime.parse(updatedObject, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        coinDeskTransferDTO.setUpdateTimeUTC(datetimeISO.format(formatter));

        // Data
        JsonObject bpiObject = jsonObject.get("bpi").getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entrySet = bpiObject.entrySet();
        for(Map.Entry<String,JsonElement> entry : entrySet){
            CoinDeskTransferDetailDTO coinDeskTransferDetailDTO = new CoinDeskTransferDetailDTO();
            JsonObject objList = bpiObject.get(entry.getKey()).getAsJsonObject();
            coinDeskTransferDetailDTO.setCurrency(objList.get("code").getAsString());
            coinDeskTransferDetailDTO.setCurrencyName(transferChineseName(objList.get("code").getAsString()));
            coinDeskTransferDetailDTO.setRate(objList.get("rate_float").getAsBigDecimal());
            coinDeskTransferDetailDTOList.add(coinDeskTransferDetailDTO);
        }
        coinDeskTransferDTO.setCoinDeskDetail(coinDeskTransferDetailDTOList);
        return coinDeskTransferDTO;
    }

    private String transferChineseName(String currency) {
        switch(currency){
            case "USD":
                return "美元";
            case "GBP":
                return "英磅";
            case "EUR":
                return "歐元";
            default:
                return currency;
        }
    }

    public boolean isCurrencyExists(Long id) {
        return currencyRepository.exists(id);
    }

    public Optional<Currency> isCurrencyExistsById(Long id) {
        return currencyRepository.findById(id);
    }

    public Optional<Currency> isCurrencyExistsByCurrency(String currency) {
        return currencyRepository.findByCurrency(currency);
    }

}
