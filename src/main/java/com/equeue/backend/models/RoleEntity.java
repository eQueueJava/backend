package com.equeue.backend.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "roles")
public class RoleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private Role name;
}

