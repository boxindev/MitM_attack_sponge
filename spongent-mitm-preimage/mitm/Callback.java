package mitmsearch.mitm;

import gurobi.*;
import java.io.FileWriter ;
import java.io.IOException ;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
public class Callback extends GRBCallback {
  private double lastiter ;
  private double lastnode ;
  private GRBVar [] vars ;
  private int Rounds;
  private int Block;
  private FileWriter logfile ;
  public Callback ( GRBVar[] xvars, FileWriter xlogfile, int Rounds,int Block) {
    lastiter = lastnode = - GRB . INFINITY ;
    vars = xvars ;
    logfile = xlogfile ;
    this.Rounds = Rounds;
    this.Block = Block;
  }
  /**
   * 
   */
  protected void callback () {
    try {
      if ( where == GRB.CB_POLLING ) {
        // Ignore polling callback
      } else if ( where == GRB.CB_PRESOLVE ) {
        // Presolve callback
        /*int cdels = getIntInfo (GRB.CB_PRE_COLDEL);
        int rdels = getIntInfo (GRB.CB_PRE_ROWDEL );
        if ( cdels != 0 || rdels != 0) {
          System.out.println ( cdels + " columns and " + rdels + " rows are removed " );
        }*/
      } else if ( where == GRB.CB_SIMPLEX ) {
        // Simplex callback
        /*double itcnt = getDoubleInfo ( GRB . CB_SPX_ITRCNT );
        if ( itcnt - lastiter >= 100) {
          lastiter = itcnt ;
          double obj = getDoubleInfo ( GRB . CB_SPX_OBJVAL );
          int ispert = getIntInfo ( GRB . CB_SPX_ISPERT );
          double pinf = getDoubleInfo ( GRB . CB_SPX_PRIMINF );
          double dinf = getDoubleInfo ( GRB . CB_SPX_DUALINF );
          char ch ;
          if ( ispert == 0) ch = ' ';
          else if ( ispert == 1) ch = 'S';
          else ch = 'P';
          System . out . println ( itcnt + " " + obj + ch + " " + pinf + " " + dinf );
        }*/
      } else if ( where == GRB.CB_MIP ) {
        // General MIP callback
        /*double nodecnt = getDoubleInfo ( GRB.CB_MIP_NODCNT );
        double objbst = getDoubleInfo ( GRB.CB_MIP_OBJBST );
        double objbnd = getDoubleInfo ( GRB.CB_MIP_OBJBND );
        int solcnt = getIntInfo ( GRB.CB_MIP_SOLCNT );
        if ( nodecnt - lastnode >= 100) {
          lastnode = nodecnt ;
          int actnodes = ( int ) getDoubleInfo ( GRB . CB_MIP_NODLFT );
          int itcnt = ( int ) getDoubleInfo ( GRB . CB_MIP_ITRCNT );
          int cutcnt = getIntInfo ( GRB . CB_MIP_CUTCNT );
          System . out . println ( nodecnt + " " + actnodes + " " + itcnt + " " + objbst + " " + objbnd + " " + solcnt + " " + cutcnt );
        }
        if ( Math . abs ( objbst - objbnd ) < 0.1 * (1.0 + Math . abs ( objbst ))) {
          System . out . println ( " Stop early - 10% gap achieved " );
          abort ();
        }
        if ( nodecnt >= 10000 && solcnt > 0) {
          System . out . println ( " Stop early - 10000 nodes explored " );
          abort ();
        }*/
      } else if ( where == GRB . CB_MIPSOL ) {
        // MIP solution callback
        int nodecnt = ( int ) getDoubleInfo ( GRB.CB_MIPSOL_NODCNT );
        double obj = getDoubleInfo ( GRB.CB_MIPSOL_OBJ );
        int solcnt = getIntInfo ( GRB.CB_MIPSOL_SOLCNT );
        double[] x = getSolution ( vars );
        System.out.println ( " **** New solution at node " + nodecnt + " , obj " + obj + " , sol " + solcnt + " **** " );
        String output = "output/resultimm"+solcnt+".json";
        MitmSolution Solution;
        Solution = getSolution(solcnt, obj, x, Rounds,Block);
	List<MitmSolution> ListSolution = new ArrayList<>();
	ListSolution.add(Solution);
        MitmSolution.toFile(output,ListSolution);
        
        
      } else if ( where == GRB . CB_MIPNODE ) {
        // MIP node callback
        /*System . out . println ( " **** New node **** " );
        if ( getIntInfo ( GRB . CB_MIPNODE_STATUS ) == GRB . OPTIMAL ) {
          double [] x = getNodeRel ( vars );
          setSolution ( vars , x );
        }*/
      } else if ( where == GRB . CB_BARRIER ) {
          // Barrier callback
          /*int itcnt = getIntInfo ( GRB . CB_BARRIER_ITRCNT );
          double primobj = getDoubleInfo ( GRB . CB_BARRIER_PRIMOBJ );
          double dualobj = getDoubleInfo ( GRB . CB_BARRIER_DUALOBJ );
          double priminf = getDoubleInfo ( GRB . CB_BARRIER_PRIMINF );
          double dualinf = getDoubleInfo ( GRB . CB_BARRIER_DUALINF );
          double cmpl = getDoubleInfo ( GRB . CB_BARRIER_COMPL );
          System . out . println ( itcnt + " " + primobj + " " + dualobj + " "+ priminf + " " + dualinf + " " + cmpl );*/
      } else if ( where == GRB.CB_MESSAGE ) {
        // Message callback
        //String msg = getStringInfo(GRB.CB_MSG_STRING );
        //if (msg != null) logfile.write (msg);
      }
    } catch ( GRBException e ) {
      System.out . println ( " Error code : " + e . getErrorCode ());
      System.out . println ( e . getMessage ());
      e.printStackTrace ();
    } catch ( Exception e ) {
    System.out.println ( " Error during callback " );
      e.printStackTrace ();
    }
  }

