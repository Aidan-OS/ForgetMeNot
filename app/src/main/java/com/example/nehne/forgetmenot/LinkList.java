package com.example.nehne.forgetmenot;

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


    public void sortDistance (GeoFence link[])
    {
	GeoFence temp = new GeoFence ("temp", 0.0, 0.0, 0.0, 0);
	int j;
	GeoFence[] Sorted = new GeoFence [link.length + 1];
	for (int i = 0 ; i < link.length + 1 ; i++)
	{
	    j = i;
	    Sorted [j].setDistance (Sorted [j - 1].getLongitude (), Sorted [j - 1].getLatitude ());
	    while (Sorted [j].getDistance () < Sorted [j - 1].getDistance ())
	    {
		temp.setLongitude (Sorted [j].getLongitude ());
		temp.setLatitude (Sorted [j].getLatitude ());
		Sorted [j].setDistance (Sorted [j - 1].getLongitude (), Sorted [j - 1].getLatitude ());
		Sorted [j - 1].setDistance (temp.getLongitude (), temp.getLatitude ());
		j--;
	    }
	}
	for (int i = 1 ; i < Sorted.length ; i++)
	{
	    link [i - 1] = Sorted [i];
	}
    }


    public void sortName (GeoFence link[])
    {
	GeoFence temp = new GeoFence ("temp", 0.0, 0.0, 0.0, 0);
	int j;
	GeoFence[] Sorted = new GeoFence [link.length + 1];
	for (int i = 0 ; i < link.length + 1 ; i++)
	{
	    j = i;
	    Sorted [j].setName (Sorted [j - 1].getName ());
	    while (Sorted [j].getName ().compareTo (Sorted [j - 1].getName ()) > 0)
	    {
		temp.setName (Sorted [j].getName ());
		Sorted [j].setName (Sorted [j - 1].getName ());
		Sorted [j - 1].setName (temp.getName ());
		j--;
	    }
	}
	for (int i = 1 ; i < Sorted.length ; i++)
	{
	    link [i - 1] = Sorted [i];
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
}


