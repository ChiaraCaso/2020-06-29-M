package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {

	
	ImdbDAO dao ;
	Graph<Director, DefaultWeightedEdge> grafo;
	Map<Integer, Director> idMap;
	List <Director> director;
	List <Arco> archi;
	
	public Model() {
		this.dao = new ImdbDAO();
		this.grafo = new SimpleWeightedGraph<Director, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.idMap = new HashMap<Integer, Director>();
		this.director = new ArrayList<Director>();
		this.archi = new ArrayList<Arco>();
	}
	
	
	public void creaGrafo (Integer anno) {
		
		dao.listAllDirectors(idMap);
		
		this.director = this.dao.getDirector(anno, idMap);
		
		Graphs.addAllVertices(this.grafo, director);
		
		this.archi = this.dao.getArchi(anno, idMap);
		
		for (Arco a : archi) {
			if (this.grafo.containsVertex(a.getD1()) && this.grafo.containsVertex(a.getD2())) {
				Graphs.addEdgeWithVertices(this.grafo, a.getD1(), a.getD2(), a.getPeso());
			}
		}
	}
	
	public List <Integer> getAnni () {
		return this.dao.getAnni();
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi () {
		return this.grafo.edgeSet().size();
	}
	
	public List<Director> getRegista (Integer anno) {
		return this.dao.getDirector(anno, idMap);
	}
	
	public String getAdiacenti (Director d) {
		String s = "";
		List<Arco> result = new ArrayList<>();
		List<Director> vicini= Graphs.neighborListOf(grafo, d);
		
		for(Director v: vicini) {
			if(!result.contains(v)) {
				Integer peso= (int) this.grafo.getEdgeWeight(this.grafo.getEdge(d, v));
				Arco a= new Arco(d,v,peso);
				s +=v.getId()+" - "+ v.getFirstName() + " " +v.getLastName() + " - #Attori Condivisi: " +peso +"\n";
				result.add(a);
			}
		}
		
		return s;
	}
}
