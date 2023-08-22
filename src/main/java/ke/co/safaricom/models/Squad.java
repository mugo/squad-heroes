package ke.co.safaricom.models;

import java.util.ArrayList;

public class Squad {
  private int id;
  private String name;
  private int size;
  private String cause;
//  private static ArrayList<Squad> squadsAvailable = new ArrayList<>();

  public Squad(String name, int size, String cause) {
//    this.id = squadsAvailable.size();
    this.name = name;
    this.size = size;
    this.cause = cause;
//    squadsAvailable.add(this);
  }

  public void setId(int id) { this.id = id; }

  public void setName(String name) {
    this.name = name;
  }

  public void setSize(int size) {
      this.size = size;
  }

  public void setCause(String cause) { this.cause = cause; }

  public int getId() { return id; }

  public String getName() { return name; }

  public int getSize() { return size; }

  public String getCause() { return cause; }

}
