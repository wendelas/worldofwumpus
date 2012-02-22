package environment;

public class GameBoard 
{
	public Tile[][] the_World;
	private int size_of_the_World;
	
	/**
	 * Wumpus World
	 * @param size
	 * 
	 * The wumpus World's size is generated on start and cannot be changed
	 * after creation. The area generated will always be a square
	 */
	public GameBoard(int size, double percent_pits)
	{
		//The world will now have a size
		set_Size(size);
		this.the_World = new Tile[get_Size()][get_Size()];
		
		for (int o = 0; o < this.get_Size(); o++)
		{
			for(int i = 0; i < this.get_Size(); i++)
			{
				the_World[o][i] = new Tile();
				the_World[o][i].x_coordinate = o;
				the_World[o][i].y_coordinate = i;
			}
		}
		
		for (int o = 0; o < this.get_Size(); o++)
		{
			for(int i = 0; i < this.get_Size(); i++)
			{
				//up
				if(o != 0)
					{this.the_World[o][i].North_of_here = this.the_World[o-1][i];}
				//left
				if(i != 0)
					{this.the_World[o][i].West_of_here = this.the_World[o][i-1];}
				//down
				if(o != (size-1))
					{this.the_World[o][i].South_of_here = this.the_World[o+1][i];}
				//right
				if(i != (size-1))
					{this.the_World[o][i].East_of_here = this.the_World[o][i+1];}				
			}
		}
		
		//The number of pits will be determined here
		double calc1 = 0;
		double calc2 = 0;
		calc1 = size*size;
		calc2 = (double) (percent_pits/100);
		int pits_to_be_placed = 0;
		pits_to_be_placed = (int) (calc1 * calc2);
		if (pits_to_be_placed == 0)
			pits_to_be_placed = 1;
		
		//Initialize pit coordinates
		int pit_x_location = 0;
		int pit_y_location = 0;
		
		int Wumpus_Count = 1;
		int Gold_Count = 1;
		
		for(int pits_placed = 0; pits_placed < pits_to_be_placed;)
		{
			pit_x_location = (int) Math.round(Math.random()*(get_Size()-1));
			pit_y_location = (int) Math.round(Math.random()*(get_Size()-1));
			//---------------------------------------------------------------------
			//Wumpus Placing Code
			if((the_World[pit_x_location][pit_y_location].is_there_a_pit_here() == false)
					&& (the_World[pit_x_location][pit_y_location] != the_World[the_World.length-1][0])
					&& (the_World[pit_x_location][pit_y_location].is_there_a_pile_of_gold_here() == false))
			{
				if(Wumpus_Count != 0)
				{
					the_World[pit_x_location][pit_y_location].has_Wumpus = true;
					
					if(pit_x_location != the_World.length-1)
						the_World[pit_x_location+1][pit_y_location].smells = true;
					if(pit_x_location != 0)
						the_World[pit_x_location-1][pit_y_location].smells = true;
					if(pit_y_location != the_World.length-1)
						the_World[pit_x_location][pit_y_location+1].smells = true;
					if(pit_y_location != 0)
						the_World[pit_x_location][pit_y_location-1].smells = true;


					Wumpus_Count--;
				}
			}
			//---------------------------------------------------------------------
			if((the_World[pit_x_location][pit_y_location].is_there_a_pit_here() == false)
					&& (the_World[pit_x_location][pit_y_location] != the_World[the_World.length-1][0])
					&& (the_World[pit_x_location][pit_y_location].is_there_a_Wumpus_here() == false))
			{
				if(Gold_Count != 0)
				{
					Gold_Count--;
					the_World[pit_x_location][pit_y_location].has_Gold = true;
					the_World[pit_x_location][pit_y_location].glitters = true;
				}
			}
			//---------------------------------------------------------------------
			//This should not place a pit if:
			//	There is already a pit there
			//	There is a Wumpus there
			//	There is Gold Here
			//	The agent starts here
			if((the_World[pit_x_location][pit_y_location].is_there_a_pit_here() == false)
					&& (the_World[pit_x_location][pit_y_location] != the_World[the_World.length-1][0])
					&& (the_World[pit_x_location][pit_y_location].is_there_a_Wumpus_here() == false)
					&& (the_World[pit_x_location][pit_y_location].is_there_a_pile_of_gold_here() == false))
			{
				pits_placed++;
				the_World[pit_x_location][pit_y_location].has_Pit = true;
				
				
				if(pit_x_location != the_World.length-1)
					the_World[pit_x_location+1][pit_y_location].is_breezy = true;
				if(pit_x_location != 0)
					the_World[pit_x_location-1][pit_y_location].is_breezy = true;
				if(pit_y_location != the_World.length-1)
					the_World[pit_x_location][pit_y_location+1].is_breezy = true;
				if(pit_y_location != 0)
					the_World[pit_x_location][pit_y_location-1].is_breezy = true;
			}

			
			
		}

	}
	
	public void revealed()
	{
		//Rows
		for (int o = 0; o < this.get_Size(); o++)
		{
			//Columns
			for(int i = 0; i < this.get_Size(); i++)
			{
				if(the_World[o][i].is_there_a_pit_here() == true)
					{System.out.print("_P_ ");}
				else if(the_World[o][i].is_there_a_Wumpus_here() == true)
					{System.out.print("_W_ ");}
				else if(the_World[o][i].is_there_a_pile_of_gold_here() == true)
					{System.out.print("_G_ ");}
				else 
				{System.out.print("___ ");}
		
			}
			System.out.println(" ");
		}
		System.out.println(" ");
		
		//Rows
		for (int o = 0; o < this.get_Size(); o++)
		{
			//Columns
			for(int i = 0; i < this.get_Size(); i++)
			{
				if(the_World[o][i].is_breezy == true)
				{System.out.print("_B_ ");}
				else if(the_World[o][i].smells == true)
				{System.out.print("_S_ ");}
				else 
				{System.out.print("___ ");}
		
			}
			System.out.println(" ");
		}
		System.out.println(" ");
	}

	public void set_Size(int size_of_the_World) {
		this.size_of_the_World = size_of_the_World;
	}

	public int get_Size() {
		return size_of_the_World;
	}

	public Tile here(int x, int y) {
		return the_World[x][y];
	}
	
}
