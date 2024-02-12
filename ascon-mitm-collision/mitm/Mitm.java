package mitmsearch.mitm;

import gurobi.*;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
public class Mitm {
  private final int Rounds;
  private final GRBModel model;
  private FileWriter logfile ;
  private final MitmFactory factory;
  private final GRBVar[][] Cond;
  private final GRBVar[][][][] DA;
  private final GRBVar[][][][] DB;
  private final GRBVar[][][] DC;
  private final GRBLinExpr   objective;
  private final GRBVar[] obj;
  private final GRBVar[] dom;

  /**
   * @param env the Gurobi environment
   */
  public Mitm(final GRBEnv env, final int Rounds) throws GRBException {
    model = new GRBModel(env);
    this.Rounds = Rounds;

    factory = new MitmFactory(model);
    Cond = new GRBVar[32][2];
    DA = new GRBVar[Rounds+1][32][5][3];
    DB = new GRBVar[Rounds][32][5][3];
    DC = new GRBVar[Rounds][32][5];
    // Initialization

    for (int k = 0; k < 32; k++)
      for (int l = 0; l < 2; l++) {
        Cond[k][l] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "Cond_"+k+"_"+l);
      }

    for (int round = 0; round < Rounds+1; round++)
      for (int i = 0; i < 32; i++)
        for (int j = 0; j < 5; j++)
          for (int l = 0; l < 3; l++) {
            DA[round][i][j][l] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "DA_"+round+"_"+i+"_"+j+"_"+l);
          }

    for (int round = 0; round < Rounds; round++)
      for (int i = 0; i < 32; i++)
        for (int j = 0; j < 5; j++)
          for (int l = 0; l < 3; l++) {
            DB[round][i][j][l] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "DB_"+round+"_"+i+"_"+j+"_"+l);
          }

    for (int round = 0; round < Rounds; round++)
      for (int i = 0; i < 32; i++)
        for (int j = 0; j < 5; j++)  {
          DC[round][i][j] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "DC_"+round+"_"+i+"_"+j);

        }


    dom = new GRBVar[32];
    for (int i = 0; i < 32; i++) {
      dom[i] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "dom_"+i);
    }

    GRBVar[] DC_match = new GRBVar[32];
    for (int i = 0; i < 32; i++) {
      DC_match[i] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "DC_match_"+"_"+i);
    }

    //fixed input
    //ArticleTrail();
    factory.addfixed_in(DA, DB, Cond);
    // Constraints
    factory.addxor(DA, DB, DC);
    factory.addSboxnew(DA, DB);
    //factory.addSboxCond(DA, DB);

    factory.addDoM_ally(DA, dom);
    //factory.addDoM_y2(DA, dom);
    //factory.addDoM_new_y4(DA, dom,DC_match);



    objective = new GRBLinExpr();
    GRBLinExpr DoF_red = new GRBLinExpr();
    GRBLinExpr DoF_blue = new GRBLinExpr();
    GRBLinExpr DoM = new GRBLinExpr();
    GRBLinExpr Cond_Count = new GRBLinExpr();

    GRBVar Obj = model.addVar(0.0, 1600.0, 0.0, GRB.INTEGER, "Obj");

    obj = new GRBVar[3];
    obj[0] = model.addVar(0.0, 1600.0, 0.0, GRB.INTEGER, "Obj1");
    obj[1] = model.addVar(0.0, 1600.0, 0.0, GRB.INTEGER, "Obj2");
    obj[2] = model.addVar(0.0, 1600.0, 0.0, GRB.INTEGER, "Obj3");


    for (int k = 0; k < 32; k++) {
      DoF_red.addTerm(-1.0, DA[0][k][0][0]);
      DoF_red.addConstant(1.0);
      DoF_blue.addTerm(-1.0, DA[0][k][0][2]);
      DoF_blue.addConstant(1.0);
    }


    for (int round = 0; round < Rounds; round ++)
      for (int i = 0; i < 32; i++)
        for (int k = 0; k < 5; k++) {
          DoF_red.addTerm(-1.0, DC[round][i][k]);
        }

    for (int i = 0; i < 32; i++) {
      DoF_red.addTerm(-1.0, DC_match[i]);
    }




    for (int i = 0; i < 32; i++) {
      DoM.addTerm(4.0, dom[i]);
    }

	
    for (int k = 0; k < 32; k++)
      for (int l = 0; l < 2; l++) {
        Cond_Count.addTerm(1.0,Cond[k][l]);
      }
    for (int i = 0; i < 32; i++) {
      Cond_Count.addTerm(4.0, dom[i]);
    }

    objective.addTerm(1.0, Obj);

    //model.addConstr(DoF_red, GRB.GREATER_EQUAL, 4, "");
    //model.addConstr(DoF_blue, GRB.GREATER_EQUAL, 4, "");
    //model.addConstr(DoM, GRB.GREATER_EQUAL, 1, "");
    
    model.addConstr(Cond_Count, GRB.LESS_EQUAL, 28, "");

    model.addConstr(DoF_red, GRB.EQUAL, obj[0], "");
    model.addConstr(DoF_blue, GRB.EQUAL, obj[1], "");
    model.addConstr(DoM, GRB.EQUAL, obj[2], "");

    model.addConstr(objective, GRB.LESS_EQUAL, DoF_blue, "");
    model.addConstr(objective, GRB.LESS_EQUAL, DoF_red, "");
    model.addConstr(objective, GRB.LESS_EQUAL, DoM, "");
    //model.addConstr(objective, GRB.LESS_EQUAL, 20, "");
    model.setObjective(objective, GRB.MAXIMIZE);
  }

  public List<MitmSolution> solve(final int nSolutions, final boolean nonOptimalSolutions, final int minObjValue, final int Threads) throws GRBException {
    model.read("tune1.prm");
    model.write("model.lp");
    model.set(GRB.IntParam.Threads, Threads);
    if (minObjValue != -1)
      model.addConstr(objective, GRB.EQUAL, minObjValue, "objectiveFix");
    model.set(GRB.IntParam.DualReductions, 0);

    try {
      FileWriter logfile = null;
      logfile = new FileWriter("cb.log");
    }
    catch (IOException e) { e.printStackTrace(); System.exit(1); }

    GRBVar[] vars = model.getVars();
    System.out.println (vars.length);
    System.out.println (vars[0].get(GRB.StringAttr.VarName));
    Callback cb = new Callback(vars,logfile);
    model.setCallback(cb);

    model.optimize();
    model.write("output.sol");
    //model.computeIIS();
    //model.write("model1.ilp");
    return getAllFoundSolutions();
  }

  public void dispose() throws GRBException {
    model.dispose();
  }

  public List<MitmSolution> getAllFoundSolutions() throws GRBException {
    return IntStream.range(0, model.get(GRB.IntAttr.SolCount)).boxed()
            .map(solNb -> getSolution(solNb))
            .collect(Collectors.toList());
  }

  private MitmSolution getSolution(final int solutionNumber) {
    try {
      model.set(GRB.IntParam.SolutionNumber, solutionNumber);
      int[][] CondValue     = new int[32][2];
      int[][][][] DAValue     = new int[Rounds+1][32][5][3];
      int[][][][] DBValue  = new int[Rounds][32][5][3];
      int[][][] DCValue = new int[Rounds][32][5];
      int[] domValue  = new int[32];
      int[] objValue  = new int[3];

      for (int round = 0; round < Rounds; round++)
        for (int i = 0; i < 32; i++)
          for (int j = 0; j < 5; j++) {
            DCValue[round][i][j]  = (int) Math.round(DC[round][i][j].get(GRB.DoubleAttr.Xn));
            for (int l = 0; l < 3; l++)  {
              DAValue[round][i][j][l]  = (int) Math.round(DA[round][i][j][l].get(GRB.DoubleAttr.Xn));
              DBValue[round][i][j][l]  = (int) Math.round(DB[round][i][j][l].get(GRB.DoubleAttr.Xn));
            }
          }
      for (int i = 0; i < 32; i++)
        for (int j = 0; j < 5; j++)
          for (int l = 0; l < 3; l++)  {
            DAValue[Rounds][i][j][l]  = (int) Math.round(DA[Rounds][i][j][l].get(GRB.DoubleAttr.Xn));
          }

      for (int j = 0; j < 32; j++)
        for (int k = 0; k < 2; k++)  {
          CondValue[j][k]  = (int) Math.round(Cond[j][k].get(GRB.DoubleAttr.Xn));
        }

      for (int i = 0; i < 32; i++)
        domValue[i]  = (int) Math.round(dom[i].get(GRB.DoubleAttr.Xn));

      for (int i = 0; i < 3; i++)
        objValue[i]  = (int) Math.round(obj[i].get(GRB.DoubleAttr.Xn));

      return new MitmSolution(Rounds, (int) Math.round(model.get(GRB.DoubleAttr.PoolObjVal)), CondValue, DAValue, DBValue, DCValue, domValue, objValue);
    } catch (GRBException e) {
      System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
      return null; // Can't access
    }
  }
  private void ArticleTrail() throws GRBException {
    //int[] blue = {7,17,39,49};
    int[] blue = {3,5,25,28,35,47};
    int[] gray = {14,30,31,46,62,63};
    int flag;
    for (int k = 0; k < 32; k++) {
      flag = 0;
      for (int q = 0; q < 6; q ++) {
        if (k == blue[q]) {
          model.addConstr(DA[0][k][0][0], GRB.EQUAL, 1, "");
          model.addConstr(DA[0][k][0][1], GRB.EQUAL, 1, "");
          model.addConstr(DA[0][k][0][2], GRB.EQUAL, 0, "");
          flag = 1;
        }
      }
      /*for (int q = 0; q < 6; q ++) {
        if (k == gray[q]) {
	  model.addConstr(DA[0][k][0][0], GRB.EQUAL, 1, "");
	  model.addConstr(DA[0][k][0][1], GRB.EQUAL, 1, "");
	  model.addConstr(DA[0][k][0][2], GRB.EQUAL, 1, "");
          flag = 1;
	}
      }
      if (flag == 0) {
	  model.addConstr(DA[0][k][0][0], GRB.EQUAL, 0, "");
	  model.addConstr(DA[0][k][0][1], GRB.EQUAL, 1, "");
	  model.addConstr(DA[0][k][0][2], GRB.EQUAL, 1, "");
      }*/
    }



  }

}
