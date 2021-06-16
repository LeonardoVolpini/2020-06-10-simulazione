package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {

	private SimpleWeightedGraph<Actor, DefaultWeightedEdge> grafo;
	private ImdbDAO dao;
	private Map<Integer,Actor> idMap;
	private List<Actor> vertici;
	private boolean isCreato;
	private ConnectivityInspector<Actor, DefaultWeightedEdge> ci;
	
	private Simulator sim;
	
	public Model() {
		this.dao= new ImdbDAO();
		this.idMap= new HashMap<>();
		this.vertici= new ArrayList<>();
		this.isCreato=false;
		this.sim= new Simulator();
	}
	
	public List<String> getAllGenres(){
		return dao.allGenres();
	}
	
	public void creaGrafo(String genere) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.dao.loadIdMap(idMap);
		this.vertici= this.dao.getVertici(idMap, genere);
		Graphs.addAllVertices(grafo, this.vertici);
		for (Adiacenza a : dao.getAdiacenze(idMap, genere)) {
			Graphs.addEdgeWithVertices(grafo, a.getA1(), a.getA2(), a.getPeso());
		}
		this.isCreato=true;
		this.ci= new ConnectivityInspector<>(grafo);
	}

	public int getNumVertici() {
		return grafo.vertexSet().size();
	}

	public int getNumArchi() {
		return grafo.edgeSet().size();
	}

	public List<Actor> getVertici() {
		Collections.sort(this.vertici);
		return this.vertici;
	}

	public boolean isGrafoCreato() {
		return isCreato;
	}
	
	public List<Actor> raggiungibili(Actor actor){
		List<Actor> result = new ArrayList<>();
		for (Actor a : ci.connectedSetOf(actor)) {
			result.add(a);
		}
		result.remove(actor);
		Collections.sort(result);
		return result;
	}
	
	public void simula(int n, String genere) {
		this.sim.init(n, genere, grafo);
		this.sim.run();
	}
	
	public int numAttoriIntervistati() {
		return sim.getNumAttoriIntervistati();
	}
	
	public int numPause() {
		return sim.getNumPause();
	}
}
