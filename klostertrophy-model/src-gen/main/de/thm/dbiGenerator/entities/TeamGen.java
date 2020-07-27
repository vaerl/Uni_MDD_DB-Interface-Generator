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
public class TeamGen {
	
	@Id
	@GeneratedValue
	@Column(name = "Team_id")
	private Long id;
	
	// attributes
	private Status status;
	private String name;
	private Integer points;
	private Gender gender;
	
	// relations
	
	// enums
	public enum Status{
		DONE, 
		PLAYING
	}
	public enum Gender{
		MALE, 
		FEMALE, 
		MIXED
	}
}
