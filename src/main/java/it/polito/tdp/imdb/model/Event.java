package it.polito.tdp.imdb.model;

public class Event implements Comparable<Event>{

	public enum EventType{
		SCELTA_CASUALE,
		SCELTA_VICINO,
		PAUSA
	}
	
	private int giorno;
	private EventType type;
	
	public Event(int giorno, EventType type) {
		this.giorno = giorno;
		this.type = type;
		//this.attore = attore;
	}

	public int getGiorno() {
		return giorno;
	}

	public void setGiorno(int giorno) {
		this.giorno = giorno;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	/*public Actor getAttore() {
		return attore;
	}

	public void setAttore(Actor attore) {
		this.attore = attore;
	}*/

	@Override
	public String toString() {
		return "Event [giorno=" + giorno + ", type=" + type;
	}

	@Override
	public int compareTo(Event o) {
		return this.giorno-o.giorno;
	}
	
	
}
