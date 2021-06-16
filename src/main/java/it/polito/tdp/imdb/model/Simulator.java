package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.model.Event.EventType;

public class Simulator {

	//modello del mondo 
	private SimpleWeightedGraph<Actor, DefaultWeightedEdge> grafo;
	private List<Actor> daIntervistare;
	private int countM;
	private int countF;
	private Actor piuVicino;
	
	//coda degli eventi
	private PriorityQueue<Event> queue;
	
	//parametri in input
	private int n; //numero di giorni
	private String genere;
	
	//parametri in output
	private int numPause;
	private int numAttoriIntervistati;
	
	
	public void init(int nGiorni, String genere, SimpleWeightedGraph<Actor, DefaultWeightedEdge> g) {
		this.grafo=g;
		this.daIntervistare= new ArrayList<>();
		for (Actor a : this.grafo.vertexSet()) 
			this.daIntervistare.add(a);
		this.queue= new PriorityQueue<>();
		this.countM=0;
		this.countF=0;
		this.piuVicino=null;
		
		this.n=nGiorni;
		this.genere=genere;
		
		this.numPause=0;
		this.numAttoriIntervistati=0;
		
		this.queue.add( new Event(1,EventType.SCELTA_CASUALE) );
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		int day= e.getGiorno();
		
		if(day==this.n) //ho finito i giorni a disposizione
			return;
		
		switch(e.getType()) {
		case SCELTA_CASUALE:
			int scelto= (int) (Math.random()*this.daIntervistare.size());
			Actor a = this.daIntervistare.get(scelto-1);
			this.daIntervistare.remove(a);
			if(a.getGender()=="M") {
				this.countM++;
				this.countF=0;
			}
			else {
				this.countF++;
				this.countM=0;
			}
			//poi schedulo
			if(this.countF==2 || this.countM==2) {
				this.countF=0;
				this.countM=0;
				int prob=(int) (Math.random()*100);
				if (prob<90)
					this.queue.add(new Event(day+1,EventType.PAUSA) );
				else {
					int probab= (int) (Math.random()*100);
					if(probab<60) {
						this.queue.add( new Event(day+1,EventType.SCELTA_CASUALE));
					}
					else {
						this.piuVicino = this.attorePiuVicino(a);
						if (piuVicino==null)
							this.queue.add( new Event(day+1,EventType.SCELTA_CASUALE));
						else {
							this.queue.add( new Event(day+1,EventType.SCELTA_VICINO));
						}
					}
				}
			}
			else {
				int probab= (int) (Math.random()*100);
				if(probab<60) {
					this.queue.add( new Event(day+1,EventType.SCELTA_CASUALE));
				}
				else {
					this.piuVicino = this.attorePiuVicino(a);
					if (piuVicino==null)
						this.queue.add( new Event(day+1,EventType.SCELTA_CASUALE));
					else {
						this.queue.add( new Event(day+1,EventType.SCELTA_VICINO));
					}
				}
			}
			this.numAttoriIntervistati++;
			break;
		case SCELTA_VICINO:
			Actor ac = this.piuVicino;
			this.daIntervistare.remove(ac);
			if(ac.getGender()=="M") {
				this.countM++;
				this.countF=0;
			}
			else {
				this.countF++;
				this.countM=0;
			}
			//poi schedulo
			if(this.countF==2 || this.countM==2) {
				this.countF=0;
				this.countM=0;
				int prob=(int) (Math.random()*100);
				if (prob<90)
					this.queue.add(new Event(day+1,EventType.PAUSA) );
				else {
					int probab= (int) (Math.random()*100);
					if(probab<60) {
						this.queue.add( new Event(day+1,EventType.SCELTA_CASUALE));
					}
					else {
						this.piuVicino = this.attorePiuVicino(ac);
						if (piuVicino==null)
							this.queue.add( new Event(day+1,EventType.SCELTA_CASUALE));
						else {
							this.queue.add( new Event(day+1,EventType.SCELTA_VICINO));
						}
					}
				}
			}
			else {
				int probab= (int) (Math.random()*100);
				if(probab<60) {
					this.queue.add( new Event(day+1,EventType.SCELTA_CASUALE));
				}
				else {
					this.piuVicino = this.attorePiuVicino(ac);
					if (piuVicino==null)
						this.queue.add( new Event(day+1,EventType.SCELTA_CASUALE));
					else {
						this.queue.add( new Event(day+1,EventType.SCELTA_VICINO));
					}
				}
			}
			this.numAttoriIntervistati++;
			break;
		case PAUSA:
			this.numPause++;
			this.queue.add(new Event(day+1,EventType.SCELTA_CASUALE));
			break;
		}
		
	}
	
	private Actor attorePiuVicino(Actor actor) {
		int max=0;
		Actor best=null;
		for (Actor a : Graphs.neighborListOf(grafo, actor)) {
			int grado= grafo.degreeOf(a);
			if (grado>max && this.daIntervistare.contains(a)) {
				max=grado;
				best=a;
			}
		}
		return best;
	}

	public int getNumPause() {
		return numPause;
	}

	public int getNumAttoriIntervistati() {
		return numAttoriIntervistati;
	}
	
	
	
}
