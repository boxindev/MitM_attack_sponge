package mitmsearch.solutiontotikz;

import mitmsearch.mitm.MitmSolution;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MitmSolutionToTikzold {
  private final MitmSolution mitmSolution;
  private static final String WhiteColor  = "white";
  private static final String GrayColor  = "lightgray";
  private static final String BlueColor = "blue";
  private static final String RedColor = "red";

  public MitmSolutionToTikzold(final String filename, final int solutionNumber) {
    this(MitmSolution.fromFile(filename).get(solutionNumber));
  }

  public MitmSolutionToTikzold(final MitmSolution mitmSolution) {
    this.mitmSolution = mitmSolution;
  }

  private boolean hasOnlyOnes(int[][] mat) {
    boolean onlyOnes = true;
    for (int i = 0; i < 4; i++)
      for (int j = 0; j < 4; j++)
        onlyOnes = onlyOnes && mat[i][j] == 1;
    return onlyOnes;
  }

  public String generate() {
    String output = "";
    // Header
    output += "\\documentclass{standalone}\n";
    output += "\\usepackage{tikz}\n";
    output += "\\usepackage{calc}\n";
    output += "\\usepackage{pgffor}\n";
    output += "\\tikzset{base/.style = {draw=black, minimum width=0.02cm, minimum height=0.02cm, align=center, on chain},}\n";
    output += "\\begin{document}\n";
    output += "\\begin{tikzpicture}[scale = 0.45,every node/.style={scale=0.5}]\n";
    output += "\\makeatletter\n";

    for (int round = 0; round < mitmSolution.Rounds; round++) {
      // DA
      output += " \\node[align=center] at ("+(-2)+","+(10*(mitmSolution.Rounds-round)+6.5)+") {\\textbf{\\Large R"+round+"}};\n";
      for (int k = 0; k < 32; k++) 
      {
        
        for (int i = 0; i < 4; i++)
          for (int j = 0; j < 3; j++) 
          {
            if (mitmSolution.DA[round][i][j][k][0] == 0 & mitmSolution.DA[round][i][j][k][1] == 0)
              output += "\\fill[color="+WhiteColor+"] ("+(5*k+i)+","+(10*(mitmSolution.Rounds-round)+j+5)+") rectangle ++(1,1);\n";
            if (mitmSolution.DA[round][i][j][k][0] == 0 & mitmSolution.DA[round][i][j][k][1] == 1)
              output += "\\fill[color="+RedColor+"] ("+(5*k+i)+","+(10*(mitmSolution.Rounds-round)+j+5)+") rectangle ++(1,1);\n";
            if (mitmSolution.DA[round][i][j][k][0] == 1 & mitmSolution.DA[round][i][j][k][1] == 0)
              output += "\\fill[color="+BlueColor+"] ("+(5*k+i)+","+(10*(mitmSolution.Rounds-round)+j+5)+") rectangle ++(1,1);\n";
            if (mitmSolution.DA[round][i][j][k][0] == 1 & mitmSolution.DA[round][i][j][k][1] == 1)
              output += "\\fill[color="+GrayColor+"] ("+(5*k+i)+","+(10*(mitmSolution.Rounds-round)+j+5)+") rectangle ++(1,1);\n";
          }
        output += "\\draw("+(5*k)+","+(10*(mitmSolution.Rounds-round)+5)+") grid ++(4,3);\n";
        output += " \\node[align=center] at ("+(5*k+2)+","+(10*(mitmSolution.Rounds-round)+4)+") {\\textbf{\\Large z="+k+"}};\n";
      }
      // DB
      for (int k = 0; k < 32; k++) 
      {
        
        for (int i = 0; i < 4; i++)
          for (int j = 0; j < 3; j++) 
          {
            if (mitmSolution.DB[round][i][j][k][0] == 0 & mitmSolution.DB[round][i][j][k][1] == 0)
              output += "\\fill[color="+WhiteColor+"] ("+(5*k+i)+","+(10*(mitmSolution.Rounds-round)+j)+") rectangle ++(1,1);\n";
            if (mitmSolution.DB[round][i][j][k][0] == 0 & mitmSolution.DB[round][i][j][k][1] == 1)
              output += "\\fill[color="+RedColor+"] ("+(5*k+i)+","+(10*(mitmSolution.Rounds-round)+j)+") rectangle ++(1,1);\n";
            if (mitmSolution.DB[round][i][j][k][0] == 1 & mitmSolution.DB[round][i][j][k][1] == 0)
              output += "\\fill[color="+BlueColor+"] ("+(5*k+i)+","+(10*(mitmSolution.Rounds-round)+j)+") rectangle ++(1,1);\n";
            if (mitmSolution.DB[round][i][j][k][0] == 1 & mitmSolution.DB[round][i][j][k][1] == 1)
              output += "\\fill[color="+GrayColor+"] ("+(5*k+i)+","+(10*(mitmSolution.Rounds-round)+j)+") rectangle ++(1,1);\n";
          }
        output += "\\draw("+(5*k)+","+(10*(mitmSolution.Rounds-round))+") grid ++(4,3);\n";
        output += " \\node[align=center] at ("+(5*k+2)+","+(10*(mitmSolution.Rounds-round)-1)+") {\\textbf{\\Large z="+k+"}};\n";
      }
      output += "\n";
    }
    
    
    /*output += "\n\n\n%grid\n";
    output += "\\@for\\x:={";
    output += String.join(",",IntStream.range(0, mitmSolution.Rounds).boxed().map(i->String.valueOf(i)).collect(Collectors.toList()));
    output += "}\\do{\n";
    output += " \\@for\\y:={3,8}\\do{\n";
    output += "  \\@for\\i:={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31}\\do{\n";
    output += "   \\draw (5*\\x+\\i, \\y)   -- ++(0,4);\n";
    output += "   \\draw (5*\\x, \\y+\\i)   -- ++(4,0);\n";
    output += "   \\draw (5*\\x+4+\\i, \\y) -- ++(0,4);\n";
    output += "   \\draw (5*\\x+4, \\y+\\i) -- ++(4,0);}\n";
    output += "  \\node[align=center] at (10*\\x+4.5, \\y+2.5) {SB};\n";
    output += "  \\draw[->] (10*\\x+4, \\y+2) -- ++(1,0);}\n";
    output += " \\node[align=center] at (10*\\x+3, 13) {\\textbf{\\Large R\\x}};\n";
    output += " \\node[align=center] at (10*\\x+3, 2)  {\\textbf{\\Large R\\x}};}\n"*/


    // Footer
    output += "\\makeatother\n";
    output += "\\end{tikzpicture}\n";
    output += "\\end{document}\n";
    return output;
  }
}
