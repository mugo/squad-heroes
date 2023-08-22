package ke.co.safaricom.dao;


import ke.co.safaricom.models.Hero;
import ke.co.safaricom.models.Squad;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import java.util.List;

public class SquadDaoSqlO implements SquadDao {

  private final Sql2o sql2o;

  public SquadDaoSqlO(Sql2o sql2o) { this.sql2o = sql2o; }

  @Override
  public List<Squad> getAll() {
    try (Connection con = sql2o.open()) {
      return con.createQuery("SELECT * FROM squads")
        .executeAndFetch(Squad.class);
    }
  }


  @Override
  public void add(Squad squad) {
    String sql = "INSERT INTO squads (name, size, cause) VALUES (:name, :size, :cause)";
    try (Connection con = sql2o.open()) {
      int id = (int) con.createQuery(sql, true)
        .bind(squad)
        .executeUpdate()
        .getKey();
      squad.setId(id);
    } catch (Sql2oException ex) {
      System.out.println(ex);
    }

  }

  @Override
  public Squad findById(int id) {
    try (Connection con = sql2o.open()) {
      return con.createQuery("SELECT * FROM squads WHERE id = :id")
        .addParameter("id", id)
        .executeAndFetchFirst(Squad.class);
    }
  }

  @Override
  public List<Hero> getAllHeroesBySquad(int squad_id) {
    try (Connection con = sql2o.open()) {
      return con.createQuery("SELECT * FROM heroes WHERE squad_id = :squad_id")
        .addParameter("squad_id", squad_id)
        .executeAndFetch(Hero.class);
    }
  }

  @Override
  public void update(int id, String newName, int newSize, String newCause) {
    String sql = "UPDATE squads SET name = :name, size = :size, cause = :cause WHERE id = :id";
    try (Connection con = sql2o.open()) {
      con.createQuery(sql)
        .addParameter("name", newName)
        .addParameter("size", newSize)
        .addParameter("cause", newCause)
        .executeUpdate();
    } catch (Sql2oException ex) {
      System.out.println(ex);
    }
  }

  @Override
  public void deleteById(int id) {
    String sql = "DELETE from squads WHERE id=:id"; //raw sql
    try (Connection con = sql2o.open()) {
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
    } catch (Sql2oException ex) {
      System.out.println(ex);
    }
  }

  @Override
  public void clearAllSquads() {
    String sql = "DELETE from squads"; //raw sql
    try (Connection con = sql2o.open()) {
      con.createQuery(sql)
        .executeUpdate();
    } catch (Sql2oException ex) {
      System.out.println(ex);
    }
  }
}
