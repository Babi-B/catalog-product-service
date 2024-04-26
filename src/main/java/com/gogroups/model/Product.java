package com.gogroups.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Service
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long productId;

	@NotNull(message = "Product cannot be null")
	@Size(min = 5)
	@Column(length = 50)
	private String productName;

	@Column(columnDefinition = "MEDIUMBLOB")
	private byte[] productImage;

	@NotNull(message = "quantity cannot be null")
	@Column(length = 50)
	@Min(1)
	private Integer quantity;

	@NotNull(message = "unit price cannot be null")
	@Column(length = 50)
	private BigDecimal unitPrice;

	@NotNull(message = "Currency cannot be null")
	@Column(length = 10)
	private String currency;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "categoryId", referencedColumnName = "categoryId", insertable = true, updatable = false, nullable = false)
	@JsonBackReference
	private Category category;

	public Product(@NotNull long productId, @NotNull String productName, byte[] productImage, @NotNull Integer quantity,
			@NotNull BigDecimal unitPrice, @NotNull String currency, Category category) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.productImage = productImage;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.currency = currency;
		this.category = category;
	}

}
