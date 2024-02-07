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
  private final GRBVar[][][] Af;
  private final GRBVar[][][] SBf;
  private final GRBVar[][][] DCf;
  private final GRBVar[][][] Ab;
  private final GRBVar[][][] SBb;
  private final GRBVar[][][] DCb;
  private final GRBLinExpr   objective;
  private final GRBVar[] obj;
  private final GRBVar[] dom_f;
  private final GRBVar[] dom_b;
  private final int Block;
  private final int Rate;
  private final int Capacity;


 /**
   * @param env the Gurobi environment
   */
  public Mitm(final GRBEnv env, final int Rounds) throws GRBException {
    model = new GRBModel(env);
    this.Rounds = Rounds;

    Block = 88;
    Rate = 8;
    Capacity = 80;


    factory = new MitmFactory(model);
    Af = new GRBVar[Rounds+1][Block][2];
    SBf = new GRBVar[Rounds][Block][2];
    DCf = new GRBVar[Rounds][Block][2];

    Ab = new GRBVar[Rounds+1][Block][2];
    SBb = new GRBVar[Rounds][Block][2];
    DCb = new GRBVar[Rounds][Block][2];

    // Initialization
    for (int round = 0; round < Rounds+1; round++)
      for (int i = 0; i < Block; i++)
            for (int l = 0; l < 2; l++) {
              Af[round][i][l] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "Af_"+round+"_"+i+"_"+l);
            }
    for (int round = 0; round < Rounds; round++)
        for (int i = 0; i < Block; i++)
              for (int l = 0; l < 2; l++) {
                SBf[round][i][l] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "SBf_"+round+"_"+i+"_"+l);
            }
    for (int round = 0; round < Rounds; round++)
        for (int i = 0; i < Block; i++)
          for (int l = 0; l < 2; l++){
              DCf[round][i][l] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "DCf_"+round+"_"+i+"_"+l);
            }

    for (int round = 0; round < Rounds+1; round++)
        for (int i = 0; i < Block; i++)
            for (int l = 0; l < 2; l++) {
              Ab[round][i][l] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "Ab_"+round+"_"+i+"_"+l);
            }
    for (int round = 0; round < Rounds; round++)
        for (int i = 0; i < Block; i++)
            for (int l = 0; l < 2; l++) {
                SBb[round][i][l] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "SBb_"+round+"_"+i+"_"+l);
            }
    for (int round = 0; round < Rounds; round++)
        for (int i = 0; i < Block; i++)
            for (int l = 0; l < 2; l++) {
              DCb[round][i][l] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "DCb_"+round+"_"+i+"_"+l);
            }


    dom_f = new GRBVar[Block];
    for (int i = 0; i < Block; i++) {
        dom_f[i]   = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "dom_f_"+i);
    }

      dom_b = new GRBVar[Block];
      for (int i = 0; i < Block; i++) {
          dom_b[i]   = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "dom_b_"+i);
      }

    //fixed input
    factory.addfixed_in(Af,Ab,Block,Rate);

    // Constraints
    //factory.addSbox(Af,SBf,DCf,Block);
    factory.addSbox_inv(Ab,SBb,DCb,Block);
    //factory.addPermutation(Af,SBf,Block);
    factory.addPermutation_inv(Ab,SBb,Block);
    //factory.add_FB_Match(Af,Ab,dom_f,dom_b, Rate,Block);
    factory.add_FB_Match_new_88(Af,SBb,dom_f,dom_b, Rate,Block);

    objective = new GRBLinExpr();
    GRBLinExpr DoF_red = new GRBLinExpr();
    GRBLinExpr DoF_blue = new GRBLinExpr();
    GRBLinExpr DoF_consume_blue = new GRBLinExpr();
    GRBLinExpr DoM = new GRBLinExpr();

    GRBVar Obj = model.addVar(0.0, 1600.0, 0.0, GRB.INTEGER, "Obj");

    obj = new GRBVar[3];
    obj[0] = model.addVar(0.0, 1600.0, 0.0, GRB.INTEGER, "Obj0"); //DoF of red forward
    obj[1] = model.addVar(0.0, 1600.0, 0.0, GRB.INTEGER, "Obj1"); //DoF of blue forward
    obj[2] = model.addVar(0.0, 1600.0, 0.0, GRB.INTEGER, "Obj4"); //DoM

    for (int k = Rate; k < Block; k++) {
      DoF_blue.addTerm(-1.0, Af[0][k][1]);
      DoF_red.addTerm(-1.0, Af[0][k][0]);
      DoF_blue.addConstant(1.0);
      DoF_red.addConstant(1.0);

    }


    for (int round = 0; round < Rounds; round ++)
      for (int i = 0; i < Block; i++) {
          DoF_red.addTerm(-1.0, DCf[round][i][0]);
          DoF_blue.addTerm(-1.0, DCf[round][i][1]);

          DoF_red.addTerm(-1.0, DCb[round][i][0]);
          DoF_blue.addTerm(-1.0, DCb[round][i][1]);

          DoF_consume_blue.addTerm(1.0, DCb[round][i][1]);
        }




    for (int i = 0; i < Block; i++) {
        //DoM.addTerm(1.0, dom_f[i]);
        DoM.addTerm(1.0, dom_b[i]);
      }





    objective.addTerm(1.0, Obj);

    model.addConstr(DoF_red, GRB.GREATER_EQUAL, 1, "");
    model.addConstr(DoF_blue, GRB.GREATER_EQUAL, 1, "");
    //model.addConstr(DoM, GRB.GREATER_EQUAL, 1, "");
    model.addConstr(DoF_consume_blue, GRB.EQUAL, 0, "");

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
    //if (minObjValue != -1)
    //  model.addConstr(objective, GRB.EQUAL, minObjValue, "objectiveFix");
    //model.set(GRB.IntParam.DualReductions, 0);
     try {
      FileWriter logfile = null;
      logfile = new FileWriter("cb.log");
    }
    catch (IOException e) { e.printStackTrace(); System.exit(1); }
    
    GRBVar[] vars = model.getVars();
    System.out.println (vars.length); 
    System.out.println (vars[0].get(GRB.StringAttr.VarName)); 
    Callback cb = new Callback(vars,logfile,Rounds,Block);
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

      int[][][] AfValue     = new int[Rounds+1][Block][2];
      int[][][] SBfValue     = new int[Rounds][Block][2];
      int[][][] DCfValue     = new int[Rounds][Block][2];
      int[][][] AbValue     = new int[Rounds+1][Block][2];
      int[][][] SBbValue     = new int[Rounds][Block][2];
      int[][][] DCbValue     = new int[Rounds][Block][2];
      int[] domfValue  = new int[Block];
      int[] dombValue  = new int[Block];
      int[] objValue  = new int[3];

      for (int round = 0; round < Rounds+1; round++)
        //for (int b = 0; b < 4; b++)
          for (int i = 0; i < Block; i++)
	        for (int l = 0; l < 2; l++) {
                  AfValue[round][i][l]  = (int) Math.round(Af[round][i][l].get(GRB.DoubleAttr.Xn));
                  AbValue[round][i][l]  = (int) Math.round(Ab[round][i][l].get(GRB.DoubleAttr.Xn));
      } 
      for (int round = 0; round < Rounds; round++)
          for (int i = 0; i < Block; i++)
	        for (int l = 0; l < 2; l++) {
                  SBfValue[round][i][l]  = (int) Math.round(SBf[round][i][l].get(GRB.DoubleAttr.Xn));
                  SBbValue[round][i][l]  = (int) Math.round(SBb[round][i][l].get(GRB.DoubleAttr.Xn));
      }
      for (int round = 0; round < Rounds; round++)
          for (int i = 0; i < Block; i++)
              for (int l = 0; l < 2; l++) {
                    DCfValue[round][i][l]  = (int) Math.round(DCf[round][i][l].get(GRB.DoubleAttr.Xn));
                    DCbValue[round][i][l]  = (int) Math.round(DCb[round][i][l].get(GRB.DoubleAttr.Xn));
          }

      for (int i = 0; i < Block; i++)
          domfValue[i]  = (int) Math.round(dom_f[i].get(GRB.DoubleAttr.Xn));
      for (int i = 0; i < Block; i++)
          dombValue[i]  = (int) Math.round(dom_b[i].get(GRB.DoubleAttr.Xn));

      for (int i = 0; i < 3; i++)
        objValue[i]  = (int) Math.round(obj[i].get(GRB.DoubleAttr.Xn));
	   
      return new MitmSolution(Rounds, (int) Math.round(model.get(GRB.DoubleAttr.PoolObjVal)), AfValue, SBfValue, DCfValue, AbValue,SBbValue,DCbValue,domfValue, dombValue,objValue);
    } catch (GRBException e) {
      System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
      return null; // Can't access
    }
  }
  
}
