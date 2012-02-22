package environment;


public class Land {
	
	public int x_coordinate;
	public int y_coordinate;
	
	public boolean has_Wumpus;
	public boolean has_Pit;
	public boolean has_Gold;
	public boolean smells;
	public boolean is_breezy;
	public boolean glitters;
	
	public Land North_of_here;
	public Land South_of_here;
	public Land West_of_here;
	public Land East_of_here;
	
	public int threat_rating;
	
	public boolean has_been_explored;
	

	public Land()
	{
		x_coordinate = 0;
		y_coordinate = 0;
		
		has_Wumpus = false;
		has_Pit    = false;
		has_Gold   = false;
		smells     = false;
		is_breezy  = false;
		glitters   = false;
		
		North_of_here = null;
		South_of_here = null;
		West_of_here = null;
		East_of_here = null;
		
		threat_rating = 0;
		
		has_been_explored = false;
	}
	//Set to true
	public void Wumpus_here()
		{this.has_Wumpus = true;}

	public void pit_here()
		{this.has_Pit = true;}
	
	public void Gold_here()
		{this.has_Gold = true;}

	public void smells_here()
		{this.smells = true;}
	
	public void breezy_here()
		{this.is_breezy = true;}
	
	public void glitters_here()
		{this.glitters = true;}
	//
	//Getters for Agent's interaction
	public boolean is_there_a_Wumpus_here()
		{return this.has_Wumpus;}

	public boolean is_there_a_pit_here()
		{return this.has_Pit;}

	public boolean is_there_a_pile_of_gold_here()
		{return this.has_Gold;}
 	
	public boolean arrow_lands_here__hit_something_useful()
	{
		if(has_Wumpus == true)
			{has_Wumpus = false;
			return true;}
		return false;
		
	}

	public Land North()
		{return North_of_here;}
	
	public Land South()
		{return South_of_here;}
	
	public Land West()
		{return West_of_here;}
	
	public Land East()
		{return East_of_here;}

	public Land Here()
		{return this;}
	
	//Land copy constructor
	public void becomes(Land the_World)
	{
		x_coordinate = the_World.x_coordinate;
		y_coordinate = the_World.y_coordinate;
		
		has_Wumpus = the_World.has_Wumpus;
		has_Pit = the_World.has_Pit;
		has_Gold = the_World.has_Gold;
		smells = the_World.smells;
		is_breezy = the_World.is_breezy;
		glitters = the_World.glitters;
		
		North_of_here = the_World.North_of_here;
		South_of_here = the_World.South_of_here;
		West_of_here = the_World.West_of_here;
		East_of_here = the_World.East_of_here;
		
		threat_rating = the_World.threat_rating;
		
		has_been_explored = the_World.has_been_explored;
	}
	
	public void becomes_low_data(Land the_World)
	{
		
		x_coordinate = the_World.x_coordinate;
		y_coordinate = the_World.y_coordinate;
		
		has_Wumpus = false;
		has_Pit = false;
		has_Gold = false;
		smells = false;
		is_breezy = false;
		glitters = false;
		
		North_of_here = the_World.North_of_here;
		South_of_here = the_World.South_of_here;
		West_of_here = the_World.West_of_here;
		East_of_here = the_World.East_of_here;
		
		threat_rating = the_World.threat_rating;
		
		has_been_explored = the_World.has_been_explored;
	}
	
	public boolean coords (Land RHS)
	{
		if(((x_coordinate == RHS.x_coordinate) &&y_coordinate == RHS.y_coordinate) == false)
			{return true;}
		return false;
	}
	
	//public void
	
}



