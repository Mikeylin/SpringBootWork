package com.homework.mikeyProject.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name = "Currency")
public class Currency {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, precision = 20)
	private Long id;

	@Column(name = "currency", nullable = false, length = 20)
	@NotNull
	@Size(max = 20)
	private String currency;

	@Column(name = "currency_name", nullable = false, length = 50)
	@NotNull
	@Size(max = 50)
	private String currencyName;

	@Column(name = "rate", nullable = false)
	@NotNull
	private BigDecimal rate;

	@Column(name = "create_time", nullable = false)
	@NotNull
	private LocalDateTime createTime;

	@Column(name = "update_time")
	private LocalDateTime updateTime;

}
