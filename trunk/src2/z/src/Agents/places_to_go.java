package Agents;

import java.util.ArrayList;
import environment.*;

public class places_to_go {
	ArrayList<Land> _List_next;
	
	public places_to_go()
		{_List_next = new ArrayList<Land>(1);}
	/**
	 * big, and expensive, but it will make sure that it will not repeat itself
	 * @param to_add
	 */
	public void add(Land to_add)
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
					//add to check if the area has been explored
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
		
	public Land next()
	{
		Land TEMP = new Land();
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
//if x's aren't the same, y's aren't the same, and the area has not been explored, add it to the list
/*if((((_List_next.get(w).x_coordinate == to_add.North_of_here.x_coordinate) &&
		(_List_next.get(w).y_coordinate == to_add.North_of_here.y_coordinate)) == false)
		&& to_add.North_of_here.has_been_explored == false)
{
}*/