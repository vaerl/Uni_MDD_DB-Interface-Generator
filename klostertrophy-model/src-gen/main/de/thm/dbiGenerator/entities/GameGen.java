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
public class GameGen {
	
	@Id
	@GeneratedValue
	@Column(name = "Game_id")
	private Long id;
	
	// attributes
	private String name;
	private Status status;
	private SortOrder sortOrder;
	private PointType pointType;
	
	// relations
		
	
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
