package Agents;

import environment.*;

/**
 * Agent Fear
 * @author patrickj
 * Agent Fear is a coward who cannot bear the thought of danger.
 * If he is confronted with something too scary (such as no clear 
 * route through) he will fall over and give up
 */
public class ChickenLittle extends Actions
{
	
	/* My intentions for my Agents memories is that they are able to move and walk
	 * and talk whenever they desire and to make intelligent decisions on it, however
	 * I seem to be having trouble with a Linked List inside of a linked list with
	 * accessing and setting the various components within it. 
	 * 
	 * Instead, I intend to have an ArrayList of all of the places that the agent has 
	 * been as well as a single tile as his perceptions of the current area. Each tile 
	 * in memory has pointers to the other tiles around it. So in a way, the agent has 
	 * a history based memory of each place he has been, and the order in which he got 
	 * there, but they will also be linked by their pointers inherent in the tiles.
	 */
	

	
	//Agent Fear's Constructor
	public ChickenLittle(GameBoard where_I_go)
	{
		//Basic agent memory setup------------------------------------------------------------
		memory = new Tile[where_I_go.the_World.length][where_I_go.the_World.length];
		//intialize the memory
		for(int Q = 0; Q < memory.length; Q++)
		{
			for(int W = 0; W < memory.length; W++)
			{
					Tile _LND = new Tile();
					memory[Q][W] = _LND;
					memory[Q][W].becomes_low_data(where_I_go.the_World[Q][W].Here());
				}
		}		
		
		//ach laddie- this be your one arrow. use it well
		//Arrow code
		arrow = true;
		//List of places to see and go
		agenda = new Directions();
		//Where I currently am
		current_location = new Tile();
		
		where_I_go.the_World[where_I_go.get_Size()-1][0].Here().has_been_explored = true;
		current_location.becomes(where_I_go.the_World[where_I_go.get_Size()-1][0].Here());
		memory[where_I_go.get_Size()-1][0].becomes(current_location);
		
		//Agents' agenda setup----------------------------------------------------------------
		assess_threat();
		agenda.add(current_location);
		
		moves = 0;
		score = 0;
		
		pass_fail = new pass_fail_test();
		pass_fail.moves = moves;
		pass_fail.pass = false;
		pass_fail.give_up = false;
		pass_fail.die = false;
	}
	
	public pass_fail_test move(GameBoard the_Land)
	{
		boolean mission_complete = false;
		boolean mission_failed = false;
		int end = 0;
		int threat = 0;
		
		while (end == 0) 
		{
			Tile location = new Tile();
			while(threat == 0)
			{
				location = agenda.next();
				if(location == null)
					{mission_failed = true;
					pass_fail.give_up = true;
					end = 1;}
				else
					{threat = location.threat_rating;
					if(threat > 0)
					{
						mission_failed = true;
						pass_fail.give_up = true;
						end = 1;
					}
					break;}
			}
			
			if(mission_failed == false)
			{
				moves += (Math.abs(current_location.x_coordinate-location.x_coordinate)+Math.abs(current_location.y_coordinate-location.y_coordinate));
				score -= (Math.abs(current_location.x_coordinate-location.x_coordinate)+Math.abs(current_location.y_coordinate-location.y_coordinate));
				
				memory[location.x_coordinate][location.y_coordinate].becomes(the_Land.the_World[location.x_coordinate][location.y_coordinate]);
				memory[location.x_coordinate][location.y_coordinate].has_been_explored = true;
				
				current_location = memory[location.x_coordinate][location.y_coordinate];
				
				
				if(the_Land.the_World[current_location.x_coordinate][current_location.y_coordinate].has_Pit)
					{mission_failed = true;
					pass_fail.die = true;
					end++;
					score -= 1000;}
				else if(the_Land.the_World[current_location.x_coordinate][current_location.y_coordinate].has_Wumpus)
					{mission_failed = true;
					pass_fail.die = true;
					end++;
					score -= 1000;}
				else if(the_Land.the_World[current_location.x_coordinate][current_location.y_coordinate].has_Gold)
					{mission_complete = true;
					end++;
					score += 1000;}
			}
			if((mission_complete != true) && (mission_failed != true))
			{
				
				if(memory[location.x_coordinate][location.y_coordinate].South_of_here != null)
					memory[location.x_coordinate][location.y_coordinate].South_of_here.North_of_here.becomes(current_location);
				if(memory[location.x_coordinate][location.y_coordinate].North_of_here != null)	
					memory[location.x_coordinate][location.y_coordinate].North_of_here.South_of_here.becomes(current_location);
				if(memory[location.x_coordinate][location.y_coordinate].West_of_here != null)
					memory[location.x_coordinate][location.y_coordinate].West_of_here.East_of_here.becomes(current_location);
				if(memory[location.x_coordinate][location.y_coordinate].East_of_here != null)
					memory[location.x_coordinate][location.y_coordinate].East_of_here.West_of_here.becomes(current_location);
			
				
				assess_threat();
				//what_are_you_thinking();
				agenda.add(current_location);
			}
	
		}
		pass_fail.moves = moves;
		pass_fail.pass = mission_complete;
		pass_fail.score = score;
		if(pass_fail.give_up == true)
		{
			pass_fail.pass = false;
			pass_fail.die = false;
		}
		return pass_fail;
	}
	
	public void what_are_you_thinking()
	{
		//Rows
		for (int o = 0; o < memory.length; o++)
		{
			//Columns
			for(int i = 0; i < memory.length; i++)
			{
				if((o == current_location.x_coordinate) &&(i == current_location.y_coordinate))
					{System.out.print(memory[o][i].threat_rating + "A_ ");} 
				else if(memory[o][i].has_been_explored == true)
					{System.out.print("+" + memory[o][i].threat_rating + "+" +  " ");}
				
				else 
					{System.out.print("_" + memory[o][i].threat_rating + "_ ");}
			}
			System.out.println(" ");
		}
		System.out.println();
	}
}