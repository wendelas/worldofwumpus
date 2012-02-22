package Agents;

import java.util.ArrayList;
import environment.*;

/**
 * @author ebertb schmidbauerk
 * Directions class determines which direction to go
 */
public class Directions {
	ArrayList<Tile> _List_next;
	
	public Directions()
		{_List_next = new ArrayList<Tile>(1);}

	public void add(Tile to_add)
	{
	if(_List_next.isEmpty() == true)
			{
			if(to_add.North_of_here != null)
				_List_next.add(to_add.North_of_here);
			if(to_add.East_of_here != null)
				_List_next.add(to_add.East_of_here);
			if(to_add.West_of_here != null)
				_List_next.add(to_add.West_of_here);
			if(to_add.South_of_here != null)
				_List_next.add(to_add.South_of_here);
			} 
		else
		{
			if(to_add.North_of_here != null)
			{
				for(int i = 0; i < _List_next.size(); i++)
				{
					
					if((to_add.North_of_here.threat_rating < _List_next.get(i).threat_rating) && (to_add.North_of_here.has_been_explored == false))
					{
						_List_next.add(i, to_add.North_of_here);
						i = _List_next.size() + 1;
					}
					
					if((i == _List_next.size()-1)&& (to_add.North_of_here.has_been_explored == false))
					{
						_List_next.add(to_add.North_of_here);
						i = _List_next.size() + 2;
					}
	
				}
			}
			
			if(to_add.South_of_here != null)
			{
				for(int i = 0; i < _List_next.size(); i++)
				{
					
					if((to_add.South_of_here.threat_rating < _List_next.get(i).threat_rating)&& (to_add.South_of_here.has_been_explored == false))
					{
						_List_next.add(i, to_add.South_of_here);
						i = _List_next.size() + 1;
					}
					if((i == _List_next.size()-1)&& (to_add.South_of_here.has_been_explored == false))
					{
						_List_next.add(to_add.South_of_here);
						i = _List_next.size() + 2;
					}
	
				}
			}
			
			if(to_add.West_of_here != null)
			{
				for(int i = 0; i < _List_next.size(); i++)
				{
					
					if((to_add.West_of_here.threat_rating < _List_next.get(i).threat_rating) && (to_add.West_of_here.has_been_explored == false))
					{
						_List_next.add(i, to_add.West_of_here);
						i = _List_next.size() + 1;
					}
					if((i == _List_next.size()-1)&& (to_add.West_of_here.has_been_explored == false))
					{
						_List_next.add(to_add.West_of_here);
						i = _List_next.size() + 2;
					}
	
				}
			}
			
			if(to_add.East_of_here != null)
			{
				for(int i = 0; i < _List_next.size(); i++)
				{
					
					if((to_add.East_of_here.threat_rating < _List_next.get(i).threat_rating)&& (to_add.East_of_here.has_been_explored == false))
					{
						_List_next.add(i, to_add.East_of_here);
						i = _List_next.size() + 1;
					}
					if((i == _List_next.size()-1)&& (to_add.East_of_here.has_been_explored == false))
					{
						_List_next.add(to_add.East_of_here);
						i = _List_next.size() + 2;
					}
	
				}
			}
		}
	}
		
	public Tile next()
	{
		Tile TEMP = new Tile();
		if(_List_next.isEmpty())
		{
			return null;
		} else {
			TEMP = _List_next.get(0);
			_List_next.remove(0);			
			return TEMP;
		}
			
	}
}
