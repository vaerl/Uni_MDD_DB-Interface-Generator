package de.thm.dbiGenerator.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class AdminGen {
	
	@Id
	@GeneratedValue
	@Column(name = "admin_id")
	private Long id;
	
	// attributes
	private String username;
	private String password;
	
	// inward relations
	
	// outward relations
	
	// enums
}
