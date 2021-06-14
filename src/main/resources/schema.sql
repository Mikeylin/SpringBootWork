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
