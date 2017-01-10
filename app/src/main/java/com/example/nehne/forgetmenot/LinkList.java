package com.example.nehne.forgetmenot;

import com.example.nehne.forgetmenot.GeoFence;

public class LinkList
{
    protected GeoFence top;

    public LinkList ()
    {
	top = null;
    }

	public GeoFence getTop ()
	{
		return (top);
	}

    public void addNode (GeoFence lpr)
    {
	if (top == null)
	{
	    top = lpr;
	}
	else
	{
	    GeoFence current = null;
	    current = top;
	    while (current.getNext () != null)
	    {
		current = current.getNext ();
	    }
	    current.setNext (lpr);
	}
    }


    public int linkListLength ()
    {
	int i = 0;
	if (top == null)
	{
	    return 0;
	}
	else
	{
	    GeoFence current = top;
	    while (current.getNext () != null)
	    {
		current = current.getNext ();
		i++;
	    }
	    i++;
	    return i;
	}
    }


    public GeoFence getNode (String name)
    {
	GeoFence current = top;

	while (current.getNext () != null)
	{
	    if (current.getName ().equals (name))
	    {
		return current;
	    }
	    current = current.getNext ();
	}
	if (current.getName ().equals (name))
	{
	    return current;
	}
	else
	{
	    return current;
	}
    }


    public GeoFence getNode (double distance)
    {
	GeoFence current = top;

	while (current.getNext () != null)
	{
	    if (current.getDistance () == distance)
	    {
		return current;
	    }
	    current = current.getNext ();
	}
	if (current.getDistance () == distance)
	{
	    return current;
	}
	else
	{
	    return current;
	}
    }


    public void deleteNode (String name)
    {
	GeoFence current = top;
	GeoFence previous = top;
	while (current.getNext () != null)
	{
	    if (current == top && current.getName ().equals (name))
	    {
		top = current.getNext ();
	    }
	    else if ((current != top) && (current.getName ().equals (name)))
	    {
		previous.setNext (current.getNext ());
	    }
	    if (current != top)
	    {
		previous = previous.getNext ();
	    }
	    current = current.getNext ();
	}
	if (current.getName ().equals (name))
	{
	    previous.setNext (null);

	}

    }


    public void deleteNode (double distance)
    {
	GeoFence current = top;
	GeoFence previous = top;
	while (current.getNext () != null)
	{
	    if (current == top && current.getDistance () == distance)
	    {
		top = current.getNext ();
	    }
	    else if ((current != top) && (current.getDistance () == distance))
	    {
		previous.setNext (current.getNext ());
	    }
	    if (current != top)
	    {
		previous = previous.getNext ();
	    }
	    current = current.getNext ();
	}
	if (current.getDistance () == distance)
	{
	    previous.setNext (null);

	}

    }


    public void arraySortDistance (GeoFence link[])
    {
	int Switch = 1;
	GeoFence Temp = new GeoFence ("temp", 0.0, 0.0, 0.0, 0.0, 0.0, 0);
	while (Switch != 0)
	{
	    Switch = 0;
	    for (int j = 0 ; j < link.length - 1 ; j++)
	    {
		if (link [j].getDistance () > link [j + 1].getDistance ())
		{
		    Temp.setName (link [j].getName ());
		    Temp.setRadius (link [j].getRadius ());
		    Temp.setLongitude (link [j].getLongitude ());
		    Temp.setLatitude (link [j].getLatitude ());
		    Temp.setDistance (link [j].getDistance ());
		    Temp.setTime (link [j].getTime ());
		    link [j].setName (link [j + 1].getName ());
		    link [j].setRadius (link [j + 1].getRadius ());
		    link [j].setLongitude (link [j + 1].getLongitude ());
		    link [j].setLatitude (link [j + 1].getLatitude ());
		    link [j].setDistance (link [j + 1].getDistance ());
		    link [j].setTime (link [j + 1].getTime ());
		    link [j + 1].setName (Temp.getName ());
		    link [j + 1].setRadius (Temp.getRadius ());
		    link [j + 1].setLongitude (Temp.getLongitude ());
		    link [j + 1].setLatitude (Temp.getLatitude ());
		    link [j + 1].setDistance (Temp.getDistance ());
		    link [j + 1].setTime (Temp.getTime ());
		    Switch = 1;
		}
	    }
	}
    }



