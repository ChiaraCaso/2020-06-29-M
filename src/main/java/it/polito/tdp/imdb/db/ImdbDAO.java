package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mariadb.jdbc.internal.com.read.dao.Results;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Arco;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(Map <Integer, Director> idMap ){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				idMap.put(res.getInt("id"), director);
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Integer> getAnni () {
		String sql = "SELECT DISTINCT YEAR " + 
				"FROM movies " + 
				"WHERE YEAR = 2004 " + 
				"OR YEAR = 2005 " + 
				"OR YEAR = 2006 " ;
		
		List <Integer> result = new ArrayList<Integer>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				result.add(res.getInt("YEAR"));
			}
			
			conn.close();
			return result;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List <Director> getDirector (Integer anno, Map<Integer, Director> idMap) {
		String sql = "SELECT DISTINCT d.id AS id " + 
				"FROM directors d, movies_directors md, movies m " + 
				"WHERE d.id = md.director_id " + 
				"AND m.id = md.movie_id " + 
				"AND YEAR = ? " + 
				"ORDER BY id ASC "; 
		
		List <Director> result = new ArrayList<Director>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				Director d = idMap.get(res.getInt("id"));
				result.add(d);
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List <Arco> getArchi (Integer anno, Map <Integer, Director> idMap) {
		String sql = "SELECT md1.director_id AS id1, md2.director_id AS id2, COUNT(DISTINCT(r1.actor_id)) AS peso " + 
				"FROM movies_directors md1, movies_directors md2, movies m1, movies m2 , roles r1, roles r2 " + 
				"WHERE md1.movie_id = m1.id " + 
				"AND md2.movie_id = m2.id " + 
				"AND md1.director_id > md2.director_id " + 
				"AND (m1.id = m2.id OR m1.id <> m2.id) " + 
				"AND m1.id = r1.movie_id " + 
				"AND m2.id = r2.movie_id " + 
				"AND (r1.movie_id = r2.movie_id OR r1.movie_id <> r2.movie_id) " + 
				"AND r1.actor_id = r2.actor_id " + 
				"AND m1.year = ? " + 
				"AND m2.year = ? " + 
				"GROUP BY id1, id2 " +
				"ORDER BY peso DESC ";
		
		List <Arco> result = new ArrayList<Arco>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				Director d1 = idMap.get(res.getInt("id1"));
				Director d2 = idMap.get(res.getInt("id2"));
				
				if (d1 != null && d2 != null) {
					result.add(new Arco(d1, d2, res.getInt("peso")));
				}
			}
			
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
