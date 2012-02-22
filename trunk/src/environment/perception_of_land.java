package environment;

public class perception_of_land extends Land {
	
	public boolean the_agent_started_here;
	

	public perception_of_land()
		{threat_rating = 0;}
	
	public void this_is_where_I_begin()
		{this.the_agent_started_here = true;}
	
	//Threat gauger mods
	public void pit_threat()
		{this.threat_rating +=1;}
	
	public void no_pit_there()
		{this.threat_rating -=1;}	
	
	public void wumpus_threat(boolean I_have_an_arrow)
		{
		if(I_have_an_arrow == true)
			{this.threat_rating +=1;}
		else
			{this.threat_rating +=5;}
		}
	
	public void no_wumpus_there(boolean I_have_an_arrow)
	{
	if(I_have_an_arrow == true)
		{this.threat_rating -=1;}
	else
		{this.threat_rating -=5;}
	}
	
	//Land copy constructor
	public void becomes_l(Land the_World)
	{
		this.threat_rating = 0;
		
		this.smells = the_World.smells;
		this.is_breezy = the_World.is_breezy;
		this.glitters = the_World.glitters;
		
		this.has_been_explored = false;
		
		this.North_of_here = the_World.North_of_here;
		this.South_of_here = the_World.South_of_here;
		this.West_of_here = the_World.West_of_here;
		this.East_of_here = the_World.East_of_here;
	}

	//perception copy constructor
	public void becomes_p(perception_of_land the_World)
	{
		this.threat_rating = 0;
		
		this.smells = the_World.smells;
		this.is_breezy = the_World.is_breezy;
		this.glitters = the_World.glitters;
		
		this.the_agent_started_here = the_World.the_agent_started_here;
		
		this.has_been_explored = false;
		
		this.North_of_here = the_World.North_of_here;
		this.South_of_here = the_World.South_of_here;
		this.West_of_here = the_World.West_of_here;
		this.East_of_here = the_World.East_of_here;
	}
	
	public int identify_threat_level(boolean has_arrow)
	{
		int threat = 0;
		
		if(this.North_of_here != null)
		{
			if(this.North_of_here.has_been_explored == true)
			{
				if(this.North_of_here.is_breezy)
					{this.pit_threat();}
				if(this.North_of_here.smells)
					{this.wumpus_threat(has_arrow);}
			}
		}
		
		if(this.East_of_here != null)
		{
			if(this.East_of_here.has_been_explored == true)
			{
				if(this.East_of_here.is_breezy)
					{this.pit_threat();}
				if(this.East_of_here.smells)
					{this.wumpus_threat(has_arrow);}
			}
		}
		
		if(this.West_of_here != null)
		{
			if(this.West_of_here.has_been_explored == true)
			{
				if(this.West_of_here.is_breezy)
					{this.pit_threat();}
				if(this.West_of_here.smells)
					{this.wumpus_threat(has_arrow);}
			}
		}
		
		if(this.South_of_here != null)
		{
			if(this.South_of_here.has_been_explored == true)
			{
				if(this.South_of_here.is_breezy)
					{this.pit_threat();}
				if(this.South_of_here.smells)
					{this.wumpus_threat(has_arrow);}
			}
		}
		
		this.threat_rating = threat;
		
		return threat;
	}
	
}
