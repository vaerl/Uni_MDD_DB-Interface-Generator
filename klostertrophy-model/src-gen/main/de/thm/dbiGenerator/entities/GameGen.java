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
public class GameGen {
	
	@Id
	@GeneratedValue
	@Column(name = "game_id")
	private Long id;
	
	// attributes
	private String name;
	@Enumerated(EnumType.STRING)
	private Status status;
	@Enumerated(EnumType.STRING)
	private SortOrder sortOrder;
	@Enumerated(EnumType.STRING)
	private PointType pointType;
	
	// inward relations
	@ManyToMany(mappedBy = "games")
	private Set<Team> teams = new HashSet<Team>();
	
	// outward relations
	
	// enums
	public enum Status{
		DONE, 
		PLAYING
	}
	public enum SortOrder{
		ASCENDING, 
		DESCENDING
	}
	public enum PointType{
		TIME, 
		POINTS
	}
}
