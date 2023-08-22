package ke.co.safaricom.dao;

import ke.co.safaricom.models.Hero;
import org.sql2o.*;
import java.util.List;

public class HeroesDaoSqlO implements HeroesDao {

  private final Sql2o sql2o;

  public HeroesDaoSqlO(Sql2o sql2o) {
    this.sql2o = sql2o;
  }

  @Override
  public List<Hero> getAll() {
    try (Connection con = sql2o.open()) {
      return con.createQuery("SELECT * FROM heroes")
        .executeAndFetch(Hero.class);
    }
  }

  @Override
  public void add(Hero hero) {
    String sql = "INSERT INTO heroes (name, age, origin_story, powers, weaknesses, squad_id) VALUES (:name, :age, :origin_story, :powers, :weaknesses, :squad_id)";
    try (Connection con = sql2o.open()) {
      int id = (int) con.createQuery(sql, true)
        .bind(hero)
        .executeUpdate()
        .getKey();
      hero.setId(id);
    } catch (Sql2oException ex) {
      System.out.println(ex);
    }
  }


  @Override
  public Hero findById(int id) {
    try (Connection con = sql2o.open()) {
      return con.createQuery("SELECT * FROM heroes WHERE id = :id")
        .addParameter("id", id)
        .executeAndFetchFirst(Hero.class);
    }
  }


  @Override
  public void update(int id, String newName, int newAge, String newOrigin_story, String newPowers, String newWeaknesses, int newSquad_id) {
    String sql = "UPDATE heroes SET name = :name, age = :age, origin_story = :origin_story, powers = :powers, weaknesses = :weaknesses, squad_id = :squad_id WHERE id = :id";
    try (Connection con = sql2o.open()) {
      con.createQuery(sql)
        .addParameter("name", newName)
        .addParameter("age", newAge)
        .addParameter("origin_story", newOrigin_story)
        .addParameter("powers", newPowers)
        .addParameter("weaknesses", newWeaknesses)
        .addParameter("squad_id", newSquad_id)
        .addParameter("id", id)
        .executeUpdate();
    } catch (Sql2oException ex) {
      System.out.println(ex);
    }
  }

  @Override
  public void deleteById(int id) {
    String sql = "DELETE FROM heroes WHERE id = :id";
    try (Connection con = sql2o.open()) {
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
    } catch (Sql2oException ex) {
      System.out.println(ex);
    }
  }

  @Override
  public void clearAllHeroes() {
    String sql = "DELETE FROM heroes";
    try (Connection con = sql2o.open()) {
      con.createQuery(sql)
        .executeUpdate();
    } catch (Sql2oException ex) {
      System.out.println(ex);
    }
  }
}
