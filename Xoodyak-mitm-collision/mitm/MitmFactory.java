package mitmsearch.mitm;

import gurobi.*;

public class MitmFactory {
  private GRBModel model;
  //private int MatchRound;
  private static final int[][] rho = new int[][]{{0,36,3,41,18},{1,44,10,45,2},{62,6,43,15,61},{28,55,25,21,56},{27,20,39,8,14}};


  public MitmFactory(final GRBModel model) {
    this.model = model;
    //this.MatchRound = MatchRound;
  }

  
  
  public void addthreexor(GRBVar[][][][][] DA, GRBVar[][][][] DP, GRBVar[][][] DC1) throws GRBException {
    //Round 0
    for (int i = 0; i < 4; i ++) 
      for (int j = 0; j < 32; j ++)  {
        model.addConstr(DA[0][i][2][j][0], GRB.EQUAL, DP[0][i][j][0], "");
        model.addConstr(DA[0][i][2][j][1], GRB.EQUAL, DP[0][i][j][1], "");
        model.addConstr(DA[0][i][2][j][2], GRB.EQUAL, DP[0][i][j][2], "");
        model.addConstr(DC1[0][i][j], GRB.EQUAL, 0, "");
      }


    GRBVar[][][][] DA_Allone = new GRBVar[DP.length][4][32][3];
    for (int round = 1; round < DP.length; round ++)
      for (int i = 0; i < 4; i ++) 
	for (int k = 0; k < 32; k ++) {
          for (int l = 0; l < 3; l ++) {
            DA_Allone[round][i][k][l] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "DA_Allone_"+round+"_"+i+"_"+k+"_"+l); 
            Determine_AllOne(DA_Allone[round][i][k][l],DA[round][i][0][k][l],DA[round][i][1][k][l],DA[round][i][2][k][l]);
          }
          double[] t1 = {1.0, 1.0, -1.0};
          model.addConstr(DA_Allone[round][i][k][1], GRB.EQUAL, DP[round][i][k][1], "");
          model.addConstr(DA_Allone[round][i][k][1], GRB.GREATER_EQUAL, DP[round][i][k][0], "");
          model.addConstr(DA_Allone[round][i][k][0], GRB.LESS_EQUAL, DP[round][i][k][0], "");
          model.addConstr(linExprOf(t1, DC1[round][i][k], DA_Allone[round][i][k][0], DP[round][i][k][0]), GRB.EQUAL, 0, "");
          model.addConstr(DA_Allone[round][i][k][2], GRB.EQUAL, DP[round][i][k][2], "");
        }       
  }

  public void addtwoxor(GRBVar[][][][] DP2, GRBVar[][][][] DP, GRBVar[][][] DC12) throws GRBException {
    GRBVar[][][][] DP_Allone = new GRBVar[DP.length][4][32][3];
    GRBVar[][][] DP_Allzero = new GRBVar[DP.length][4][32];
    for (int round = 0; round < DP.length; round ++)
      for (int i = 0; i < 4; i ++) 
	for (int k = 0; k < 32; k ++) {
          for (int l = 0; l < 3; l ++) {
            DP_Allone[round][i][k][l] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "DP_Allone_"+round+"_"+i+"_"+k+"_"+l); 
            Determine_AllOne(DP_Allone[round][i][k][l],DP[round][(i+3)%4][(k+27)%32][l],DP[round][(i+3)%4][(k+18)%32][l]);
          }

          DP_Allzero[round][i][k] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "DP_Allzero_"+round+"_"+i+"_"+k); 
          Determine_Allzero(DP_Allzero[round][i][k],DP[round][(i+3)%4][(k+27)%32][0],DP[round][(i+3)%4][(k+18)%32][0]);

          double[] t1 = {1.0, 1.0, -1.0};
          model.addConstr(DP_Allone[round][i][k][1], GRB.EQUAL, DP2[round][i][k][1], "");
          model.addConstr(DP_Allone[round][i][k][1], GRB.GREATER_EQUAL, DP2[round][i][k][0], "");
          model.addConstr(DP_Allone[round][i][k][0], GRB.LESS_EQUAL, DP2[round][i][k][0], "");
          model.addConstr(linExprOf(t1, DC12[round][i][k], DP_Allone[round][i][k][0], DP2[round][i][k][0]), GRB.EQUAL, 0, "");
          model.addConstr(DP_Allone[round][i][k][2], GRB.EQUAL, DP2[round][i][k][2], "");
        
          model.addConstr(DC12[round][i][k], GRB.LESS_EQUAL, DP_Allzero[round][i][k], "");
        }    
  }
  

  public void addTheta(GRBVar[][][][][] DA, GRBVar[][][][] DP2, GRBVar[][][][][] DB, GRBVar[][][][] DC2) throws GRBException {
    GRBVar[][][][][] DP2_Allone = new GRBVar[DP2.length][4][3][32][3];
    //two red
    GRBVar[][][][] DP2_Allzero = new GRBVar[DP2.length][4][3][32];
    for (int round = 0; round < DP2.length; round ++)
      for (int i = 0; i < 4; i ++) 
        for (int j = 0; j < 3; j ++)
	  for (int k = 0; k < 32; k ++) {
            for (int l = 0; l < 3; l ++) {
              DP2_Allone[round][i][j][k][l] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "DP2_Allone_"+round+"_"+i+"_"+j+"_"+k+"_"+l); 
              Determine_AllOne(DP2_Allone[round][i][j][k][l],DA[round][i][j][k][l], DP2[round][i][k][l]);
            }
          DP2_Allzero[round][i][j][k] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "DP2_Allzero_"+round+"_"+i+"_"+j+"_"+k); 
          Determine_Allzero(DP2_Allzero[round][i][j][k],DA[round][i][j][k][0], DP2[round][i][k][0]);


          double[] t1 = {1.0, 1.0, -1.0};
          if (j==0) {
            model.addConstr(DP2_Allone[round][i][j][k][1], GRB.EQUAL, DB[round][i][j][k][1], "");
            model.addConstr(DP2_Allone[round][i][j][k][1], GRB.GREATER_EQUAL, DB[round][i][j][k][0], "");
            model.addConstr(DP2_Allone[round][i][j][k][0], GRB.LESS_EQUAL, DB[round][i][j][k][0], "");
            model.addConstr(linExprOf(t1, DC2[round][i][j][k], DP2_Allone[round][i][j][k][0], DB[round][i][j][k][0]), GRB.EQUAL, 0, "");
            model.addConstr(DP2_Allone[round][i][j][k][2], GRB.EQUAL, DB[round][i][j][k][2], "");
        
            model.addConstr(DC2[round][i][j][k], GRB.LESS_EQUAL, DP2_Allzero[round][i][j][k], "");
          }
          if (j==1) {
            model.addConstr(DP2_Allone[round][i][j][k][1], GRB.EQUAL, DB[round][(i+3)%4][j][k][1], "");
            model.addConstr(DP2_Allone[round][i][j][k][1], GRB.GREATER_EQUAL, DB[round][(i+3)%4][j][k][0], "");
            model.addConstr(DP2_Allone[round][i][j][k][0], GRB.LESS_EQUAL, DB[round][(i+3)%4][j][k][0], "");
            model.addConstr(linExprOf(t1, DC2[round][i][j][k], DP2_Allone[round][i][j][k][0], DB[round][(i+3)%4][j][k][0]), GRB.EQUAL, 0, "");
            model.addConstr(DP2_Allone[round][i][j][k][2], GRB.EQUAL, DB[round][(i+3)%4][j][k][2], "");
        
            model.addConstr(DC2[round][i][j][k], GRB.LESS_EQUAL, DP2_Allzero[round][i][j][k], "");
          }
	  if (j==2) {
            model.addConstr(DP2_Allone[round][i][j][k][1], GRB.EQUAL, DB[round][i][j][(k+21)%32][1], "");
            model.addConstr(DP2_Allone[round][i][j][k][1], GRB.GREATER_EQUAL, DB[round][i][j][(k+21)%32][0], "");
            model.addConstr(DP2_Allone[round][i][j][k][0], GRB.LESS_EQUAL, DB[round][i][j][(k+21)%32][0], "");
            model.addConstr(linExprOf(t1, DC2[round][i][j][k], DP2_Allone[round][i][j][k][0], DB[round][i][j][(k+21)%32][0]), GRB.EQUAL, 0, "");
            model.addConstr(DP2_Allone[round][i][j][k][2], GRB.EQUAL, DB[round][i][j][(k+21)%32][2], "");
        
            model.addConstr(DC2[round][i][j][k], GRB.LESS_EQUAL, DP2_Allzero[round][i][j][k], "");
          }
        }
     
  }

  public void addnonlinearcond(GRBVar[][][][][] DB, GRBVar[][][][][] DA) throws GRBException {
    double[][] t = {{7, 0, 7, 0, 0, 2, 1, 0, 1, -2, 7, -8, -1, -8, 3, 1, -5, 1},
		    {4, 0, -1, 4, 0, 14, 3, 0, 0, -7, -7, 12, 3, 6, -15, 4, -9, -1},
		    {1, 0, 0, 2, 0, -1, 5, 0, 8, 4, -8, -1, -3, -5, 6, -2, 7, -7},
		    {-3, 0, -6, -3, 0, -6, -4, 0, -8, -2, 6, -2, 3, 6, -2, -3, 5, 8},
		    {-1, 0, 4, 14, 0, 3, 0, 0, 3, 12, -7, -7, -15, 5, 4, -1, -9, 4},
		    {-10, 0, -2, -10, 0, 0, -5, 0, 1, 11, 5, 2, 0, 3, -6, -6, 8, -2},
		    {0, 0, -7, -2, 0, -7, 0, 0, 0, -8, 3, 4, 6, 3, 8, -1, 5, -8},
		    {0, 0, 3, -1, 0, 3, 12, 0, 3, -1, -7, 3, 10, -6, -6, -13, 5, 3},
		    {-12, 0, -3, -6, 0, 0, -5, 0, -1, 0, 1, -3, -1, 4, 3, 8, 6, -1},
		    {1, 0, -2, -4, 0, -6, -1, 0, 0, -4, 4, 7, -1, -1, 0, 6, 2, -3},
		    {3, 0, 5, 1, 0, -1, 0, 0, 0, -2, 5, -5, 1, -2, 0, -1, -5, 2},
		    {0, 0, 1, 0, 0, 1, -1, 0, 2, -1, -5, 4, 1, -5, 2, 6, -5, -3},
		    {-1, 0, -6, 0, 0, -10, -3, 0, -10, 1, 8, -4, -5, 3, 0, -2, 6, 10},
		    {0, 0, 0, -1, 0, 2, -7, 0, -4, -1, 2, 2, 5, 3, -3, -1, -2, 2},
		    {3, 0, 1, -1, 0, 1, -1, 0, 3, -4, 1, 1, 5, -3, -2, -1, 0, 0},
		    {0, 0, 0, 1, 0, -1, 0, 0, -3, 3, 1, -3, 0, 2, 4, -3, -1, 1}};

    double[] c = {0, -1, -1, -11, -2, -12, -9, -1, -13, -7, -1, -10, -13, -7, -1, -3};
    for (int i = 0; i < 4; i ++) 
      for (int j = 0; j < 32; j ++) {
          for (int q = 0; q < 16; q++) {
            model.addConstr(linExprOf(t[q], DB[0][i][0][j][0], DB[0][i][0][j][1], DB[0][i][0][j][2], DB[0][i][1][j][0], DB[0][i][1][j][1], DB[0][i][1][j][2], DB[0][i][2][j][0], DB[0][i][2][j][1], DB[0][i][2][j][2], DA[1][i][0][j][0], DA[1][i][0][j][1], DA[1][i][0][j][2], DA[1][i][1][(j+31)%32][0], DA[1][i][1][(j+31)%32][1], DA[1][i][1][(j+31)%32][2], DA[1][(i+2)%4][2][(j+24)%32][0], DA[1][(i+2)%4][2][(j+24)%32][1], DA[1][(i+2)%4][2][(j+24)%32][2]), GRB.GREATER_EQUAL, c[q], "");
            }
     }
  }

  public void addnonlinear(GRBVar[][][][][] DB, GRBVar[][][][][] DA) throws GRBException {
    double[][] t = {{1, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0},
		    {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, -1},
		    {0, 1, -1, 1, 0, 0, 1, 0, 0, -1, -1, 1},
		    {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, -1},
		    {0, -1, 0, 0, 0, -1, 0, 0, -1, 0, 1, 0},
		    {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0},
		    {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, -1, 0},
		    {0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 1, 0},
		    {0, 0, 0, 1, 0, 0, 0, 0, 1, 0, -1, 0},
		    {0, 1, -1, 0, 1, -1, 0, 1, -1, 0, -1, 1},
		    {0, -1, 0, -1, 0, 0, -1, 0, 0, 0, 1, 0},
		    {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, -1},
		    {-1, 1, 0, -1, 1, 0, -1, 1, 0, 1, -1, 0},
		    {0, -1, 0, 0, -1, 0, -1, 1, -1, 0, 1, 0},
		    {0, -1, 0, -1, 1, -1, 0, -1, 0, 0, 1, 0},
		    {0, 0, 0, 1, 0, 0, 0, 0, 0, -1, 0, 0, 0},
		    {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, -1, 0, 0},
		    {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, -1, 0, 0},
		    {0, 0, 0, 0, 0, 0, 1, 0, 0, -1, 0, 0, 0}};

    double[] c = {0, 0, 0, 0, -2, 0, 0, 0, 0, 0, -2, 0, 0, -2, -2, 0, 0, 0, 0};
    for (int round = 1; round < DB.length-1; round ++)
      for (int i = 0; i < 4; i ++) 
        for (int j = 0; j < 32; j ++) {
            for (int q = 0; q < 19; q++) {
               model.addConstr(linExprOf(t[q], DB[round][i][0][j][0], DB[round][i][0][j][1], DB[round][i][0][j][2], DB[round][i][1][j][0], DB[round][i][1][j][1], DB[round][i][1][j][2], DB[round][i][2][j][0], DB[round][i][2][j][1], DB[round][i][2][j][2], DA[round+1][i][0][j][0], DA[round+1][i][0][j][1], DA[round+1][i][0][j][2]), GRB.GREATER_EQUAL, c[q], "");
               model.addConstr(linExprOf(t[q], DB[round][i][1][j][0], DB[round][i][1][j][1], DB[round][i][1][j][2], DB[round][i][2][j][0], DB[round][i][2][j][1], DB[round][i][2][j][2], DB[round][i][0][j][0], DB[round][i][0][j][1], DB[round][i][0][j][2], DA[round+1][i][1][(j+31)%32][0], DA[round+1][i][1][(j+31)%32][1], DA[round+1][i][1][(j+31)%32][2]), GRB.GREATER_EQUAL, c[q], "");
               model.addConstr(linExprOf(t[q], DB[round][i][2][j][0], DB[round][i][2][j][1], DB[round][i][2][j][2], DB[round][i][0][j][0], DB[round][i][0][j][1], DB[round][i][0][j][2], DB[round][i][1][j][0], DB[round][i][1][j][1], DB[round][i][1][j][2], DA[round+1][(i+2)%4][2][(j+24)%32][0], DA[round+1][(i+2)%4][2][(j+24)%32][1], DA[round+1][(i+2)%4][2][(j+24)%32][2]), GRB.GREATER_EQUAL, c[q], "");
              //model.addConstr(linExprOf(t[q], DB[round][i][0][j][0], DB[round][i][0][j][1], DB[round][i][1][j][0], DB[round][i][1][j][1], DB[round][i][2][j][0], DB[round][i][2][j][1], DA[round+1][i][0][j][0], DA[round+1][i][0][j][1], DA[round+1][i][1][(j+31)%32][0], DA[round+1][i][1][(j+31)%32][1], DA[round+1][(i+2)%4][2][(j+24)%32][0], DA[round+1][(i+2)%4][2][(j+24)%32][1], DC3[round][i][j][0], DC3[round][i][j][1], DC3[round][i][j][2], DC3[round][i][j][3]), GRB.GREATER_EQUAL, c[q], "");
            }
     }
  }

  public void addDoM(GRBVar[][][][][] DB, GRBVar[][] dom) throws GRBException {   
    GRBVar[][][] DB_Allone = new GRBVar[4][32][3];
    for (int i = 0; i < 4; i ++) 
      for (int k = 0; k < 32; k ++) {
        for (int l = 0; l < 3; l ++) 
          DB_Allone[i][k][l] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "DP2_Allone_"+i+"_"+k+"_"+l); 
        Determine_AllOne(DB_Allone[i][k][0],DB[DB.length-1][i][0][k][1], DB[DB.length-1][i][1][k][1], DB[DB.length-1][i][2][k][1]);//Not white
	Determine_AllOne(DB_Allone[i][k][1],DB[DB.length-1][i][0][k][0], DB[DB.length-1][i][1][k][0]);
	Determine_AllOne(DB_Allone[i][k][2],DB[DB.length-1][i][0][k][2], DB[DB.length-1][i][1][k][2]);
      }

    double[][] t = {{1, 1, 1, -2},
		    {-1, 1, -1, 1},
		    {-1, -1, -1, -1},
		    {1, -1, -1, 1},
		    {-1, -1, 1, 1}};

    double[] c = {0, -1, -3, -1, -1};

    for (int i = 0; i < 4; i ++) 
      for (int k = 0; k < 32; k ++) {
        for (int q = 0; q < 5; q++) {
          model.addConstr(linExprOf(t[q], DB_Allone[i][k][0], DB_Allone[i][k][1], DB_Allone[i][k][2], dom[i][k]), GRB.GREATER_EQUAL, c[q], "");
        }
     }    
  }

    public void addDoM_new(GRBVar[][][][][] DB, GRBVar[][] dom) throws GRBException {
        int r = DB.length-1;
        for (int i = 0; i < 4; i ++)
            for (int k = 0; k < 32; k ++) {
                Determine_AllOne(dom[i][k],DB[r][i][0][k][1],DB[r][i][1][k][1],DB[r][i][2][k][1]);
            }
    }

  public void addDoMnew(GRBVar[][][][][] DB, GRBVar[][] dom) throws GRBException {   
    GRBVar[][] DB_Allone = new GRBVar[4][32];  
    GRBVar[][] DB_Notmul = new GRBVar[4][32];
    for (int i = 0; i < 4; i ++) 
      for (int k = 0; k < 32; k ++) {
        DB_Allone[i][k] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "DB_Allone_"+i+"_"+k); 
        DB_Notmul[i][k] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "DB_Notmul_"+i+"_"+k); 
        Determine_AllOne(DB_Allone[i][k],DB[DB.length-1][i][0][k][1], DB[DB.length-1][i][1][k][1], DB[DB.length-1][i][2][k][1]);//Not white
	Determine_Notmul(DB[DB.length-1][i][0][k][0], DB[DB.length-1][i][0][k][2],DB[DB.length-1][i][1][k][0], DB[DB.length-1][i][1][k][2],DB_Notmul[i][k]);
      }

    
    for (int i = 0; i < 4; i ++) 
      for (int k = 0; k < 32; k ++) {
        Determine_AllOne(dom[i][k], DB_Allone[i][k], DB_Notmul[i][k]);
        
     }    
  }
  
  public void Determine_Notmul(GRBVar Var10, GRBVar Var12, GRBVar Var20, GRBVar Var22, GRBVar mainVar) throws GRBException {
        double[][] t = {{0, -1, 0, -1, 1, 1},
			{0, 1, 1, 0, -1, 0},
			{-1, 0, -1, 0, 1, 1},
			{1, 0, 0, 1, -1, 0},
			{-1, -1, 0, 0, 1, 1},
			{0, 0, -1, -1, 1, 1}};
    	double[] c = {-1, 0, -1, 0, -1, -1};
        for (int i = 0; i < 6; i++) {
          model.addConstr(linExprOf(t[i], Var10, Var12, Var20, Var22, mainVar), GRB.GREATER_EQUAL, c[i], "");
        }
  }
  public void betaConstraints(GRBVar[][][][][] DA, GRBVar[][] beta) throws GRBException {
    double[] t = {1, 1, -2}; 
    for (int i = 0; i < 4; i ++) 
      for (int k = 0; k < 32; k ++) {
        model.addConstr(DA[0][i][2][k][0], GRB.GREATER_EQUAL, beta[i][k], "");
        model.addConstr(DA[0][i][2][k][2], GRB.GREATER_EQUAL, beta[i][k], "");     
        model.addConstr(linExprOf(t, DA[0][i][2][k][0], DA[0][i][2][k][2], beta[i][k]), GRB.LESS_EQUAL, 1, "");
    }
  }

  public void Determine_Allzero(GRBVar mainVar, GRBVar ... vars) throws GRBException {
        GRBLinExpr expr = new GRBLinExpr();
	expr.addTerm(1.0, mainVar);
        for (int i = 0; i < vars.length; i++) {
          expr.addTerm(1.0, vars[i]);
	  model.addConstr(linExprOf(mainVar,vars[i]), GRB.LESS_EQUAL, 1, "");
        }
        model.addConstr(expr, GRB.GREATER_EQUAL, 1, "");
  }

  public void Determine_AllOne(GRBVar mainVar, GRBVar ... vars) throws GRBException {
        GRBLinExpr exprm = new GRBLinExpr();
	exprm.addTerm(1.0, mainVar);
	GRBLinExpr exprp = new GRBLinExpr();
	exprp.addTerm(-1.0*vars.length, mainVar);
        for (int i = 0; i < vars.length; i++) {
          exprm.addTerm(-1.0, vars[i]);
	  exprp.addTerm(1.0, vars[i]);
        }
        model.addConstr(exprm, GRB.GREATER_EQUAL, 1-vars.length, "");
	model.addConstr(exprp, GRB.GREATER_EQUAL, 0, "");
  }
  
  public void Determine_ExistOne(GRBVar mainVar, GRBVar ... vars) throws GRBException {
        GRBLinExpr expr1 = new GRBLinExpr();
	expr1.addTerm(1.0, mainVar);
        GRBLinExpr exprm = new GRBLinExpr();
        exprm.addTerm(vars.length, mainVar);
        for (int i = 0; i < vars.length; i++) {
          expr1.addTerm(-1.0, vars[i]);
          exprm.addTerm(-1.0, vars[i]);
        }
        model.addConstr(exprm, GRB.GREATER_EQUAL, 0, "");
	model.addConstr(expr1, GRB.LESS_EQUAL, 0, "");
  }




 

  public GRBLinExpr linExprOf(double[] coeffs, GRBVar ... vars) throws GRBException {
    GRBLinExpr ofVars = new GRBLinExpr();
    ofVars.addTerms(coeffs, vars);
    return ofVars;
  }

  public GRBLinExpr linExprOf(double constant, GRBVar ... vars) {
    GRBLinExpr ofVars = linExprOf(vars);
    ofVars.addConstant(constant);
    return ofVars;
  }

  public GRBLinExpr linExprOf(GRBVar ... vars) {
    GRBLinExpr expr = new GRBLinExpr();
    for (GRBVar var : vars)
      expr.addTerm(1.0, var);
    return expr;
  }

}
