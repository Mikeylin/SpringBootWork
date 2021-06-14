***
## [使用框架/資料庫]
> 專案類型: Maven

| 框架/資料庫 |
| ------ |
| Spring boot 1.5.2 |
| Spring Data JPA | 
| Spring boot starter 系列 (Web,Test) | 
| Mockito |
| Hibernate | 
| Lombok | 
| H2 | 
***
## [功能]
```
SwaggerUI	: http://localhost:8080/swagger-ui.html#/

H2 DataBase	: http://localhost:8080/console/

	JDBC UR	: jdbc:h2:~/test;DB_CLOSE_ON_EXIT=FALSE
  
	UserName: sa
  
    不用密碼
```
### CoinDesk APIs
```sh
*API URL*
http://localhost:8080/v1/CoinDesk
```
```sh
*Request Header*
Content-Type:application/json
```
> 請參閱 SwaggerUI

  #####  /v1/CoinDesk 取得CoinDesk資料
  ##### /v1/CoinDesk/currency 查詢幣別對應表資料
  ##### /v1/CoinDesk/currency 新增幣別對應表資料
  ##### /v1/CoinDesk/currency/{id} 刪除幣別對應表資料
  ##### /v1/CoinDesk/currency/{id} 更新幣別對應表資料
  ##### /v1/CoinDesk/transfer 取得CoinDesk資料(資料轉換)


***
## [資料庫/資料表結構]
> 請參閱 src\main\resources\schema.sql

```sh
DROP TABLE Currency IF EXISTS;
CREATE TABLE Currency (
    id IDENTITY NOT NULL PRIMARY KEY,
    currency VARCHAR(20) NOT NULL COMMENT '幣別',
    currency_name VARCHAR(50) NOT NULL COMMENT '幣別名稱',
	rate decimal(18,6) NOT NULL COMMENT '匯率',
	create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
	update_time datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
    PRIMARY KEY (id)
);

```


