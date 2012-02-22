package Agents;

import environment.*;

/**
 * @author ebertb schmidbauerk
 * Chicken Little agent.
 */
public class ChickenLittle extends Actions
{
	

	public ChickenLittle(GameBoard where_I_go)
	{

		memory = new Tile[where_I_go.the_World.length][where_I_go.the_World.length];

		for(int Q = 0; Q < memory.length; Q++)
		{
			for(int W = 0; W < memory.length; W++)
			{
					Tile _LND = new Tile();
					memory[Q][W] = _LND;
					memory[Q][W].becomes_low_data(where_I_go.the_World[Q][W].Here());
				}
		}		
		

		arrow = true;

		agenda = new Directions();

		current_location = new Tile();
		
		where_I_go.the_World[where_I_go.get_Size()-1][0].Here().has_been_explored = true;
		current_location.becomes(where_I_go.the_World[where_I_go.get_Size()-1][0].Here());
		memory[where_I_go.get_Size()-1][0].becomes(current_location);
		

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
		
		for (int o = 0; o < memory.length; o++)
		{
			
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
