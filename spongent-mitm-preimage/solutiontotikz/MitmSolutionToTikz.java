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
/*
        for (int round = 0; round < mitmSolution.Rounds+1; round++) {
            int numered = 0;
            int numeblue = 0;
            output += " \\node[align=center] at ("+(-2)+","+(4*(mitmSolution.Rounds-round)+2)+") [scale=1.5]{\\textbf{\\huge $A^{("+(round)+")}$}};\n";

            for (int k = 0; k < mitmSolution.Af[round].length; k++)
            {

                if (mitmSolution.Af[round][k][0] == 0 & mitmSolution.Af[round][k][1] == 0)
                    output += "\\fill[color="+WhiteColor+"] ("+(5*(k/4)+k%4)+","+(4*(mitmSolution.Rounds-round)+2)+") rectangle ++(1,1);\n";
                if (mitmSolution.Af[round][k][0] == 0 & mitmSolution.Af[round][k][1] == 1) {
                    numered ++;
                    output += "\\fill[color=" + RedColor + "] (" + (5 * (k / 4) + k % 4) + "," + (4 * (mitmSolution.Rounds - round) + 2) + ") rectangle ++(1,1);\n";
                }
                if (mitmSolution.Af[round][k][0] == 1 & mitmSolution.Af[round][k][1] == 0) {
                    numeblue ++;
                    output += "\\fill[color=" + BlueColor + "] (" + (5 * (k / 4) + k % 4) + "," + (4 * (mitmSolution.Rounds - round) + 2) + ") rectangle ++(1,1);\n";
                }
                if (mitmSolution.Af[round][k][0] == 1 & mitmSolution.Af[round][k][1] == 1)
                    output += "\\fill[color="+GrayColor+"] ("+(5*(k/4)+k%4)+","+(4*(mitmSolution.Rounds-round)+2)+") rectangle ++(1,1);\n";
            }
            if (round ==0){
                output += "\\fill[color="+RedColor+"] ("+(5*( mitmSolution.DCf[round].length/4)+1)+","+(4 * (mitmSolution.Rounds - round)+ 2+0.5)+")  rectangle ++(1,1);\n";
                //output += "\\draw("+(5*( mitmSolution.DCf[round].length/4)+1)+","+(4 * (mitmSolution.Rounds - round)+0.5)+") grid ++(1,1);\n";
                output += " \\node[align=center] at ("+(5*( mitmSolution.DCf[round].length/4)+3.5)+","+(4 * (mitmSolution.Rounds - round)+ 2+0.5+0.5)+") {\\textbf{\\huge "+(numered)+"}};\n";

                output += "\\fill[color="+BlueColor+"] ("+(5*( mitmSolution.DCf[round].length/4)+1)+","+(4 * (mitmSolution.Rounds - round)+ 2-0.5)+")  rectangle ++(1,1);\n";
                //output += "\\draw("+(5*( mitmSolution.DCf[round].length/4)+1)+","+(4 * (mitmSolution.Rounds - round)-0.5)+") grid ++(1,1);\n";
                output += " \\node[align=center] at ("+(5*( mitmSolution.DCf[round].length/4)+3.5)+","+(4 * (mitmSolution.Rounds - round)+ 2-0.5+0.5)+") {\\textbf{\\huge "+(numeblue)+"}};\n";
            }
	
	    for (int k = 0; k < mitmSolution.Af[round].length; k++)
            {

                if ((k%4)==0)
                    output += "\\draw("+(5*(k/4)+k%4)+","+(4*(mitmSolution.Rounds-round)+2)+") grid ++(4,1);\n";
            }

            if ( round != mitmSolution.Rounds) {
                int consumered = 0;
                int consumeblue = 0;
                output += " \\node[align=center] at ("+(-2)+","+(4*(mitmSolution.Rounds-round))+") [scale=1.5]{\\textbf{\\huge $S^{("+(round)+")}$}};\n";

                for (int k = 0; k < mitmSolution.SBf[round].length; k++)
                {

                    if (mitmSolution.SBf[round][k][0] == 0 & mitmSolution.SBf[round][k][1] == 0)
                        output += "\\fill[color="+WhiteColor+"] ("+(5*(k/4)+k%4)+","+(4*(mitmSolution.Rounds-round))+") rectangle ++(1,1);\n";
                    if (mitmSolution.SBf[round][k][0] == 0 & mitmSolution.SBf[round][k][1] == 1)
                        output += "\\fill[color="+RedColor+"] ("+(5*(k/4)+k%4)+","+(4*(mitmSolution.Rounds-round))+") rectangle ++(1,1);\n";
                    if (mitmSolution.SBf[round][k][0] == 1 & mitmSolution.SBf[round][k][1] == 0)
                        output += "\\fill[color="+BlueColor+"] ("+(5*(k/4)+k%4)+","+(4*(mitmSolution.Rounds-round))+") rectangle ++(1,1);\n";
                    if (mitmSolution.SBf[round][k][0] == 1 & mitmSolution.SBf[round][k][1] == 1)
                        output += "\\fill[color="+GrayColor+"] ("+(5*(k/4)+k%4)+","+(4*(mitmSolution.Rounds-round))+") rectangle ++(1,1);\n";

                }

                for (int k = 0; k < mitmSolution.SBf[round].length; k++)
                        {

                            if ((k%4)==0)
                                output += "\\draw("+(5*(k/4)+k%4)+","+(4*(mitmSolution.Rounds-round))+") grid ++(4,1);\n";
                        }
                for (int k = 0; k < mitmSolution.DCf[round].length; k++) {
                    if (mitmSolution.DCf[round][k][0] == 1) {
                        consumered ++;
                        output += "\\draw[line width=2pt, color=red] (" + (5 * (k / 4) + k % 4) + "," + (4 * (mitmSolution.Rounds - round)) + ") rectangle ++(1,1);\n";
                    }
                    if (mitmSolution.DCf[round][k][1] == 1) {
                        consumeblue ++;
                        output += "\\draw[line width=2pt, color=blue] (" + (5 * (k / 4) + k % 4) + "," + (4 * (mitmSolution.Rounds - round)) + ") rectangle ++(1,1);\n";
                    }
                }
                output += "\\fill[color="+RedColor+"] ("+(5*( mitmSolution.DCf[round].length/4)+1)+","+(4 * (mitmSolution.Rounds - round)+0.5)+")  rectangle ++(1,1);\n";
                //output += "\\draw("+(5*( mitmSolution.DCf[round].length/4)+1)+","+(4 * (mitmSolution.Rounds - round)+0.5)+") grid ++(1,1);\n";
                output += " \\node[align=center] at ("+(5*( mitmSolution.DCf[round].length/4)+3.5)+","+(4 * (mitmSolution.Rounds - round)+0.5+0.5)+") {\\textbf{\\huge - "+(consumered)+"}};\n";

                output += "\\fill[color="+BlueColor+"] ("+(5*( mitmSolution.DCf[round].length/4)+1)+","+(4 * (mitmSolution.Rounds - round)-0.5)+")  rectangle ++(1,1);\n";
                //output += "\\draw("+(5*( mitmSolution.DCf[round].length/4)+1)+","+(4 * (mitmSolution.Rounds - round)-0.5)+") grid ++(1,1);\n";
                output += " \\node[align=center] at ("+(5*( mitmSolution.DCf[round].length/4)+3.5)+","+(4 * (mitmSolution.Rounds - round)-0.5+0.5)+") {\\textbf{\\huge - "+(consumeblue)+"}};\n";

            }


        }*/

        for (int round = 0; round < mitmSolution.Rounds+1; round++) {
            int numered = 0;
            int numeblue = 0;
            output += " \\node[align=center] at ("+(-2)+","+(4*round - 4*(mitmSolution.Rounds)-2)+") [scale=1.5]{\\textbf{\\huge $A^{("+(round)+")}$}};\n";

            for (int k = 0; k < mitmSolution.Ab[round].length; k++)
            {

                if (mitmSolution.Ab[round][k][0] == 0 & mitmSolution.Ab[round][k][1] == 0)
                    output += "\\fill[color="+WhiteColor+"] ("+(5*(k/4)+k%4)+","+(4*round - 4*(mitmSolution.Rounds)-2)+") rectangle ++(1,1);\n";
                if (mitmSolution.Ab[round][k][0] == 0 & mitmSolution.Ab[round][k][1] == 1) {
                    numered ++;
                    output += "\\fill[color=" + RedColor + "] (" + (5 * (k / 4) + k % 4) + "," + (4 * round - 4 * (mitmSolution.Rounds) - 2) + ") rectangle ++(1,1);\n";
                }
                if (mitmSolution.Ab[round][k][0] == 1 & mitmSolution.Ab[round][k][1] == 0) {
                    numeblue ++;
                    output += "\\fill[color=" + BlueColor + "] (" + (5 * (k / 4) + k % 4) + "," + (4 * round - 4 * (mitmSolution.Rounds) - 2) + ") rectangle ++(1,1);\n";
                }
                if (mitmSolution.Ab[round][k][0] == 1 & mitmSolution.Ab[round][k][1] == 1)
                    output += "\\fill[color="+GrayColor+"] ("+(5*(k/4)+k%4)+","+(4*round - 4*(mitmSolution.Rounds)-2)+") rectangle ++(1,1);\n";
            }
            if (round ==0){
                output += "\\fill[color="+RedColor+"] ("+(5*( mitmSolution.DCf[round].length/4)+1)+","+(4 * round - 4 * (mitmSolution.Rounds) - 1.5)+")  rectangle ++(1,1);\n";
                //output += "\\draw("+(5*( mitmSolution.DCf[round].length/4)+1)+","+(4 * round - 4 * (mitmSolution.Rounds) - 1.5)+") grid ++(1,1);\n";
                output += " \\node[align=center] at ("+(5*( mitmSolution.DCf[round].length/4)+3.5)+","+(4 * round - 4 * (mitmSolution.Rounds) - 1.5+0.5)+") {\\textbf{\\huge "+(numered)+"}};\n";

                output += "\\fill[color="+BlueColor+"] ("+(5*( mitmSolution.DCf[round].length/4)+1)+","+(4 * round - 4 * (mitmSolution.Rounds) - 2.5)+")  rectangle ++(1,1);\n";
                //output += "\\draw("+(5*( mitmSolution.DCf[round].length/4)+1)+","+(4 * round - 4 * (mitmSolution.Rounds) - 2.5)+") grid ++(1,1);\n";
                output += " \\node[align=center] at ("+(5*( mitmSolution.DCf[round].length/4)+3.5)+","+(4 * round - 4 * (mitmSolution.Rounds) - 2.5+0.5)+") {\\textbf{\\huge "+(numeblue)+"}};\n";
            }
	    for (int k = 0; k < mitmSolution.Ab[round].length; k++)
            {

                if ((k%4)==0)
                    output += "\\draw("+(5*(k/4)+k%4)+","+(4*round - 4*(mitmSolution.Rounds)-2)+") grid ++(4,1);\n";
            }
	    if ( round > 0 & round != mitmSolution.Rounds){
            int consumered = 0;
            int consumeblue = 0;
            for (int k = 0; k < mitmSolution.DCb[round].length; k++) {
                if (mitmSolution.DCb[round - 1][k][0] == 1) {
                    consumered ++;
                    output += "\\draw[line width=2pt, color=red] (" + (5 * (k / 4) + k % 4) + "," + (4 * round - 4 * (mitmSolution.Rounds) - 2) + ") rectangle ++(1,1);\n";
                }
                if (mitmSolution.DCb[round - 1][k][1] == 1) {
                    consumeblue ++;
                    output += "\\draw[line width=2pt, color=blue] (" + (5 * (k / 4) + k % 4) + "," + (4 * round - 4 * (mitmSolution.Rounds) - 2) + ") rectangle ++(1,1);\n";
                }

            }

            output += "\\fill[color="+RedColor+"] ("+(5*( mitmSolution.DCf[round].length/4)+1)+","+(4 * round - 4 * (mitmSolution.Rounds) - 1.5)+")  rectangle ++(1,1);\n";
            //output += "\\draw("+(5*( mitmSolution.DCf[round].length/4)+1)+","+(4 * round - 4 * (mitmSolution.Rounds) - 1.5)+") grid ++(1,1);\n";
            output += " \\node[align=center] at ("+(5*( mitmSolution.DCf[round].length/4)+3.5)+","+(4 * round - 4 * (mitmSolution.Rounds) - 1.5+0.5)+") {\\textbf{\\huge - "+(consumered)+"}};\n";

            output += "\\fill[color="+BlueColor+"] ("+(5*( mitmSolution.DCf[round].length/4)+1)+","+(4 * round - 4 * (mitmSolution.Rounds) - 2.5)+")  rectangle ++(1,1);\n";
            //output += "\\draw("+(5*( mitmSolution.DCf[round].length/4)+1)+","+(4 * round - 4 * (mitmSolution.Rounds) - 2.5)+") grid ++(1,1);\n";
            output += " \\node[align=center] at ("+(5*( mitmSolution.DCf[round].length/4)+3.5)+","+(4 * round - 4 * (mitmSolution.Rounds) - 2.5+0.5)+") {\\textbf{\\huge - "+(consumeblue)+"}};\n";
}


            if ( round != mitmSolution.Rounds) {
                output += " \\node[align=center] at ("+(-2)+","+(4*round - 4*(mitmSolution.Rounds))+") [scale=1.5]{\\textbf{\\huge $S^{("+(round)+")}$}};\n";

                for (int k = 0; k < mitmSolution.SBb[round].length; k++)
                {

                    if (mitmSolution.SBb[round][k][0] == 0 & mitmSolution.SBb[round][k][1] == 0)
                        output += "\\fill[color="+WhiteColor+"] ("+(5*(k/4)+k%4)+","+(4*round - 4*(mitmSolution.Rounds))+") rectangle ++(1,1);\n";
                    if (mitmSolution.SBb[round][k][0] == 0 & mitmSolution.SBb[round][k][1] == 1)
                        output += "\\fill[color="+RedColor+"] ("+(5*(k/4)+k%4)+","+(4*round - 4*(mitmSolution.Rounds))+") rectangle ++(1,1);\n";
                    if (mitmSolution.SBb[round][k][0] == 1 & mitmSolution.SBb[round][k][1] == 0)
                        output += "\\fill[color="+BlueColor+"] ("+(5*(k/4)+k%4)+","+(4*round - 4*(mitmSolution.Rounds))+") rectangle ++(1,1);\n";
                    if (mitmSolution.SBb[round][k][0] == 1 & mitmSolution.SBb[round][k][1] == 1)
                        output += "\\fill[color="+GrayColor+"] ("+(5*(k/4)+k%4)+","+(4*round - 4*(mitmSolution.Rounds))+") rectangle ++(1,1);\n";

                }
		for (int k = 0; k < mitmSolution.Ab[round].length; k++)
            {
                if ((k%4)==0)
                    output += "\\draw("+(5*(k/4)+k%4)+","+(4*round - 4*(mitmSolution.Rounds))+") grid ++(4,1);\n";
            }


            }


        }








        // Footer
        output += "\\makeatother\n";
        output += "\\end{tikzpicture}\n";
        output += "\\end{document}\n";
        return output;
    }
}
