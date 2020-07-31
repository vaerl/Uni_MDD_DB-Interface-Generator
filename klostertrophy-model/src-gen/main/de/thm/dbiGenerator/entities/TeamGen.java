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
public class TeamGen {
	
	@Id
	@GeneratedValue
	@Column(name = "team_id")
	private Long id;
	
	// attributes
	@Enumerated(EnumType.STRING)
	private Status status;
	private String name;
	private Integer points;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	// inward relations
	
	// outward relations
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(
	name = "TeamGame",
	joinColumns = {@JoinColumn(name = "team_id")}, 
					inverseJoinColumns = {@JoinColumn(name = "game_id")})
	private Set<Game> games = new HashSet<Game>();
	
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
