package Agents;

import environment.Land;
/**
 * To do List:
 * 
 * @author patrickj
 *
 */
public class Actions 
{	
	Land[][] memory;
	Land current_location;
	
	//Current Error: the threat ratings don't seem to take
	
	boolean arrow;
	
	places_to_go agenda;
	
	int moves = 0;
	
	public void assess_threat()
	{
		/*Design Ideology
		 * 1) find a threat
		 * 	1.1) if a threat does not exist, keep on going
		 * 	1.2) if a threat exists, 
		 * 		1.2.1) create a new tile that is the same as the old tile with the new threat value
		 * 		1.2.2) rebuild all connections in memory, and its own connections
		 * 		1.2.3) at the end of the assess threat get the new current tile with the new threat values
		 * x is row
		 * y is cols
		 */
		int threat = 0;
		int x = 0;
		int y = 0;
		//-----------------------------------------------------------
		//----------------------------------------------------------- NORTH
		//-----------------------------------------------------------
		if(current_location.North() != null)
		{
			if(current_location.North().has_been_explored == false)
			{
				//threat_evaluator-------------------------------------------------------------------------------------------------------------------v
				if((current_location.North().has_Wumpus == false) || (current_location.North().threat_rating < 5))
				{
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
				//threat_evaluator-------------------------------------------------------------------------------------------------------------------^
				//threat_response--------------------------------------------------------------------------------------------------------------------v				
					if(threat != 0)
					{
						if(threat >= 2)
						{
							memory[current_location.x_coordinate-1][current_location.y_coordinate].Wumpus_here();
							threat = 5;	
						}
						
						//New Tile
						Land N = new Land();
						//the tile becomes the Northern piece of Land
						N.becomes(memory[current_location.x_coordinate][current_location.y_coordinate].North_of_here);
						//The new tile's threat rating becomes the current threat level
						N.threat_rating += threat;
						//The tile then replaces the old tile

						//Relink the northern tile
						//North - North
						if(current_location.North_of_here.North_of_here != null)
							{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].North_of_here.North_of_here != null)
								{
								N.North_of_here = memory[current_location.x_coordinate-2][current_location.y_coordinate];
								memory[current_location.x_coordinate-2][current_location.y_coordinate].South_of_here = N;
								}
							}
						//North - South
						if(current_location.North_of_here.South_of_here != null)
						{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].North_of_here.South_of_here != null)
								{
								N.South_of_here = memory[current_location.x_coordinate][current_location.y_coordinate];
								memory[current_location.x_coordinate][current_location.y_coordinate].North_of_here = N;
								}
						}
						//North - West
						if(current_location.North_of_here.West_of_here != null)
							{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].North_of_here.West_of_here != null)
								{
								N.West_of_here = memory[current_location.x_coordinate-1][current_location.y_coordinate-1];
								memory[current_location.x_coordinate-1][current_location.y_coordinate-1].East_of_here = N;
								}
							}
						//North - East
						if(current_location.North_of_here.East_of_here != null)
							{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].North_of_here.East_of_here != null)
								{
								N.East_of_here = memory[current_location.x_coordinate-1][current_location.y_coordinate+1];
								memory[current_location.x_coordinate-1][current_location.y_coordinate+1].West_of_here = N;
								}
							}
						memory[current_location.x_coordinate-1][current_location.y_coordinate] = N;
					}
				//threat_response--------------------------------------------------------------------------------------------------------------------^					
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
					//threat_response--------------------------------------------------------------------------------------------------------------------v				
					if(threat != 0)
					{
						if(threat >= 2)
						{
							memory[current_location.x_coordinate-1][current_location.y_coordinate].Wumpus_here();
							threat = 5;	
						}
						
						//New Tile
						Land N = new Land();
						//the tile becomes the Northern piece of Land
						N.becomes(memory[current_location.x_coordinate][current_location.y_coordinate].North_of_here);
						//The new tile's threat rating becomes the current threat level
						N.threat_rating += threat;
						//The tile then replaces the old tile

						//Relink the northern tile
						//North - North
						if(current_location.North_of_here.North_of_here != null)
						{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].North_of_here.North_of_here != null)
								{
								N.North_of_here = memory[current_location.x_coordinate-2][current_location.y_coordinate];
								memory[current_location.x_coordinate-2][current_location.y_coordinate].South_of_here = N;
								}
							}
						//North - South
						if(current_location.North_of_here.South_of_here != null)
						{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].North_of_here.South_of_here != null)
								{
								N.South_of_here = memory[current_location.x_coordinate][current_location.y_coordinate];
								memory[current_location.x_coordinate][current_location.y_coordinate].North_of_here = N;
								}
						}
						//North - West
						if(current_location.North_of_here.West_of_here != null)
							{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].North_of_here.West_of_here != null)
								{
								N.West_of_here = memory[current_location.x_coordinate-1][current_location.y_coordinate-1];
								memory[current_location.x_coordinate-1][current_location.y_coordinate-1].East_of_here = N;
								}
							}
						//North - East
						if(current_location.North_of_here.East_of_here != null)
							{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].North_of_here.East_of_here != null)
								{
								N.East_of_here = memory[current_location.x_coordinate-1][current_location.y_coordinate+1];
								memory[current_location.x_coordinate-1][current_location.y_coordinate+1].West_of_here = N;
								}
							}
						memory[current_location.x_coordinate-1][current_location.y_coordinate] = N;
					}
				//threat_response--------------------------------------------------------------------------------------------------------------------^
					threat = 0;
				}
			}
		}
		//-----------------------------------------------------------
		//----------------------------------------------------------- SOUTH
		//-----------------------------------------------------------		
		if(current_location.South() != null)
		{
			if(current_location.South().has_been_explored == false)
			{
				//threat_evaluator-------------------------------------------------------------------------------------------------------------------v
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
				//threat_evaluator-------------------------------------------------------------------------------------------------------------------^
				//threat_response--------------------------------------------------------------------------------------------------------------------v				
					if(threat != 0)
					{
						if(threat >= 2)
						{
							memory[current_location.x_coordinate+1][current_location.y_coordinate].Wumpus_here();
							threat = 5;	
						}
						
						//New Tile
						Land N = new Land();
						//the tile becomes the Northern piece of Land
						N.becomes(memory[current_location.x_coordinate][current_location.y_coordinate].South_of_here);
						//The new tile's threat rating becomes the current threat level
						N.threat_rating += threat;
						//The tile then replaces the old tile

						//Relink the Southern tile
						//South - North
						if(current_location.South_of_here.North_of_here != null)
							{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].South_of_here.North_of_here != null)
								{
								N.North_of_here = memory[current_location.x_coordinate][current_location.y_coordinate];
								memory[current_location.x_coordinate][current_location.y_coordinate].North_of_here = N;
								}
							}
						//South - South
						if(current_location.South_of_here.South_of_here != null)
						{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].South_of_here.South_of_here != null)
								{
								N.South_of_here = memory[current_location.x_coordinate+2][current_location.y_coordinate];
								memory[current_location.x_coordinate+2][current_location.y_coordinate].South_of_here = N;
								}
						}
						//South - East
						if(current_location.South_of_here.East_of_here != null)
							{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].South_of_here.East_of_here != null)
								{
								N.West_of_here = memory[current_location.x_coordinate+1][current_location.y_coordinate+1];
								memory[current_location.x_coordinate+1][current_location.y_coordinate+1].East_of_here = N;
								}
							}
						//South - West
						if(current_location.South_of_here.West_of_here != null)
							{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].South_of_here.West_of_here != null)
								{
								N.East_of_here = memory[current_location.x_coordinate+1][current_location.y_coordinate-1];
								memory[current_location.x_coordinate+1][current_location.y_coordinate-1].West_of_here = N;
								}
							}
						memory[current_location.x_coordinate+1][current_location.y_coordinate] = N;
					}
				//threat_response--------------------------------------------------------------------------------------------------------------------^					
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
					//threat_response--------------------------------------------------------------------------------------------------------------------v				
					if(threat != 0)
					{
						if(threat >= 2)
						{
							memory[current_location.x_coordinate][current_location.y_coordinate-1].pit_here();
							threat = 5;	
						}
						
						//New Tile
						Land N = new Land();
						//the tile becomes the Northern piece of Land
						N.becomes(memory[current_location.x_coordinate][current_location.y_coordinate].South_of_here);
						//The new tile's threat rating becomes the current threat level
						N.threat_rating += threat;
						//The tile then replaces the old tile

						//Relink the Southern tile
						//South - North
						if(current_location.South_of_here.North_of_here != null)
							{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].South_of_here.North_of_here != null)
								{
								N.North_of_here = memory[current_location.x_coordinate][current_location.y_coordinate];
								memory[current_location.x_coordinate][current_location.y_coordinate].North_of_here = N;
								}
							}
						//South - South
						if(current_location.South_of_here.South_of_here != null)
						{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].South_of_here.South_of_here != null)
								{
								N.South_of_here = memory[current_location.x_coordinate+2][current_location.y_coordinate];
								memory[current_location.x_coordinate+2][current_location.y_coordinate].South_of_here = N;
								}
						}
						//South - East
						if(current_location.South_of_here.East_of_here != null)
							{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].South_of_here.West_of_here != null)
								{
								N.West_of_here = memory[current_location.x_coordinate+1][current_location.y_coordinate+1];
								memory[current_location.x_coordinate+1][current_location.y_coordinate+1].East_of_here = N;
								}
							}
						//South - West
						if(current_location.South_of_here.West_of_here != null)
							{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].South_of_here.East_of_here != null)
								{
								N.East_of_here = memory[current_location.x_coordinate+1][current_location.y_coordinate-1];
								memory[current_location.x_coordinate+1][current_location.y_coordinate-1].West_of_here = N;
								}
							}
						memory[current_location.x_coordinate+1][current_location.y_coordinate] = N;
					}
				//threat_response--------------------------------------------------------------------------------------------------------------------^
					threat = 0;
				}
			}
		}
		//-----------------------------------------------------------
		//----------------------------------------------------------- EAST
		//-----------------------------------------------------------		
		if(current_location.East() != null)
		{
			if(current_location.East().has_been_explored == false)
			{
				//threat_evaluator-------------------------------------------------------------------------------------------------------------------v
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
				//threat_evaluator-------------------------------------------------------------------------------------------------------------------^
				//threat_response--------------------------------------------------------------------------------------------------------------------v				
					if(threat != 0)
					{
						if(threat >= 2)
						{
							memory[current_location.x_coordinate+1][current_location.y_coordinate].Wumpus_here();
							threat = 5;	
						}
						
						//New Tile
						Land N = new Land();
						//the tile becomes the Northern piece of Land
						N.becomes(memory[current_location.x_coordinate][current_location.y_coordinate].East_of_here);
						//The new tile's threat rating becomes the current threat level
						N.threat_rating += threat;
						//The tile then replaces the old tile

						//Relink the Eastern tile
						//East - North
						if(current_location.East_of_here.North_of_here != null)
							{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].East_of_here.North_of_here != null)
								{
								N.North_of_here = memory[current_location.x_coordinate-1][current_location.y_coordinate+1];
								memory[current_location.x_coordinate-1][current_location.y_coordinate+1].North_of_here = N;
								}
							}
						//East - South
						if(current_location.East_of_here.South_of_here != null)
						{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].East_of_here.South_of_here != null)
								{
								N.South_of_here = memory[current_location.x_coordinate+1][current_location.y_coordinate+1];
								memory[current_location.x_coordinate+1][current_location.y_coordinate+1].South_of_here = N;
								}
						}
						//East - East
						if(current_location.East_of_here.East_of_here != null)
							{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].East_of_here.East_of_here != null)
								{
								N.West_of_here = memory[current_location.x_coordinate][current_location.y_coordinate+2];
								memory[current_location.x_coordinate][current_location.y_coordinate+2].East_of_here = N;
								}
							}
						//East - West
						if(current_location.East_of_here.West_of_here != null)
							{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].East_of_here.West_of_here != null)
								{
								N.East_of_here = memory[current_location.x_coordinate][current_location.y_coordinate];
								memory[current_location.x_coordinate][current_location.y_coordinate].West_of_here = N;
								}
							}
						memory[current_location.x_coordinate][current_location.y_coordinate+1] = N;
					}
				//threat_response--------------------------------------------------------------------------------------------------------------------^					
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
					//threat_response--------------------------------------------------------------------------------------------------------------------v				
					if(threat != 0)
					{
						if(threat >= 2)
						{
							memory[current_location.x_coordinate+1][current_location.y_coordinate].Wumpus_here();
							threat = 5;	
						}
						
						//New Tile
						Land N = new Land();
						//the tile becomes the Northern piece of Land
						N.becomes(memory[current_location.x_coordinate][current_location.y_coordinate].East_of_here);
						//The new tile's threat rating becomes the current threat level
						N.threat_rating += threat;
						//The tile then replaces the old tile

						//Relink the Eastern tile
						//East - North
						if(current_location.East_of_here.North_of_here != null)
							{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].East_of_here.North_of_here != null)
								{
								N.North_of_here = memory[current_location.x_coordinate-1][current_location.y_coordinate+1];
								memory[current_location.x_coordinate-1][current_location.y_coordinate+1].North_of_here = N;
								}
							}
						//East - South
						if(current_location.East_of_here.South_of_here != null)
						{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].East_of_here.South_of_here != null)
								{
								N.South_of_here = memory[current_location.x_coordinate+1][current_location.y_coordinate+1];
								memory[current_location.x_coordinate+1][current_location.y_coordinate+1].South_of_here = N;
								}
						}
						//East - East
						if(current_location.East_of_here.East_of_here != null)
							{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].East_of_here.East_of_here != null)
								{
								N.West_of_here = memory[current_location.x_coordinate][current_location.y_coordinate+2];
								memory[current_location.x_coordinate][current_location.y_coordinate+2].East_of_here = N;
								}
							}
						//East - West
						if(current_location.East_of_here.West_of_here != null)
							{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].East_of_here.West_of_here != null)
								{
								N.East_of_here = memory[current_location.x_coordinate][current_location.y_coordinate];
								memory[current_location.x_coordinate][current_location.y_coordinate].West_of_here = N;
								}
							}
						memory[current_location.x_coordinate][current_location.y_coordinate+1] = N;
					}
				//threat_response--------------------------------------------------------------------------------------------------------------------^
					threat = 0;
				}
		}
		//-----------------------------------------------------------
		//----------------------------------------------------------- WEST
		//-----------------------------------------------------------		
		if(current_location.West() != null)
		{
			if(current_location.West().has_been_explored == false)
			{
				//threat_evaluator-------------------------------------------------------------------------------------------------------------------v
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
				//threat_evaluator-------------------------------------------------------------------------------------------------------------------^
				//threat_response--------------------------------------------------------------------------------------------------------------------v				
					if(threat != 0)
					{
						if(threat >= 2)
						{
							memory[current_location.x_coordinate+1][current_location.y_coordinate].Wumpus_here();
							threat = 5;	
						}
						
						//New Tile
						Land N = new Land();
						//the tile becomes the Northern piece of Land
						N.becomes(memory[current_location.x_coordinate][current_location.y_coordinate].West_of_here);
						//The new tile's threat rating becomes the current threat level
						N.threat_rating += threat;
						//The tile then replaces the old tile

						//Relink the northern tile
						//West - North
						if(current_location.West_of_here.North_of_here != null)
							{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].West_of_here.North_of_here != null)
								{
								N.North_of_here = memory[current_location.x_coordinate-1][current_location.y_coordinate-1];
								memory[current_location.x_coordinate-1][current_location.y_coordinate-1].North_of_here = N;
								}
							}
						//West - South
						if(current_location.West_of_here.South_of_here != null)
						{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].West_of_here.South_of_here != null)
								{
								N.South_of_here = memory[current_location.x_coordinate][current_location.y_coordinate-1];
								memory[current_location.x_coordinate+1][current_location.y_coordinate-1].South_of_here = N;
								}
						}
						//West - East
						if(current_location.West_of_here.East_of_here != null)
							{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].West_of_here.East_of_here != null)
								{
								N.West_of_here = memory[current_location.x_coordinate][current_location.y_coordinate];
								memory[current_location.x_coordinate][current_location.y_coordinate].East_of_here = N;
								}
							}
						//West - West
						if(current_location.West_of_here.West_of_here != null)
							{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].West_of_here.West_of_here != null)
								{
								N.East_of_here = memory[current_location.x_coordinate][current_location.y_coordinate-2];
								memory[current_location.x_coordinate][current_location.y_coordinate-2].West_of_here = N;
								}
							}
						memory[current_location.x_coordinate][current_location.y_coordinate-1] = N;
					}
				//threat_response--------------------------------------------------------------------------------------------------------------------^					
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
					//threat_response--------------------------------------------------------------------------------------------------------------------v				
					if(threat != 0)
					{
						if(threat >= 2)
						{
							memory[current_location.x_coordinate+1][current_location.y_coordinate].Wumpus_here();
							threat = 5;	
						}
						
						//New Tile
						Land N = new Land();
						//the tile becomes the Northern piece of Land
						N.becomes(memory[current_location.x_coordinate][current_location.y_coordinate].West_of_here);
						//The new tile's threat rating becomes the current threat level
						N.threat_rating += threat;
						//The tile then replaces the old tile

						//Relink the northern tile
						//West - North
						if(current_location.West_of_here.North_of_here != null)
							{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].West_of_here.North_of_here != null)
								{
								N.North_of_here = memory[current_location.x_coordinate-1][current_location.y_coordinate-1];
								memory[current_location.x_coordinate-1][current_location.y_coordinate-1].North_of_here = N;
								}
							}
						//West - South
						if(current_location.West_of_here.South_of_here != null)
						{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].West_of_here.South_of_here != null)
								{
								N.South_of_here = memory[current_location.x_coordinate][current_location.y_coordinate-1];
								memory[current_location.x_coordinate+1][current_location.y_coordinate-1].South_of_here = N;
								}
						}
						//West - East
						if(current_location.West_of_here.East_of_here != null)
							{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].West_of_here.East_of_here != null)
								{
								N.West_of_here = memory[current_location.x_coordinate][current_location.y_coordinate];
								memory[current_location.x_coordinate][current_location.y_coordinate].East_of_here = N;
								}
							}
						//West - West
						if(current_location.West_of_here.West_of_here != null)
							{
							if(memory[current_location.x_coordinate][current_location.y_coordinate].West_of_here.West_of_here != null)
								{
								N.East_of_here = memory[current_location.x_coordinate][current_location.y_coordinate-2];
								memory[current_location.x_coordinate][current_location.y_coordinate-2].West_of_here = N;
								}
							}
						memory[current_location.x_coordinate][current_location.y_coordinate-1] = N;
					}
					}
				//threat_response--------------------------------------------------------------------------------------------------------------------^
					threat = 0;
		}
			/*if(current_location.West().has_been_explored == false)
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
					if(threat >= 2)
						{memory[current_location.x_coordinate][current_location.y_coordinate].West_of_here.pit_here();
						threat = 5;}
					memory[current_location.x_coordinate][current_location.y_coordinate].West_of_here.threat_rating += threat;
					threat = 0;
				}
				
				if((current_location.West().has_Pit == false) || (current_location.North().threat_rating < 5))
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
					if(threat >= 2)
						{memory[current_location.x_coordinate][current_location.y_coordinate].West_of_here.pit_here();
						threat = 5;}
					memory[current_location.x_coordinate][current_location.y_coordinate].West_of_here.threat_rating += threat;
					threat = 0;
				}*/
			}
		}
	}
}
/* Old Code
if(current_location.South() != null)
{
	if(current_location.South().has_been_explored == false)
	{
		if((current_location.South().has_Wumpus == false) || (current_location.North().threat_rating < 5))
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
			//threat_response--------------------------------------------------------------------------------------------------------------------v				
			if(threat != 0)
			{
				if(threat >= 2)
				{
					memory[current_location.x_coordinate][current_location.y_coordinate+1].Wumpus_here();
					threat = 5;	
				}
				
				//New Tile
				Land N = new Land();
				//the tile becomes the Northern piece of Land
				N.becomes(memory[current_location.x_coordinate][current_location.y_coordinate].East_of_here);
				//The new tile's threat rating becomes the current threat level
				N.threat_rating += threat;
				//The tile then replaces the old tile
				if(memory[current_location.x_coordinate][current_location.y_coordinate] != null)
					{memory[current_location.x_coordinate][current_location.y_coordinate].South_of_here = N;
					N.North_of_here = memory[current_location.x_coordinate][current_location.y_coordinate-1];}
				if(memory[current_location.x_coordinate][current_location.y_coordinate+2] != null)
					{memory[current_location.x_coordinate][current_location.y_coordinate+2].North_of_here = N;
					N.South_of_here = memory[current_location.x_coordinate][current_location.y_coordinate-1];}
				if(memory[current_location.x_coordinate+1][current_location.y_coordinate+1] != null)
					{memory[current_location.x_coordinate+1][current_location.y_coordinate+1].East_of_here = N;
					N.West_of_here = memory[current_location.x_coordinate][current_location.y_coordinate-1];}
				if(memory[current_location.x_coordinate-1][current_location.y_coordinate+1] != null)
					{memory[current_location.x_coordinate-1][current_location.y_coordinate+1].West_of_here = N;
					N.East_of_here = memory[current_location.x_coordinate][current_location.y_coordinate-1];}
			}
		//threat_response--------------------------------------------------------------------------------------------------------------------^	
			threat = 0;
		}
		
		
		if((current_location.South().has_Pit == false) || (current_location.North().threat_rating < 5))
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
			//threat_response--------------------------------------------------------------------------------------------------------------------v				
			if(threat != 0)
			{
				if(threat >= 2)
				{
					memory[current_location.x_coordinate][current_location.y_coordinate+1].Wumpus_here();
					threat = 5;	
				}
				
				//New Tile
				Land N = new Land();
				//the tile becomes the Northern piece of Land
				N.becomes(memory[current_location.x_coordinate][current_location.y_coordinate].East_of_here);
				//The new tile's threat rating becomes the current threat level
				N.threat_rating += threat;
				//The tile then replaces the old tile
				if(memory[current_location.x_coordinate][current_location.y_coordinate] != null)
					{memory[current_location.x_coordinate][current_location.y_coordinate].South_of_here = N;
					N.North_of_here = memory[current_location.x_coordinate][current_location.y_coordinate-1];}
				if(memory[current_location.x_coordinate][current_location.y_coordinate+2] != null)
					{memory[current_location.x_coordinate][current_location.y_coordinate+2].North_of_here = N;
					N.South_of_here = memory[current_location.x_coordinate][current_location.y_coordinate-1];}
				if(memory[current_location.x_coordinate+1][current_location.y_coordinate+1] != null)
					{memory[current_location.x_coordinate+1][current_location.y_coordinate+1].East_of_here = N;
					N.West_of_here = memory[current_location.x_coordinate][current_location.y_coordinate-1];}
				if(memory[current_location.x_coordinate-1][current_location.y_coordinate+1] != null)
					{memory[current_location.x_coordinate-1][current_location.y_coordinate+1].West_of_here = N;
					N.East_of_here = memory[current_location.x_coordinate][current_location.y_coordinate-1];}
			}
		//threat_response--------------------------------------------------------------------------------------------------------------------^	
		threat = 0;
		}
	}
}*/