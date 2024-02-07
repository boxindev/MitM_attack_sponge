package mitmsearch.mitm;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.io.File;
import java.io.IOException;

public class MitmSolution {
  public int Rounds;
  public int objective;
  public int[][] Cond;
  public int[][][][] DA;
  public int[][][][] DB;
  public int[][][][] DC;
  public int[] dom;
  public int[] obj;

  public MitmSolution() {}

  public MitmSolution(int Rounds, int objective, int[][] Cond, int[][][][] DA, int[][][][] DB, int[][][][]DC, int[] dom, int[] obj) {
    this.Rounds = Rounds;
    this.objective = objective;
    this.Cond = Cond;
    this.DA = DA;
    this.DB = DB;
    this.DC = DC;   
    this.dom = dom;
    this.obj = obj;
  }

  public void toFile(String fileName) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      mapper.writeValue(new File(fileName), this);
    }
    catch (JsonParseException e) { e.printStackTrace(); System.exit(1); }
    catch (JsonMappingException e) { e.printStackTrace(); System.exit(1); }
    catch (IOException e) { e.printStackTrace(); System.exit(1); }
  }

  public static void toFile(String fileName, List<MitmSolution> solutions) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      mapper.writeValue(new File(fileName), solutions);
    }
    catch (JsonParseException e) { e.printStackTrace(); System.exit(1); }
    catch (JsonMappingException e) { e.printStackTrace(); System.exit(1); }
    catch (IOException e) { e.printStackTrace(); System.exit(1); }
  }

  public static void toFile(String fileName, MitmSolution solutions) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      mapper.writeValue(new File(fileName), solutions);
    }
    catch (JsonParseException e) { e.printStackTrace(); System.exit(1); }
    catch (JsonMappingException e) { e.printStackTrace(); System.exit(1); }
    catch (IOException e) { e.printStackTrace(); System.exit(1); }
  }

  public static void toFile(File file, List<MitmSolution> solutions) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      mapper.writeValue(file, solutions);
    }
    catch (JsonParseException e) { e.printStackTrace(); System.exit(1); }
    catch (JsonMappingException e) { e.printStackTrace(); System.exit(1); }
    catch (IOException e) { e.printStackTrace(); System.exit(1); }
  }

  public static List<MitmSolution> fromFile(String fileName) {
    return fromFile(new File(fileName));
  }

  public static List<MitmSolution> fromFile(File file) {
    try {
      return new ObjectMapper().readValue(file, new TypeReference<List<MitmSolution>>(){});
    }
    catch (JsonParseException e) { e.printStackTrace(); System.exit(1); }
    catch (JsonMappingException e) { e.printStackTrace(); System.exit(1); }
    catch (IOException e) { e.printStackTrace(); System.exit(1); }
    return null; // Can't reach
  }
}
