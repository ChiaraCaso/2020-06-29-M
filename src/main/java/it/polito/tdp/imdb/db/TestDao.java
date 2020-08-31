package it.polito.tdp.imdb.db;

import java.util.HashMap;
import java.util.Map;

import it.polito.tdp.imdb.model.Director;

public class TestDao {

	public static void main(String[] args) {
		TestDao testDao = new TestDao();
		testDao.run();
	}
	
	public void run() {
		
		Map<Integer, Director> idMap = new HashMap<Integer, Director>();
		ImdbDAO dao = new ImdbDAO();
		System.out.println("Actors:");
		System.out.println(dao.listAllActors());
		System.out.println("Movies:");
		System.out.println(dao.listAllMovies());
		System.out.println("Directors:");
		System.out.println(dao.listAllDirectors(idMap));
		System.out.println("\n\n" + dao.getArchi(2005, idMap) + "\n");
	}

}