    public void arraySortName (GeoFence link[])
    {
	int Switch = 1;
	GeoFence Temp = new GeoFence ("temp", 0.0, 0.0, 0.0, 0.0, 0.0, 0);
	while (Switch != 0)
	{
	    Switch = 0;
	    for (int j = 0 ; j < link.length - 1 ; j++)
	    {
		if (link [j].getName ().compareTo (link [j + 1].getName ()) > 0)
		{
		    Temp.setName (link [j].getName ());
		    Temp.setRadius (link [j].getRadius ());
		    Temp.setLongitude (link [j].getLongitude ());
		    Temp.setLatitude (link [j].getLatitude ());
		    Temp.setDistance (link [j].getDistance ());
		    Temp.setTime (link [j].getTime ());
		    link [j].setName (link [j + 1].getName ());
		    link [j].setRadius (link [j + 1].getRadius ());
		    link [j].setLongitude (link [j + 1].getLongitude ());
		    link [j].setLatitude (link [j + 1].getLatitude ());
		    link [j].setDistance (link [j + 1].getDistance ());
		    link [j].setTime (link [j + 1].getTime ());
		    link [j + 1].setName (Temp.getName ());
		    link [j + 1].setRadius (Temp.getRadius ());
		    link [j + 1].setLongitude (Temp.getLongitude ());
		    link [j + 1].setLatitude (Temp.getLatitude ());
		    link [j + 1].setDistance (Temp.getDistance ());
		    link [j + 1].setTime (Temp.getTime ());
		    Switch = 1;
		}
	    }
	}
    }


    public void showList ()

    {

	if (top == null)

	    {

		System.out.println ("Empty list");

	    }

	else

	    {

		System.out.println ("The current list");

		GeoFence current;

		current = top;

		while (current.getNext () != null)

		    {

			System.out.println (current.getName () + " " + current.getLongitude ());

			current = current.getNext ();

		    }

		System.out.println (current.getName () + " " + current.getLongitude ());

		System.out.println ("*******");

	    }

    }


    public void distanceSort ()
    {
	boolean switched = true;
	GeoFence previous = null;
	GeoFence current = null;
	GeoFence after = null;


	while (switched)
	{
	    current = top;
	    after = current.getNext ();
	    previous = null;
	    switched = false;
	    while (after != null)
	    {
		double currentDistance = current.getDistance ();
		double afterDistance = after.getDistance ();

		if (currentDistance > afterDistance && previous == null)
		{
		    current.setNext (after.getNext ());
		    after.setNext (current);
		    top = after;
		    switched = true;
		}

		else if (currentDistance > afterDistance)
		{
		    current.setNext (after.getNext ());
		    after.setNext (current);
		    previous.setNext (after);
		    switched = true;
		}

		if (previous == null)
		{
		    previous = top;
		    current = previous.getNext ();
		    after = current.getNext ();
		}

		else if (previous.getNext () != null)
		{
		    previous = previous.getNext ();
		    current = previous.getNext ();
		    after = current.getNext ();
		}

	    }
	}
    }

    public void nameSort ()
    {
	boolean switched = true;
	GeoFence previous = null;
	GeoFence current = null;
	GeoFence after = null;


	while (switched)
	{
	    current = top;
	    after = current.getNext ();
	    previous = null;
	    switched = false;
	    while (after != null)
	    {
		String currentName = current.getName ();
		String afterName = after.getName ();

		if (currentName.compareTo(afterName) > 0 && previous == null)
		{
		    current.setNext (after.getNext ());
		    after.setNext (current);
		    top = after;
		    switched = true;
		}

		else if (currentName.compareTo(afterName) > 0 )
		{
		    current.setNext (after.getNext ());
		    after.setNext (current);
		    previous.setNext (after);
		    switched = true;
		}

		if (previous == null)
		{
		    previous = top;
		    current = previous.getNext ();
		    after = current.getNext ();
		}

		else if (previous.getNext () != null)
		{
		    previous = previous.getNext ();
		    current = previous.getNext ();
		    after = current.getNext ();
		}

	    }
	}
    }
}
// end bubbleSort


