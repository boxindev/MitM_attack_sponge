package mitmsearch.solutiontotikz;

import mitmsearch.mitm.MitmSolution;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.lang.Math;

public class MitmSolutionToTikz {
  private final MitmSolution mitmSolution;
  private static final String WhiteColor  = "white";
  private static final String GrayColor  = "lightgray";
  private static final String BlueColor = "blue";
  private static final String RedColor = "red"; 
  private static final String PurpleColor  = "green!60";
  private static final int[][] rho = new int[][]{{0,36,3,41,18},{1,44,10,45,2},{62,6,43,15,61},{28,55,25,21,56},{27,20,39,8,14}};
  public MitmSolutionToTikz(final String filename, final int solutionNumber) {
    this(MitmSolution.fromFile(filename).get(solutionNumber));
  }

  public MitmSolutionToTikz(final MitmSolution mitmSolution) {
    this.mitmSolution = mitmSolution;
  }


  public String generate() {
    String output = "";
    // Header
    output += "\\documentclass{standalone}\n";
    output += "\\usepackage{tikz}\n";
    output += "\\usepackage{calc}\n";
    output += "\\usepackage{pgffor}\n";
    output += "\\usetikzlibrary{patterns}\n";
    output += "\\tikzset{base/.style = {draw=black, minimum width=0.02cm, minimum height=0.02cm, align=center, on chain},}\n";
    output += "\\begin{document}\n";
    output += "\\begin{tikzpicture}[scale = 0.45,every node/.style={scale=0.5}]\n";
    output += "\\makeatletter\n";

    for (int round = 0; round < mitmSolution.Rounds+1; round++) {

      output += " \\node[align=center] at ("+(-2)+","+(12*(mitmSolution.Rounds-round)+8.5)+") [scale=1.5]{\\textbf{\\huge $A^{("+(round)+")}$}};\n";
      int countred = 0;
      int countblue= 0;
        for (int k = 0; k < 32; k++)
        {       
          for (int i = 0; i < 5; i++)
            {
              if (mitmSolution.DA[round][k][i][0] == 0 & mitmSolution.DA[round][k][i][1] == 0 & mitmSolution.DA[round][k][i][2] == 0) {
                output += "\\fill[color=" + WhiteColor + "] (" + (2 * k) + "," + (12 * (mitmSolution.Rounds - round) + (4 - i) + 6) + ") rectangle ++(1,1);\n";
              }
              if (mitmSolution.DA[round][k][i][0] == 0 & mitmSolution.DA[round][k][i][1] == 1 & mitmSolution.DA[round][k][i][2] == 1) {
                if (round == 0){
                  countred ++;
                }
                output += "\\fill[color=" + RedColor + "] (" + (2 * k) + "," + (12 * (mitmSolution.Rounds - round) + (4 - i) + 6) + ") rectangle ++(1,1);\n";
              }
              if (mitmSolution.DA[round][k][i][0] == 1 & mitmSolution.DA[round][k][i][1] == 1 & mitmSolution.DA[round][k][i][2] == 0) {
                if (round == 0){
                  countblue ++;
                }
                output += "\\fill[color=" + BlueColor + "] (" + (2 * k) + "," + (12 * (mitmSolution.Rounds - round) + (4 - i) + 6) + ") rectangle ++(1,1);\n";
              }
              if (mitmSolution.DA[round][k][i][0] == 0 & mitmSolution.DA[round][k][i][1] == 1 & mitmSolution.DA[round][k][i][2] == 0) {
                output += "\\fill[color=" + PurpleColor + "] (" + (2 * k) + "," + (12 * (mitmSolution.Rounds - round) + (4 - i) + 6) + ") rectangle ++(1,1);\n";
              }
              if (mitmSolution.DA[round][k][i][0] == 1 & mitmSolution.DA[round][k][i][1] == 1 & mitmSolution.DA[round][k][i][2] == 1) {
                output += "\\fill[color=" + GrayColor + "] (" + (2 * k) + "," + (12 * (mitmSolution.Rounds - round) + (4 - i) + 6) + ") rectangle ++(1,1);\n";
              }
            }
          output += "\\draw("+(2*k)+","+(12*(mitmSolution.Rounds-round)+6)+") grid ++(1,5);\n";
        }

      if (round == 0){
        output += "\\fill[color="+RedColor+"] ("+(2*31+4)+","+(12 * (mitmSolution.Rounds - round) + (4 - 1) + 6)+")  rectangle ++(1,1);\n";
        output += "\\draw("+(2*31+4)+","+(12 * (mitmSolution.Rounds - round) + (4 - 1) + 6)+") grid ++(1,1);\n";
        output += " \\node[align=center] at ("+(2*31+2.5)+","+(12 * (mitmSolution.Rounds - round) + (4 - 1) + 6.4)+") {\\textbf{\\huge + "+(countred)+"}};\n";

        output += "\\fill[color="+BlueColor+"] ("+(2*31+4)+","+(12 * (mitmSolution.Rounds - round) + (4 - 3) + 6)+")  rectangle ++(1,1);\n";
        output += "\\draw("+(2*31+4)+","+(12 * (mitmSolution.Rounds - round) + (4 - 3) + 6)+") grid ++(1,1);\n";
        output += " \\node[align=center] at ("+(2*31+2.5)+","+(12 * (mitmSolution.Rounds - round) + (4 - 3) + 6.4)+") {\\textbf{\\huge + "+(countblue)+"}};\n";
      }

       if (round > 0) {
         int consumered = 0;
         int consumeblue= 0;
         for (int k = 0; k < 32; k++)
           for (int i = 0; i < 5; i++)
            {
              if (mitmSolution.DC[round-1][k][i][0] == 1 & mitmSolution.DC[round-1][k][i][1] == 1) {
                consumered++;
                consumeblue++;
                output += "\\draw[line width=2pt, color=yellow] (" + (2 * k) + "," + (12 * (mitmSolution.Rounds - round) + (4 - i) + 6) + ") rectangle ++(1,1);\n";
              }
              if (mitmSolution.DC[round-1][k][i][0] == 1 & mitmSolution.DC[round-1][k][i][1] == 0){
                consumered++;
                output += "\\draw[line width=2pt, color=red] ("+(2*k)+","+(12*(mitmSolution.Rounds-round)+(4-i)+6)+") rectangle ++(1,1);\n";
              }

              if (mitmSolution.DC[round-1][k][i][0] == 0 & mitmSolution.DC[round-1][k][i][1] == 1) {
                consumeblue ++;
                output += "\\draw[line width=2pt, color=blue] (" + (2 * k) + "," + (12 * (mitmSolution.Rounds - round) + (4 - i) + 6) + ") rectangle ++(1,1);\n";
              }
            }
         output += "\\fill[color="+RedColor+"] ("+(2*31+4)+","+(12 * (mitmSolution.Rounds - round) + (4 - 1) + 6)+")  rectangle ++(1,1);\n";
         output += "\\draw("+(2*31+4)+","+(12 * (mitmSolution.Rounds - round) + (4 - 1) + 6)+") grid ++(1,1);\n";
         output += " \\node[align=center] at ("+(2*31+2.5)+","+(12 * (mitmSolution.Rounds - round) + (4 - 1) + 6.4)+") {\\textbf{\\huge - "+(consumered)+"}};\n";

         output += "\\fill[color="+BlueColor+"] ("+(2*31+4)+","+(12 * (mitmSolution.Rounds - round) + (4 - 3) + 6)+")  rectangle ++(1,1);\n";
         output += "\\draw("+(2*31+4)+","+(12 * (mitmSolution.Rounds - round) + (4 - 3) + 6)+") grid ++(1,1);\n";
         output += " \\node[align=center] at ("+(2*31+2.5)+","+(12 * (mitmSolution.Rounds - round) + (4 - 3) + 6.4)+") {\\textbf{\\huge - "+(consumeblue)+"}};\n";
       }

       if ( round != mitmSolution.Rounds) {
         output += " \\node[align=center] at ("+(-2)+","+(12*(mitmSolution.Rounds-round)+2.5)+") [scale=1.5]{\\textbf{\\huge $S^{("+(round)+")}$}};\n";

         for (int k = 0; k < 32; k++)
         {       
            for (int i = 0; i < 5; i++)
            {
              if (mitmSolution.DB[round][k][i][0] == 0 & mitmSolution.DB[round][k][i][1] == 0 & mitmSolution.DB[round][k][i][2] == 0)
                output += "\\fill[color="+WhiteColor+"] ("+(2*k)+","+(12*(mitmSolution.Rounds-round)+(4-i))+") rectangle ++(1,1);\n";
              if (mitmSolution.DB[round][k][i][0] == 0 & mitmSolution.DB[round][k][i][1] == 1 & mitmSolution.DB[round][k][i][2] == 1)
                output += "\\fill[color="+RedColor+"] ("+(2*k)+","+(12*(mitmSolution.Rounds-round)+(4-i))+") rectangle ++(1,1);\n";
              if (mitmSolution.DB[round][k][i][0] == 1 & mitmSolution.DB[round][k][i][1] == 1 & mitmSolution.DB[round][k][i][2] == 0)
                output += "\\fill[color="+BlueColor+"] ("+(2*k)+","+(12*(mitmSolution.Rounds-round)+(4-i))+") rectangle ++(1,1);\n";
              if (mitmSolution.DB[round][k][i][0] == 0 & mitmSolution.DB[round][k][i][1] == 1 & mitmSolution.DB[round][k][i][2] == 0)
                output += "\\fill[color="+PurpleColor+"] ("+(2*k)+","+(12*(mitmSolution.Rounds-round)+(4-i))+") rectangle ++(1,1);\n";
              if (mitmSolution.DB[round][k][i][0] == 1 & mitmSolution.DB[round][k][i][1] == 1 & mitmSolution.DB[round][k][i][2] == 1)
                output += "\\fill[color="+GrayColor+"] ("+(2*k)+","+(12*(mitmSolution.Rounds-round)+(4-i))+") rectangle ++(1,1);\n";
            }
          output += "\\draw("+(2*k)+","+(12*(mitmSolution.Rounds-round))+") grid ++(1,5);\n";
        }
        
          
      }
   
       
    }
      
      for (int k = 0; k < 32; k++){
	    if(mitmSolution.dom[k]==1){
		  output += " \\node[align=center] at ("+(2*k+0.5)+","+(5)+") [scale=1.5]{\\textbf{\\huge $M$}};\n";
        }
      }
      
  
    



    // Footer
    output += "\\makeatother\n";
    output += "\\end{tikzpicture}\n";
    output += "\\end{document}\n";
    return output;
  }
}
