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

    for (int round = 0; round < mitmSolution.Rounds; round++) {
      
      output += " \\node[align=center] at ("+(-2)+","+(23*(mitmSolution.Rounds-round)+19.5)+") [scale=1.5]{\\textbf{\\huge $A^{("+round+")}$}};\n";
        //DA
        for (int k = 0; k < 32; k++) 
        {       
          for (int i = 0; i < 4; i++)
            for (int j = 0; j < 3; j++) 
            {
              if (mitmSolution.DA[round][i][j][k][0] == 0 & mitmSolution.DA[round][i][j][k][1] == 0 & mitmSolution.DA[round][i][j][k][2] == 0)
                output += "\\fill[color="+WhiteColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+j+18)+") rectangle ++(1,1);\n";
              if (mitmSolution.DA[round][i][j][k][0] == 0 & mitmSolution.DA[round][i][j][k][1] == 1 & mitmSolution.DA[round][i][j][k][2] == 1)
                output += "\\fill[color="+RedColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+j+18)+") rectangle ++(1,1);\n";
              if (mitmSolution.DA[round][i][j][k][0] == 1 & mitmSolution.DA[round][i][j][k][1] == 1 & mitmSolution.DA[round][i][j][k][2] == 0)
                output += "\\fill[color="+BlueColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+j+18)+") rectangle ++(1,1);\n";
              if (mitmSolution.DA[round][i][j][k][0] == 0 & mitmSolution.DA[round][i][j][k][1] == 1 & mitmSolution.DA[round][i][j][k][2] == 0)
                output += "\\fill[color="+PurpleColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+j+18)+") rectangle ++(1,1);\n";
              if (mitmSolution.DA[round][i][j][k][0] == 1 & mitmSolution.DA[round][i][j][k][1] == 1 & mitmSolution.DA[round][i][j][k][2] == 1)
                output += "\\fill[color="+GrayColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+j+18)+") rectangle ++(1,1);\n";
            }
          output += "\\draw("+(5*k)+","+(23*(mitmSolution.Rounds-round)+18)+") grid ++(4,3);\n";
          output += " \\node[align=center] at ("+(5*k+2)+","+(23*(mitmSolution.Rounds-round)+17)+")[scale=2] {{\\Large z="+k+"}};\n";
        }
        
        if (round == 0) {
	  int allred=0;
          int allblue=0;
          for (int k = 0; k < 32; k++)        
            for (int i = 0; i < 4; i++) {
              if (mitmSolution.DA[round][i][2][k][0] == 0 & mitmSolution.DA[round][i][2][k][1] == 1 & mitmSolution.DA[round][i][2][k][2] == 1)
                allred++;
              if (mitmSolution.DA[round][i][2][k][0] == 1 & mitmSolution.DA[round][i][2][k][1] == 1 & mitmSolution.DA[round][i][2][k][2] == 0)
		allblue++;
          }
          output += "\\fill[color="+RedColor+"] ("+(5*31+4+2)+","+(23*(mitmSolution.Rounds-round)+20)+")  rectangle ++(1,1);\n";
          output += "\\draw("+(5*31+4+2)+","+(23*(mitmSolution.Rounds-round)+20)+")  grid ++(1,1);\n";
          output += " \\node[align=center] at ("+(5*31+4+4.2)+","+(23*(mitmSolution.Rounds-round)+20.4)+") {\\textbf{\\huge = "+(allred)+"}};\n";
          output += "\\fill[color="+BlueColor+"] ("+(5*31+4+2)+","+(23*(mitmSolution.Rounds-round)+18)+")  rectangle ++(1,1);\n";
          output += "\\draw("+(5*31+4+2)+","+(23*(mitmSolution.Rounds-round)+18)+") grid ++(1,1);\n";   
          output += " \\node[align=center] at ("+(5*31+4+4.1)+","+(23*(mitmSolution.Rounds-round)+18.4)+") {\\textbf{\\huge = "+(allblue)+"}};\n";
        }
         //DP     
         output += " \\node[align=center] at ("+(-2)+","+(23*(mitmSolution.Rounds-round)+15.5)+") [scale=1.5]{\\textbf{\\huge $C^{("+round+")}$}};\n";
         int consumered = 0;
         for (int k = 0; k < 32; k++) 
         {       
          for (int i = 0; i < 4; i++)
           {
              if (mitmSolution.DP[round][i][k][0] == 0 & mitmSolution.DP[round][i][k][1] == 0 & mitmSolution.DP[round][i][k][2] == 0)
                output += "\\fill[color="+WhiteColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+15)+") rectangle ++(1,1);\n";
              if (mitmSolution.DP[round][i][k][0] == 0 & mitmSolution.DP[round][i][k][1] == 1 & mitmSolution.DP[round][i][k][2] == 1)
                output += "\\fill[color="+RedColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+15)+") rectangle ++(1,1);\n";
              if (mitmSolution.DP[round][i][k][0] == 1 & mitmSolution.DP[round][i][k][1] == 1 & mitmSolution.DP[round][i][k][2] == 0)
                output += "\\fill[color="+BlueColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+15)+") rectangle ++(1,1);\n";
              if (mitmSolution.DP[round][i][k][0] == 0 & mitmSolution.DP[round][i][k][1] == 1 & mitmSolution.DP[round][i][k][2] == 0)
                output += "\\fill[color="+PurpleColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+15)+") rectangle ++(1,1);\n";
              if (mitmSolution.DP[round][i][k][0] == 1 & mitmSolution.DP[round][i][k][1] == 1 & mitmSolution.DP[round][i][k][2] == 1)
                output += "\\fill[color="+GrayColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+15)+") rectangle ++(1,1);\n";
            }
          output += "\\draw("+(5*k)+","+(23*(mitmSolution.Rounds-round)+15)+") grid ++(4,1);\n";
          //output += " \\node[align=center] at ("+(5*k+2)+","+(21*(mitmSolution.Rounds-round)+13)+") {\\textbf{\\Large z="+k+"}};\n";
          for (int i = 0; i < 4; i++)
          {
              //DC1
              if (mitmSolution.DC1[round][i][k] == 1) {
                consumered ++;
                output += "\\draw[line width=2pt, color=yellow]("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+15)+") grid ++(1,1);\n";
              }
          }
        }

        output += "\\fill[color="+RedColor+"] ("+(5*31+4+4)+","+(23*(mitmSolution.Rounds-round)+15)+")  rectangle ++(1,1);\n";
        output += "\\draw("+(5*31+4+4)+","+(23*(mitmSolution.Rounds-round)+15)+") grid ++(1,1);\n";
        output += " \\node[align=center] at ("+(5*31+4+2.5)+","+(23*(mitmSolution.Rounds-round)+15.4)+") {\\textbf{\\huge - "+(consumered)+"}};\n";

   
       //DP2
       output += " \\node[align=center] at ("+(-2)+","+(23*(mitmSolution.Rounds-round)+13.5)+")[scale=1.5] {\\textbf{\\huge $D^{("+round+")}$}};\n";
       int consumered2 = 0;
       for (int k = 0; k < 32; k++) 
       {       
          for (int i = 0; i < 4; i++)
           {
              if (mitmSolution.DP2[round][i][k][0] == 0 & mitmSolution.DP2[round][i][k][1] == 0 & mitmSolution.DP2[round][i][k][2] == 0)
                output += "\\fill[color="+WhiteColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+13)+") rectangle ++(1,1);\n";
              if (mitmSolution.DP2[round][i][k][0] == 0 & mitmSolution.DP2[round][i][k][1] == 1 & mitmSolution.DP2[round][i][k][2] == 1)
                output += "\\fill[color="+RedColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+13)+") rectangle ++(1,1);\n";
              if (mitmSolution.DP2[round][i][k][0] == 1 & mitmSolution.DP2[round][i][k][1] == 1 & mitmSolution.DP2[round][i][k][2] == 0)
                output += "\\fill[color="+BlueColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+13)+") rectangle ++(1,1);\n";
              if (mitmSolution.DP2[round][i][k][0] == 0 & mitmSolution.DP2[round][i][k][1] == 1 & mitmSolution.DP2[round][i][k][2] == 0)
                output += "\\fill[color="+PurpleColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+13)+") rectangle ++(1,1);\n";
              if (mitmSolution.DP2[round][i][k][0] == 1 & mitmSolution.DP2[round][i][k][1] == 1 & mitmSolution.DP2[round][i][k][2] == 1)
                output += "\\fill[color="+GrayColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+13)+") rectangle ++(1,1);\n";
            }
          output += "\\draw("+(5*k)+","+(23*(mitmSolution.Rounds-round)+13)+") grid ++(4,1);\n";
          //output += " \\node[align=center] at ("+(5*k+2)+","+(21*(mitmSolution.Rounds-round)+13)+") {\\textbf{\\Large z="+k+"}};\n";
          for (int i = 0; i < 4; i++)
           {
              //DC12
              if (mitmSolution.DC12[round][i][k] == 1) {
                consumered2 ++;
                output += "\\draw[line width=2pt, color=yellow]("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+13)+") grid ++(1,1);\n";
              }
            }
        }
        output += "\\fill[color="+RedColor+"] ("+(5*31+4+4)+","+(23*(mitmSolution.Rounds-round)+13)+")  rectangle ++(1,1);\n";
        output += "\\draw("+(5*31+4+4)+","+(23*(mitmSolution.Rounds-round)+13)+") grid ++(1,1);\n";
        output += " \\node[align=center] at ("+(5*31+4+2.5)+","+(23*(mitmSolution.Rounds-round)+13.4)+") {\\textbf{\\huge - "+(consumered2)+"}};\n";
      
      // DB after theta
      
        output += " \\node[align=center] at ("+(-2)+","+(23*(mitmSolution.Rounds-round)+10.5)+")[scale=1.5] {\\textbf{\\huge ${\\Huge \\theta}^{("+round+")}$}};\n";
        for (int k = 0; k < 32; k++) 
        {       
          for (int i = 0; i < 4; i++)
            for (int j = 0; j < 3; j++) 
            {
              if (j==0) {
                if (mitmSolution.DB[round][i][j][k][0] == 0 & mitmSolution.DB[round][i][j][k][1] == 0 & mitmSolution.DB[round][i][j][k][2] == 0)
                  output += "\\fill[color="+WhiteColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+j+9)+") rectangle ++(1,1);\n";
                if (mitmSolution.DB[round][i][j][k][0] == 0 & mitmSolution.DB[round][i][j][k][1] == 1 & mitmSolution.DB[round][i][j][k][2] == 1)
                  output += "\\fill[color="+RedColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+j+9)+") rectangle ++(1,1);\n";
                if (mitmSolution.DB[round][i][j][k][0] == 1 & mitmSolution.DB[round][i][j][k][1] == 1 & mitmSolution.DB[round][i][j][k][2] == 0)
                  output += "\\fill[color="+BlueColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+j+9)+") rectangle ++(1,1);\n";
                if (mitmSolution.DB[round][i][j][k][0] == 0 & mitmSolution.DB[round][i][j][k][1] == 1 & mitmSolution.DB[round][i][j][k][2] == 0)
                  output += "\\fill[color="+PurpleColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+j+9)+") rectangle ++(1,1);\n";
                if (mitmSolution.DB[round][i][j][k][0] == 1 & mitmSolution.DB[round][i][j][k][1] == 1 & mitmSolution.DB[round][i][j][k][2] == 1)
                  output += "\\fill[color="+GrayColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+j+9)+") rectangle ++(1,1);\n";
              }
              if (j==1) {
                if (mitmSolution.DB[round][i][j][k][0] == 0 & mitmSolution.DB[round][i][j][k][1] == 0 & mitmSolution.DB[round][i][j][k][2] == 0)
                  output += "\\fill[color="+WhiteColor+"] ("+(5*k+(i+1)%4)+","+(23*(mitmSolution.Rounds-round)+j+9)+") rectangle ++(1,1);\n";
                if (mitmSolution.DB[round][i][j][k][0] == 0 & mitmSolution.DB[round][i][j][k][1] == 1 & mitmSolution.DB[round][i][j][k][2] == 1)
                  output += "\\fill[color="+RedColor+"] ("+(5*k+(i+1)%4)+","+(23*(mitmSolution.Rounds-round)+j+9)+") rectangle ++(1,1);\n";
                if (mitmSolution.DB[round][i][j][k][0] == 1 & mitmSolution.DB[round][i][j][k][1] == 1 & mitmSolution.DB[round][i][j][k][2] == 0)
                  output += "\\fill[color="+BlueColor+"] ("+(5*k+(i+1)%4)+","+(23*(mitmSolution.Rounds-round)+j+9)+") rectangle ++(1,1);\n";
                if (mitmSolution.DB[round][i][j][k][0] == 0 & mitmSolution.DB[round][i][j][k][1] == 1 & mitmSolution.DB[round][i][j][k][2] == 0)
                  output += "\\fill[color="+PurpleColor+"] ("+(5*k+(i+1)%4)+","+(23*(mitmSolution.Rounds-round)+j+9)+") rectangle ++(1,1);\n";
                if (mitmSolution.DB[round][i][j][k][0] == 1 & mitmSolution.DB[round][i][j][k][1] == 1 & mitmSolution.DB[round][i][j][k][2] == 1)
                  output += "\\fill[color="+GrayColor+"] ("+(5*k+(i+1)%4)+","+(23*(mitmSolution.Rounds-round)+j+9)+") rectangle ++(1,1);\n";
              }
              if (j==2) {
                if (mitmSolution.DB[round][i][j][k][0] == 0 & mitmSolution.DB[round][i][j][k][1] == 0 & mitmSolution.DB[round][i][j][k][2] == 0)
                  output += "\\fill[color="+WhiteColor+"] ("+(5*((k+11)%32)+i)+","+(23*(mitmSolution.Rounds-round)+j+9)+") rectangle ++(1,1);\n";
                if (mitmSolution.DB[round][i][j][k][0] == 0 & mitmSolution.DB[round][i][j][k][1] == 1 & mitmSolution.DB[round][i][j][k][2] == 1)
                  output += "\\fill[color="+RedColor+"] ("+(5*((k+11)%32)+i)+","+(23*(mitmSolution.Rounds-round)+j+9)+") rectangle ++(1,1);\n";
                if (mitmSolution.DB[round][i][j][k][0] == 1 & mitmSolution.DB[round][i][j][k][1] == 1 & mitmSolution.DB[round][i][j][k][2] == 0)
                  output += "\\fill[color="+BlueColor+"] ("+(5*((k+11)%32)+i)+","+(23*(mitmSolution.Rounds-round)+j+9)+") rectangle ++(1,1);\n";
                if (mitmSolution.DB[round][i][j][k][0] == 0 & mitmSolution.DB[round][i][j][k][1] == 1 & mitmSolution.DB[round][i][j][k][2] == 0)
                  output += "\\fill[color="+PurpleColor+"] ("+(5*((k+11)%32)+i)+","+(23*(mitmSolution.Rounds-round)+j+9)+") rectangle ++(1,1);\n";
                if (mitmSolution.DB[round][i][j][k][0] == 1 & mitmSolution.DB[round][i][j][k][1] == 1 & mitmSolution.DB[round][i][j][k][2] == 1)
                  output += "\\fill[color="+GrayColor+"] ("+(5*((k+11)%32)+i)+","+(23*(mitmSolution.Rounds-round)+j+9)+") rectangle ++(1,1);\n";
              }
            }
        }
        for (int k = 0; k < 32; k++) {
          output += "\\draw("+(5*k)+","+(23*(mitmSolution.Rounds-round)+9)+") grid ++(4,3);\n";
          //output += " \\node[align=center] at ("+(6*k+2)+","+(25*(mitmSolution.Rounds-round)+6)+") {\\textbf{\\Large z="+k+"}};\n";
        }
        int consumered3 = 0;
        for (int k = 0; k < 32; k++) 
        {       
          for (int i = 0; i < 4; i++)
            for (int j = 0; j < 3; j++) 
            {
              //DC2
              if (mitmSolution.DC2[round][i][j][k] == 1) {
                consumered3 ++;
                output += "\\draw[line width=2pt, color=yellow]("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+j+9)+") grid ++(1,1);\n"; 
              }
            }
        }
        output += "\\fill[color="+RedColor+"] ("+(5*31+4+4)+","+(23*(mitmSolution.Rounds-round)+10)+")  rectangle ++(1,1);\n";
        output += "\\draw("+(5*31+4+4)+","+(23*(mitmSolution.Rounds-round)+10)+")  grid ++(1,1);\n";
        output += " \\node[align=center] at ("+(5*31+4+2.5)+","+(23*(mitmSolution.Rounds-round)+10.4)+") {\\textbf{\\huge - "+(consumered3)+"}};\n";
      

      // DB before pho
        output += " \\node[align=center] at ("+(-2)+","+(23*(mitmSolution.Rounds-round)+6.5)+")[scale=1.5] {\\textbf{\\huge $\\iota^{("+round+")}$}};\n";
        for (int k = 0; k < 32; k++) 
        {       
          for (int i = 0; i < 4; i++)
            for (int j = 0; j < 3; j++) 
            {
              if (mitmSolution.DB[round][i][j][k][0] == 0 & mitmSolution.DB[round][i][j][k][1] == 0 & mitmSolution.DB[round][i][j][k][2] == 0)
                output += "\\fill[color="+WhiteColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+j+5)+") rectangle ++(1,1);\n";
              if (mitmSolution.DB[round][i][j][k][0] == 0 & mitmSolution.DB[round][i][j][k][1] == 1 & mitmSolution.DB[round][i][j][k][2] == 1)
                output += "\\fill[color="+RedColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+j+5)+") rectangle ++(1,1);\n";
              if (mitmSolution.DB[round][i][j][k][0] == 1 & mitmSolution.DB[round][i][j][k][1] == 1 & mitmSolution.DB[round][i][j][k][2] == 0)
                output += "\\fill[color="+BlueColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+j+5)+") rectangle ++(1,1);\n";
              if (mitmSolution.DB[round][i][j][k][0] == 0 & mitmSolution.DB[round][i][j][k][1] == 1 & mitmSolution.DB[round][i][j][k][2] == 0)
                output += "\\fill[color="+PurpleColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+j+5)+") rectangle ++(1,1);\n";
              if (mitmSolution.DB[round][i][j][k][0] == 1 & mitmSolution.DB[round][i][j][k][1] == 1 & mitmSolution.DB[round][i][j][k][2] == 1)
                output += "\\fill[color="+GrayColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+j+5)+") rectangle ++(1,1);\n";
            }
          output += "\\draw("+(5*k)+","+(23*(mitmSolution.Rounds-round)+5)+") grid ++(4,3);\n";
          //output += " \\node[align=center] at ("+(6*k+2)+","+(25*(mitmSolution.Rounds-round)-1)+") {\\textbf{\\Large z="+k+"}};\n";
        }
      
      
      //kai
      if (round<mitmSolution.Rounds-1) {
        output += " \\node[align=center] at ("+(-2)+","+(23*(mitmSolution.Rounds-round)+1.5)+")[scale=1.5] {\\textbf{\\huge $\\chi^{("+round+")}$}};\n";
        for (int k = 0; k < 32; k++) 
        {       
          for (int i = 0; i < 4; i++)
            for (int j = 0; j < 3; j++) 
            {
              if (j==0) {
                if (mitmSolution.DA[round+1][i][j][k][0] == 0 & mitmSolution.DA[round+1][i][j][k][1] == 0 & mitmSolution.DA[round+1][i][j][k][2] == 0)
                  output += "\\fill[color="+WhiteColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+j)+") rectangle ++(1,1);\n";
                if (mitmSolution.DA[round+1][i][j][k][0] == 0 & mitmSolution.DA[round+1][i][j][k][1] == 1 & mitmSolution.DA[round+1][i][j][k][2] == 1)
                  output += "\\fill[color="+RedColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+j)+") rectangle ++(1,1);\n";
                if (mitmSolution.DA[round+1][i][j][k][0] == 1 & mitmSolution.DA[round+1][i][j][k][1] == 1 & mitmSolution.DA[round+1][i][j][k][2] == 0)
                  output += "\\fill[color="+BlueColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+j)+") rectangle ++(1,1);\n";
                if (mitmSolution.DA[round+1][i][j][k][0] == 0 & mitmSolution.DA[round+1][i][j][k][1] == 1 & mitmSolution.DA[round+1][i][j][k][2] == 0)
                  output += "\\fill[color="+PurpleColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+j)+") rectangle ++(1,1);\n";
                if (mitmSolution.DA[round+1][i][j][k][0] == 1 & mitmSolution.DA[round+1][i][j][k][1] == 1 & mitmSolution.DA[round+1][i][j][k][2] == 1)
                  output += "\\fill[color="+GrayColor+"] ("+(5*k+i)+","+(23*(mitmSolution.Rounds-round)+j)+") rectangle ++(1,1);\n";
              }
              if (j==1) {
                if (mitmSolution.DA[round+1][i][j][k][0] == 0 & mitmSolution.DA[round+1][i][j][k][1] == 0 & mitmSolution.DA[round+1][i][j][k][2] == 0)
                  output += "\\fill[color="+WhiteColor+"] ("+(5*((k+1)%32)+i)+","+(23*(mitmSolution.Rounds-round)+j)+") rectangle ++(1,1);\n";
                if (mitmSolution.DA[round+1][i][j][k][0] == 0 & mitmSolution.DA[round+1][i][j][k][1] == 1 & mitmSolution.DA[round+1][i][j][k][2] == 1)
                  output += "\\fill[color="+RedColor+"] ("+(5*((k+1)%32)+i)+","+(23*(mitmSolution.Rounds-round)+j)+") rectangle ++(1,1);\n";
                if (mitmSolution.DA[round+1][i][j][k][0] == 1 & mitmSolution.DA[round+1][i][j][k][1] == 1 & mitmSolution.DA[round+1][i][j][k][2] == 0)
                  output += "\\fill[color="+BlueColor+"] ("+(5*((k+1)%32)+i)+","+(23*(mitmSolution.Rounds-round)+j)+") rectangle ++(1,1);\n";
                if (mitmSolution.DA[round+1][i][j][k][0] == 0 & mitmSolution.DA[round+1][i][j][k][1] == 1 & mitmSolution.DA[round+1][i][j][k][2] == 0)
                  output += "\\fill[color="+PurpleColor+"] ("+(5*((k+1)%32)+i)+","+(23*(mitmSolution.Rounds-round)+j)+") rectangle ++(1,1);\n";
                if (mitmSolution.DA[round+1][i][j][k][0] == 1 & mitmSolution.DA[round+1][i][j][k][1] == 1 & mitmSolution.DA[round+1][i][j][k][2] == 1)
                  output += "\\fill[color="+GrayColor+"] ("+(5*((k+1)%32)+i)+","+(23*(mitmSolution.Rounds-round)+j)+") rectangle ++(1,1);\n";
              }
              if (j==2) {
                if (mitmSolution.DA[round+1][i][j][k][0] == 0 & mitmSolution.DA[round+1][i][j][k][1] == 0 & mitmSolution.DA[round+1][i][j][k][2] == 0)
                  output += "\\fill[color="+WhiteColor+"] ("+(5*((k+8)%32)+(i+2)%4)+","+(23*(mitmSolution.Rounds-round)+j)+") rectangle ++(1,1);\n";
                if (mitmSolution.DA[round+1][i][j][k][0] == 0 & mitmSolution.DA[round+1][i][j][k][1] == 1 & mitmSolution.DA[round+1][i][j][k][2] == 1)
                  output += "\\fill[color="+RedColor+"] ("+(5*((k+8)%32)+(i+2)%4)+","+(23*(mitmSolution.Rounds-round)+j)+") rectangle ++(1,1);\n";
                if (mitmSolution.DA[round+1][i][j][k][0] == 1 & mitmSolution.DA[round+1][i][j][k][1] == 1 & mitmSolution.DA[round+1][i][j][k][2] == 0)
                  output += "\\fill[color="+BlueColor+"] ("+(5*((k+8)%32)+(i+2)%4)+","+(23*(mitmSolution.Rounds-round)+j)+") rectangle ++(1,1);\n";
                if (mitmSolution.DA[round+1][i][j][k][0] == 0 & mitmSolution.DA[round+1][i][j][k][1] == 1 & mitmSolution.DA[round+1][i][j][k][2] == 0)
                  output += "\\fill[color="+PurpleColor+"] ("+(5*((k+8)%32)+(i+2)%4)+","+(23*(mitmSolution.Rounds-round)+j)+") rectangle ++(1,1);\n";
                if (mitmSolution.DA[round+1][i][j][k][0] == 1 & mitmSolution.DA[round+1][i][j][k][1] == 1 & mitmSolution.DA[round+1][i][j][k][2] == 1)
                  output += "\\fill[color="+GrayColor+"] ("+(5*((k+8)%32)+(i+2)%4)+","+(23*(mitmSolution.Rounds-round)+j)+") rectangle ++(1,1);\n";
              }
            }
          output += "\\draw("+(5*k)+","+(23*(mitmSolution.Rounds-round)+18)+") grid ++(4,3);\n";
          output += " \\node[align=center] at ("+(5*k+2)+","+(23*(mitmSolution.Rounds-round)+17)+")[scale=2] {{\\Large z="+k+"}};\n";
        }
	for (int k = 0; k < 32; k++) 
        {       
          output += "\\draw("+(5*k)+","+(23*(mitmSolution.Rounds-round))+") grid ++(4,3);\n";
          //output += " \\node[align=center] at ("+(5*k+2)+","+(25*(mitmSolution.Rounds-round)+17)+")[scale=2] {{\\Large z="+k+"}};\n";
        }
      }
      /*for (int k = 0; k < 64; k++) 
      {
	if (mitmSolution.dom[0][k]==1)
        {
	  output += "\\node[align=center] at ("+(6*((k-rho[3][0]+64)%64)+3+0.5)+","+(18+4+0.5)+") {\\textbf{\\Large $m$}};\n";
          output += "\\node[align=center] at ("+(6*((k-rho[3][0]+64)%64)+3+0.5)+","+(18+1+0.5)+") {\\textbf{\\Large $m$}};\n";
          output += "\\node[align=center] at ("+(6*((k-rho[0][2]+64)%64)+0+0.5)+","+(18+2+0.5)+") {\\textbf{\\Large $m$}};\n";
          output += "\\node[align=center] at ("+(6*((k-rho[0][2]+64)%64)+0+0.5)+","+(18+4+0.5)+") {\\textbf{\\Large $m$}};\n";
	  //output += "\\draw[line width=2pt, color=green]("+(6*((k-rho[3][0]+64)%64)+3)+","+(18+4)+") grid ++(1,1);\n";
          //output += "\\draw[line width=2pt, color=green]("+(6*((k-rho[3][0]+64)%64)+3)+","+(18+1)+") grid ++(1,1);\n";
          //output += "\\draw[line width=2pt, color=green]("+(6*((k-rho[0][2]+64)%64)+0)+","+(18+2)+") grid ++(1,1);\n";
          //output += "\\draw[line width=2pt, color=green]("+(6*((k-rho[0][2]+64)%64)+0)+","+(18+4)+") grid ++(1,1);\n";
        }
        if (mitmSolution.dom[1][k]==1)
        {
	  output += "\\node[align=center] at ("+(6*((k-rho[4][1]+64)%64)+4+0.5)+","+(18+3+0.5)+") {\\textbf{\\Large $m$}};\n";
          output += "\\node[align=center] at ("+(6*((k-rho[4][1]+64)%64)+4+0.5)+","+(18+0+0.5)+") {\\textbf{\\Large $m$}};\n";
          output += "\\node[align=center] at ("+(6*((k-rho[1][3]+64)%64)+1+0.5)+","+(18+1+0.5)+") {\\textbf{\\Large $m$}};\n";
          output += "\\node[align=center] at ("+(6*((k-rho[1][3]+64)%64)+1+0.5)+","+(18+3+0.5)+") {\\textbf{\\Large $m$}};\n";
	  //output += "\\draw[line width=2pt, color=green]("+(6*((k-rho[4][1]+64)%64)+4)+","+(18+3)+") grid ++(1,1);\n";
          //output += "\\draw[line width=2pt, color=green]("+(6*((k-rho[4][1]+64)%64)+4)+","+(18+0)+") grid ++(1,1);\n";
          //output += "\\draw[line width=2pt, color=green]("+(6*((k-rho[1][3]+64)%64)+1)+","+(18+1)+") grid ++(1,1);\n";
          //output += "\\draw[line width=2pt, color=green]("+(6*((k-rho[1][3]+64)%64)+1)+","+(18+3)+") grid ++(1,1);\n";
        }
      }*/
      output += "\n";

    }



    // Footer
    output += "\\makeatother\n";
    output += "\\end{tikzpicture}\n";
    output += "\\end{document}\n";
    return output;
  }
}
