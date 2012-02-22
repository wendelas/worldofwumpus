package Agents;

import environment.Tile;
/**
 * 
 * @author ebertb schmidbauerk
 * Keeps track of what Actions can be taken
 */
public class Actions 
{	
	Tile[][] memory;
	Tile current_location;
	
	pass_fail_test pass_fail;
	
	boolean arrow;
	
	Directions agenda;
	
	int moves;
	int score;
	
	public void assess_threat()
	{

		 
		int threat = 0;
		

		if(current_location.x_coordinate-1 >= 0)
		{
			if((current_location.North() != null) && (memory[current_location.x_coordinate-1][current_location.y_coordinate] != null))
			{
				if(current_location.North().has_been_explored == false)
				{

					if((current_location.North().has_Wumpus == false) || (current_location.North().threat_rating < 5))
					{
						//System.out.println("North:Wumpus ");
						if(current_location.North().East_of_here != null)
							{if(current_location.North().East_of_here.smells && current_location.North().East_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.North().West_of_here != null)
							{if(current_location.North().West_of_here.smells && current_location.North().West_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.North().North_of_here != null)
							{if(current_location.North().North_of_here.smells && current_location.North().North_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.North().South_of_here != null)
							{if(current_location.North().South_of_here.smells && current_location.North().South_of_here.has_been_explored == true)
								{threat++;}}			
						if(threat != 0)
						{
							if(threat >= 2)
							{
								memory[current_location.x_coordinate-1][current_location.y_coordinate].Wumpus_here();
								threat = 5;	
							}
							
							
							Tile N = new Tile();
							
							N.becomes(memory[current_location.x_coordinate][current_location.y_coordinate].North_of_here);
							
							N.threat_rating += threat;
							
							if(current_location.x_coordinate-2 >= 0)
								{
								
								N.North_of_here = memory[current_location.x_coordinate-2][current_location.y_coordinate];
								memory[current_location.x_coordinate-2][current_location.y_coordinate].South_of_here = N;
								}
							
							N.South_of_here = memory[current_location.x_coordinate][current_location.y_coordinate];
							memory[current_location.x_coordinate][current_location.y_coordinate].North_of_here = N;
							
							if((current_location.x_coordinate-1 >= 0) && (current_location.y_coordinate-1  >= 0))
								{
								
								N.West_of_here = memory[current_location.x_coordinate-1][current_location.y_coordinate-1];
								memory[current_location.x_coordinate-1][current_location.y_coordinate-1].East_of_here = N;
								}
							
							if((current_location.x_coordinate-1 >= 0) && (current_location.y_coordinate+1  < memory.length))
								{
								
								N.East_of_here = memory[current_location.x_coordinate-1][current_location.y_coordinate+1];
								memory[current_location.x_coordinate-1][current_location.y_coordinate+1].West_of_here = N;
								}
							memory[current_location.x_coordinate-1][current_location.y_coordinate] = N;
						}
										
						threat = 0;
					}
					
					if((current_location.North().has_Pit == false) || (current_location.North().threat_rating < 5)) 
					{
						
						if(current_location.North().East_of_here != null)
							{if(current_location.North().East_of_here.is_breezy && current_location.North().East_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.North().West_of_here != null)
							{if(current_location.North().West_of_here.is_breezy && current_location.North().West_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.North().North_of_here != null)
							{if(current_location.North().North_of_here.is_breezy && current_location.North().North_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.North().South_of_here != null)
							{if(current_location.North().South_of_here.is_breezy && current_location.North().South_of_here.has_been_explored == true)
								{threat++;}}
										
						if(threat != 0)
						{
							if(threat >= 2)
							{
								memory[current_location.x_coordinate-1][current_location.y_coordinate].pit_here();
								threat = 5;	
							}
							
							
							Tile N = new Tile();
							
							N.becomes(memory[current_location.x_coordinate][current_location.y_coordinate].North_of_here);
							
							N.threat_rating += threat;
							
	
							
							
							if(current_location.x_coordinate-2 >= 0)
							{
								
								N.North_of_here = memory[current_location.x_coordinate-2][current_location.y_coordinate];
								memory[current_location.x_coordinate-2][current_location.y_coordinate].South_of_here = N;
							}
							
							N.South_of_here = memory[current_location.x_coordinate][current_location.y_coordinate];
							memory[current_location.x_coordinate][current_location.y_coordinate].North_of_here = N;
							
							if((current_location.x_coordinate-1 >= 0) && (current_location.y_coordinate-1  >= 0))
							{
								
								N.West_of_here = memory[current_location.x_coordinate-1][current_location.y_coordinate-1];
								memory[current_location.x_coordinate-1][current_location.y_coordinate-1].East_of_here = N;
							}
							
							if((current_location.x_coordinate-1 >= 0) && (current_location.y_coordinate+1  < memory.length))
							{
								
								N.East_of_here = memory[current_location.x_coordinate-1][current_location.y_coordinate+1];
								memory[current_location.x_coordinate-1][current_location.y_coordinate+1].West_of_here = N;
							}
							memory[current_location.x_coordinate-1][current_location.y_coordinate] = N;
						}
					
						threat = 0;
					}
				}
			}
		}
		
		if(current_location.x_coordinate+1 < memory.length)
		{
			if((current_location.South() != null) && (memory[current_location.x_coordinate+1][current_location.y_coordinate] != null))
			{
				if(current_location.South().has_been_explored == false)
				{
					
					if((current_location.South().has_Wumpus == false) || (current_location.South().threat_rating < 5))
					{
						
						if(current_location.South().East_of_here != null)
							{if(current_location.South().East_of_here.smells && current_location.South().East_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.South().West_of_here != null)
							{if(current_location.South().West_of_here.smells && current_location.South().West_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.South().North_of_here != null)
							{if(current_location.South().North_of_here.smells && current_location.South().North_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.South().South_of_here != null)
							{if(current_location.South().South_of_here.smells && current_location.South().South_of_here.has_been_explored == true)
								{threat++;}}
									
						if(threat != 0)
						{
							if(threat >= 2)
							{
								memory[current_location.x_coordinate+1][current_location.y_coordinate].Wumpus_here();
								threat = 5;	
							}
							
							
							Tile N = new Tile();
							
							N.becomes(memory[current_location.x_coordinate][current_location.y_coordinate].South_of_here);
							
							N.threat_rating += threat;
							
							N.North_of_here = memory[current_location.x_coordinate][current_location.y_coordinate];
							memory[current_location.x_coordinate][current_location.y_coordinate].North_of_here = N;
							
							if(current_location.x_coordinate+2 < memory.length)
							{
								
									N.South_of_here = memory[current_location.x_coordinate+2][current_location.y_coordinate];
									memory[current_location.x_coordinate+2][current_location.y_coordinate].South_of_here = N;
							}
							
							if((current_location.x_coordinate+1 < memory.length) && (current_location.y_coordinate+1  < memory.length))
							{
								
								N.West_of_here = memory[current_location.x_coordinate+1][current_location.y_coordinate+1];
								memory[current_location.x_coordinate+1][current_location.y_coordinate+1].East_of_here = N;
							}
							
							if((current_location.x_coordinate+1 < memory.length) && (current_location.y_coordinate-1  >= 0))
							{
								
								N.East_of_here = memory[current_location.x_coordinate+1][current_location.y_coordinate-1];
								memory[current_location.x_coordinate+1][current_location.y_coordinate-1].West_of_here = N;
							}
							memory[current_location.x_coordinate+1][current_location.y_coordinate] = N;
						}
										
						threat = 0;
					}
					
					if((current_location.South().has_Pit == false) || (current_location.South().threat_rating < 5)) 
					{
						
						if(current_location.South().East_of_here != null)
							{if(current_location.South().East_of_here.is_breezy && current_location.South().East_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.South().West_of_here != null)
							{if(current_location.South().West_of_here.is_breezy && current_location.South().West_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.South().North_of_here != null)
							{if(current_location.South().North_of_here.is_breezy && current_location.South().North_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.South().South_of_here != null)
							{if(current_location.South().South_of_here.is_breezy && current_location.South().South_of_here.has_been_explored == true)
								{threat++;}}
										
						if(threat != 0)
						{
							if(threat >= 2)
							{
								memory[current_location.x_coordinate+1][current_location.y_coordinate].pit_here();
								threat = 5;	
							}
							
							
							Tile N = new Tile();
							
							N.becomes(memory[current_location.x_coordinate][current_location.y_coordinate].South_of_here);
							
							N.threat_rating += threat;
							
							N.North_of_here = memory[current_location.x_coordinate][current_location.y_coordinate];
							memory[current_location.x_coordinate][current_location.y_coordinate].North_of_here = N;
							
							if(current_location.x_coordinate+2 < memory.length)
							{
								
									N.South_of_here = memory[current_location.x_coordinate+2][current_location.y_coordinate];
									memory[current_location.x_coordinate+2][current_location.y_coordinate].South_of_here = N;
							}
							
							if((current_location.x_coordinate+1 < memory.length) && (current_location.y_coordinate+1  < memory.length))
							{
								
								N.West_of_here = memory[current_location.x_coordinate+1][current_location.y_coordinate+1];
								memory[current_location.x_coordinate+1][current_location.y_coordinate+1].East_of_here = N;
							}
							
							if((current_location.x_coordinate+1 < memory.length) && (current_location.y_coordinate-1  >= 0))
							{
								
								N.East_of_here = memory[current_location.x_coordinate+1][current_location.y_coordinate-1];
								memory[current_location.x_coordinate+1][current_location.y_coordinate-1].West_of_here = N;
							}
							memory[current_location.x_coordinate+1][current_location.y_coordinate] = N;
						}
					
						threat = 0;
					}
				}
			}
		}
			
		if(current_location.y_coordinate+1 < memory.length)
		{
			if((current_location.East() != null) && (memory[current_location.x_coordinate][current_location.y_coordinate+1] != null))
			{
				if(current_location.East().has_been_explored == false)
				{
					
					if((current_location.East().has_Wumpus == false) || (current_location.East().threat_rating < 5))
					{
						
						if(current_location.East().East_of_here != null)
							{if(current_location.East().East_of_here.smells && current_location.East().East_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.East().West_of_here != null)
							{if(current_location.East().West_of_here.smells && current_location.East().West_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.East().North_of_here != null)
							{if(current_location.East().North_of_here.smells && current_location.East().North_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.East().South_of_here != null)
							{if(current_location.East().South_of_here.smells && current_location.East().South_of_here.has_been_explored == true)
								{threat++;}}
									
						if(threat != 0)
						{
							if(threat >= 2)
							{
								memory[current_location.x_coordinate][current_location.y_coordinate+1].Wumpus_here();
								threat = 5;	
							}
							
							
							Tile N = new Tile();
							
							N.becomes(memory[current_location.x_coordinate][current_location.y_coordinate].East_of_here);
							
							N.threat_rating += threat;
							
	
							
							if((current_location.x_coordinate-1 >= 0) && (current_location.y_coordinate+1  < memory.length))
							{
								
								N.North_of_here = memory[current_location.x_coordinate-1][current_location.y_coordinate+1];
								memory[current_location.x_coordinate-1][current_location.y_coordinate+1].North_of_here = N;
							}
							
							if((current_location.x_coordinate+1 < memory.length) && (current_location.y_coordinate+1 < memory.length))
							{
								
								N.South_of_here = memory[current_location.x_coordinate+1][current_location.y_coordinate+1];
								memory[current_location.x_coordinate+1][current_location.y_coordinate+1].South_of_here = N;
							}
							
							if(current_location.y_coordinate+2 < memory.length)
							{
								
								N.West_of_here = memory[current_location.x_coordinate][current_location.y_coordinate+2];
								memory[current_location.x_coordinate][current_location.y_coordinate+2].East_of_here = N;
							}
							
							N.East_of_here = memory[current_location.x_coordinate][current_location.y_coordinate];
							memory[current_location.x_coordinate][current_location.y_coordinate].West_of_here = N;
							
							memory[current_location.x_coordinate][current_location.y_coordinate+1] = N;
						}
										
						threat = 0;
					}
					
					if((current_location.East().has_Pit == false) || (current_location.East().threat_rating < 5)) 
					{
						
						if(current_location.East().East_of_here != null)
							{if(current_location.East().East_of_here.is_breezy && current_location.East().East_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.East().West_of_here != null)
							{if(current_location.East().West_of_here.is_breezy && current_location.East().West_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.East().North_of_here != null)
							{if(current_location.East().North_of_here.is_breezy && current_location.East().North_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.East().South_of_here != null)
							{if(current_location.East().South_of_here.is_breezy && current_location.East().South_of_here.has_been_explored == true)
								{threat++;}}
										
						if(threat != 0)
						{
							if(threat >= 2)
							{
								memory[current_location.x_coordinate][current_location.y_coordinate+1].pit_here();
								threat = 5;	
							}
							
							
							Tile N = new Tile();
							
							N.becomes(memory[current_location.x_coordinate][current_location.y_coordinate].East_of_here);
							
							N.threat_rating += threat;
							
	
							
							if((current_location.x_coordinate-1 >= 0) && (current_location.y_coordinate+1  < memory.length))
							{
								
								N.North_of_here = memory[current_location.x_coordinate-1][current_location.y_coordinate+1];
								memory[current_location.x_coordinate-1][current_location.y_coordinate+1].North_of_here = N;
							}
							
							if((current_location.x_coordinate+1 < memory.length) && (current_location.y_coordinate+1 < memory.length))
							{
								
								N.South_of_here = memory[current_location.x_coordinate+1][current_location.y_coordinate+1];
								memory[current_location.x_coordinate+1][current_location.y_coordinate+1].South_of_here = N;
							}
							
							if(current_location.y_coordinate+2 < memory.length)
							{
								
								N.West_of_here = memory[current_location.x_coordinate][current_location.y_coordinate+2];
								memory[current_location.x_coordinate][current_location.y_coordinate+2].East_of_here = N;
							}
							
							N.East_of_here = memory[current_location.x_coordinate][current_location.y_coordinate];
							memory[current_location.x_coordinate][current_location.y_coordinate].West_of_here = N;
							
							memory[current_location.x_coordinate][current_location.y_coordinate+1] = N;
						}
					
						threat = 0;
					}
				}
		}
				
		if(current_location.y_coordinate-1 >= 0)
		{
			if((current_location.West() != null) && (memory[current_location.x_coordinate][current_location.y_coordinate-1] != null))
			{
				if(current_location.West().has_been_explored == false)
				{
					
					if((current_location.West().has_Wumpus == false) || (current_location.West().threat_rating < 5))
					{
						
						if(current_location.West().East_of_here != null)
							{if(current_location.West().East_of_here.smells && current_location.West().East_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.West().West_of_here != null)
							{if(current_location.West().West_of_here.smells && current_location.West().West_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.West().North_of_here != null)
							{if(current_location.West().North_of_here.smells && current_location.West().North_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.West().South_of_here != null)
							{if(current_location.West().South_of_here.smells && current_location.West().South_of_here.has_been_explored == true)
								{threat++;}}
									
						if(threat != 0)
						{
							if(threat >= 2)
							{
								memory[current_location.x_coordinate][current_location.y_coordinate-1].Wumpus_here();
								threat = 5;	
							}
							
							
							Tile N = new Tile();
							
							N.becomes(memory[current_location.x_coordinate][current_location.y_coordinate].West_of_here);
							
							N.threat_rating += threat;
							
							if((current_location.x_coordinate-1 >= 0) && (current_location.y_coordinate-1  >= 0))
							{
								
								N.North_of_here = memory[current_location.x_coordinate-1][current_location.y_coordinate-1];
								memory[current_location.x_coordinate-1][current_location.y_coordinate-1].North_of_here = N;
							}
							
							if((current_location.x_coordinate+1  < memory.length) && (current_location.y_coordinate-1  >= 0))
							{
								
								N.South_of_here = memory[current_location.x_coordinate+1][current_location.y_coordinate-1];
								memory[current_location.x_coordinate+1][current_location.y_coordinate-1].South_of_here = N;
							}
							
							N.West_of_here = memory[current_location.x_coordinate][current_location.y_coordinate];
							memory[current_location.x_coordinate][current_location.y_coordinate].East_of_here = N;
							
							if(current_location.y_coordinate-2  >= 0)
							{
								
								N.East_of_here = memory[current_location.x_coordinate][current_location.y_coordinate-2];
								memory[current_location.x_coordinate][current_location.y_coordinate-2].West_of_here = N;
							}
							memory[current_location.x_coordinate][current_location.y_coordinate-1] = N;
						}
										
						threat = 0;
					}
					
					if((current_location.West().has_Pit == false) || (current_location.West().threat_rating < 5)) 
					{
						
						if(current_location.West().East_of_here != null)
							{if(current_location.West().East_of_here.is_breezy && current_location.West().East_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.West().West_of_here != null)
							{if(current_location.West().West_of_here.is_breezy && current_location.West().West_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.West().North_of_here != null)
							{if(current_location.West().North_of_here.is_breezy && current_location.West().North_of_here.has_been_explored == true)
								{threat++;}}
						if(current_location.West().South_of_here != null)
							{if(current_location.West().South_of_here.is_breezy && current_location.West().South_of_here.has_been_explored == true)
								{threat++;}}
										
						if(threat != 0)
						{
							if(threat >= 2)
							{
								memory[current_location.x_coordinate][current_location.y_coordinate-1].pit_here();
								threat = 5;	
							}
							
							
							Tile N = new Tile();
							
							N.becomes(memory[current_location.x_coordinate][current_location.y_coordinate].West_of_here);
							
							N.threat_rating += threat;
							
	
							
							if((current_location.x_coordinate-1 >= 0) && (current_location.y_coordinate-1  >= 0))
							{
								
								N.North_of_here = memory[current_location.x_coordinate-1][current_location.y_coordinate-1];
								memory[current_location.x_coordinate-1][current_location.y_coordinate-1].North_of_here = N;
							}
							
							if((current_location.x_coordinate+1  < memory.length) && (current_location.y_coordinate-1  >= 0))
							{
								
								N.South_of_here = memory[current_location.x_coordinate+1][current_location.y_coordinate-1];
								memory[current_location.x_coordinate+1][current_location.y_coordinate-1].South_of_here = N;
							}
							
							N.West_of_here = memory[current_location.x_coordinate][current_location.y_coordinate];
							memory[current_location.x_coordinate][current_location.y_coordinate].East_of_here = N;
							
							if(current_location.y_coordinate-2  >= 0)
							{
								
								N.East_of_here = memory[current_location.x_coordinate][current_location.y_coordinate-2];
								memory[current_location.x_coordinate][current_location.y_coordinate-2].West_of_here = N;
							}
							memory[current_location.x_coordinate][current_location.y_coordinate-1] = N;
						}
					}
				
				threat = 0;
			}
		}
	}
	}
	}
}