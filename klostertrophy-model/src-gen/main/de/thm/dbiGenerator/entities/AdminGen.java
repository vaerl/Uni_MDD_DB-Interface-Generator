package de.thm.dbiGenerator.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Setter
@Getter
@NoArgsConstructor
public class AdminGen {
	
	@Id
	@GeneratedValue
	@Column(name = "Admin_id")
	private Long id;
	
	// attributes
	private String username;
	private String password;
	
	// relations
	
	// enums
}