  private MitmSolution getSolution(int solutionNumber, double obj, double[] x, int Rounds,int Block) {
    //try {
      //model.set(GRB.IntParam.SolutionNumber, solutionNumber);
      //int Rounds = 8;
      int[][][] AfValue     = new int[Rounds+1][Block][2];
      int[][][] SBfValue  = new int[Rounds][Block][2];
      int[][][]DCfValue  = new int[Rounds][Block][2];
      int[][][] AbValue     = new int[Rounds+1][Block][2];
      int[][][] SBbValue  = new int[Rounds][Block][2];
      int[][][]DCbValue  = new int[Rounds][Block][2];
      int[] DomfValue  = new int[Block];
      int[] DombValue  = new int[Block];
      int[] objValue  = new int[5];

      
     

      for (int round = 0; round < Rounds+1; round++)
        for (int i = 0; i < Block; i++)
              for (int k = 0; k < 2; k++)  { 
                AfValue[round][i][k]  = (int) Math.round(x[2*(Block*round+i)+k]);
            }
      

      for (int round = 0; round < Rounds; round++)
        for (int i = 0; i < Block; i++)
              for (int k = 0; k < 2; k++)  { 
                SBfValue[round][i][k]  = (int) Math.round(x[Block*2*(Rounds+1)+2*(Block*round+i)+k]);
            }
      

      for (int round = 0; round < Rounds; round++)
        for (int i = 0; i < Block; i++)
            for (int k = 0; k < 2; k++)  {
                  DCfValue[round][i][k]  = (int) Math.round(x[Block*2*(Rounds+1)+Block*2*Rounds+2*(Block*round+i)+k]);
          }

      int forward_count = Block*2*(Rounds+1)+Block*2*Rounds+Block*2*Rounds;

      for (int round = 0; round < Rounds+1; round++)
          for (int i = 0; i < Block; i++)
              for (int k = 0; k < 2; k++)  { 
                AbValue[round][i][k]  = (int) Math.round(x[forward_count+2*(Block*round+i)+k]);
            }
      

      for (int round = 0; round < Rounds; round++)
          for (int i = 0; i < Block; i++)
              for (int k = 0; k < 2; k++)  { 
                SBbValue[round][i][k]  = (int) Math.round(x[forward_count+Block*2*(Rounds+1)+2*(Block*round+i)+k]);
            }
      

      for (int round = 0; round < Rounds; round++)
          for (int i = 0; i < Block; i++)
              for (int k = 0; k < 2; k++)  {
                DCbValue[round][i][k]  = (int) Math.round(x[forward_count+Block*2*(Rounds+1)+Block*2*Rounds+2*(Block*round+i)+k]);
      }




      for (int i = 0; i < Block; i++) {
          DomfValue[i]  = (int) Math.round(x[forward_count*2    +i]);
      }
      for (int i = 0; i < Block; i++) {
          DombValue[i]  = (int) Math.round(x[forward_count*2 + Block    +i]);
      }
      for (int i = 0; i < 3; i++) {
        objValue[i]  = (int) Math.round(x[x.length-1-(2-i)]);
      }
      
      return new MitmSolution(Rounds, (int) obj, AfValue, SBfValue, DCfValue,AbValue, SBbValue, DCbValue,DomfValue,DombValue, objValue);
    /*} catch (GRBException e) {
      System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
      return null; // Can't access
    }*/
  }
}
