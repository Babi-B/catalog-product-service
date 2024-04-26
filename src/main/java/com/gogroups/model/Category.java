package com.gogroups.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Service
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long categoryId;

	@NotNull(message = "category name cannot be null")
	@Size(min = 3)
	@Column(length = 50)
	private String categoryName;

	@Column(columnDefinition = "MEDIUMBLOB")
	private byte[] categoryImage;

	@NotNull(message = "Description cannot be null")
	@Size(min = 10)
	@Column(length = 50)
	private String categoryDescription;

	@OneToMany(orphanRemoval = true, mappedBy = "category", fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonManagedReference
	private Set<Product> products;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "userId", referencedColumnName = "userId", insertable = true, updatable = false, nullable = false)
	@JsonBackReference
	private User user;

	public Category(long categoryId, @NotNull String categoryName, byte[] categoryImage,
			@NotNull String categoryDescription, User user) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.categoryImage = categoryImage;
		this.categoryDescription = categoryDescription;
		this.user = user;
	}

}
