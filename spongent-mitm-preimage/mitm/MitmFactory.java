package mitmsearch.mitm;

import gurobi.*;

public class MitmFactory {
    private GRBModel model;
    private static final int[] permutation_f_88 = new int[]{0, 22, 44, 66, 1, 23, 45, 67, 2, 24, 46, 68, 3, 25, 47, 69, 4, 26, 48, 70, 5, 27, 49, 71, 6, 28, 50, 72, 7, 29, 51, 73, 8, 30, 52, 74, 9, 31, 53, 75, 10, 32, 54, 76, 11, 33, 55, 77, 12, 34, 56, 78, 13, 35, 57, 79, 14, 36, 58, 80, 15, 37, 59, 81, 16, 38, 60, 82, 17, 39, 61, 83, 18, 40, 62, 84, 19, 41, 63, 85, 20, 42, 64, 86, 21, 43, 65, 87};
    private static final int[] permutation_f_176 = new int[]{0, 44, 88, 132, 1, 45, 89, 133, 2, 46, 90, 134, 3, 47, 91, 135, 4, 48, 92, 136, 5, 49, 93, 137, 6, 50, 94, 138, 7, 51, 95, 139, 8, 52, 96, 140, 9, 53, 97, 141, 10, 54, 98, 142, 11, 55, 99, 143, 12, 56, 100, 144, 13, 57, 101, 145, 14, 58, 102, 146, 15, 59, 103, 147, 16, 60, 104, 148, 17, 61, 105, 149, 18, 62, 106, 150, 19, 63, 107, 151, 20, 64, 108, 152, 21, 65, 109, 153, 22, 66, 110, 154, 23, 67, 111, 155, 24, 68, 112, 156, 25, 69, 113, 157, 26, 70, 114, 158, 27, 71, 115, 159, 28, 72, 116, 160, 29, 73, 117, 161, 30, 74, 118, 162, 31, 75, 119, 163, 32, 76, 120, 164, 33, 77, 121, 165, 34, 78, 122, 166, 35, 79, 123, 167, 36, 80, 124, 168, 37, 81, 125, 169, 38, 82, 126, 170, 39, 83, 127, 171, 40, 84, 128, 172, 41, 85, 129, 173, 42, 86, 130, 174, 43, 87, 131, 175};
    private static final int[] permutation_f_240 = new int[]{0, 60, 120, 180, 1, 61, 121, 181, 2, 62, 122, 182, 3, 63, 123, 183, 4, 64, 124, 184, 5, 65, 125, 185, 6, 66, 126, 186, 7, 67, 127, 187, 8, 68, 128, 188, 9, 69, 129, 189, 10, 70, 130, 190, 11, 71, 131, 191, 12, 72, 132, 192, 13, 73, 133, 193, 14, 74, 134, 194, 15, 75, 135, 195, 16, 76, 136, 196, 17, 77, 137, 197, 18, 78, 138, 198, 19, 79, 139, 199, 20, 80, 140, 200, 21, 81, 141, 201, 22, 82, 142, 202, 23, 83, 143, 203, 24, 84, 144, 204, 25, 85, 145, 205, 26, 86, 146, 206, 27, 87, 147, 207, 28, 88, 148, 208, 29, 89, 149, 209, 30, 90, 150, 210, 31, 91, 151, 211, 32, 92, 152, 212, 33, 93, 153, 213, 34, 94, 154, 214, 35, 95, 155, 215, 36, 96, 156, 216, 37, 97, 157, 217, 38, 98, 158, 218, 39, 99, 159, 219, 40, 100, 160, 220, 41, 101, 161, 221, 42, 102, 162, 222, 43, 103, 163, 223, 44, 104, 164, 224, 45, 105, 165, 225, 46, 106, 166, 226, 47, 107, 167, 227, 48, 108, 168, 228, 49, 109, 169, 229, 50, 110, 170, 230, 51, 111, 171, 231, 52, 112, 172, 232, 53, 113, 173, 233, 54, 114, 174, 234, 55, 115, 175, 235, 56, 116, 176, 236, 57, 117, 177, 237, 58, 118, 178, 238, 59, 119, 179, 239};
    private static final int[] permutation_f_272 = new int[]{0, 68, 136, 204, 1, 69, 137, 205, 2, 70, 138, 206, 3, 71, 139, 207, 4, 72, 140, 208, 5, 73, 141, 209, 6, 74, 142, 210, 7, 75, 143, 211, 8, 76, 144, 212, 9, 77, 145, 213, 10, 78, 146, 214, 11, 79, 147, 215, 12, 80, 148, 216, 13, 81, 149, 217, 14, 82, 150, 218, 15, 83, 151, 219, 16, 84, 152, 220, 17, 85, 153, 221, 18, 86, 154, 222, 19, 87, 155, 223, 20, 88, 156, 224, 21, 89, 157, 225, 22, 90, 158, 226, 23, 91, 159, 227, 24, 92, 160, 228, 25, 93, 161, 229, 26, 94, 162, 230, 27, 95, 163, 231, 28, 96, 164, 232, 29, 97, 165, 233, 30, 98, 166, 234, 31, 99, 167, 235, 32, 100, 168, 236, 33, 101, 169, 237, 34, 102, 170, 238, 35, 103, 171, 239, 36, 104, 172, 240, 37, 105, 173, 241, 38, 106, 174, 242, 39, 107, 175, 243, 40, 108, 176, 244, 41, 109, 177, 245, 42, 110, 178, 246, 43, 111, 179, 247, 44, 112, 180, 248, 45, 113, 181, 249, 46, 114, 182, 250, 47, 115, 183, 251, 48, 116, 184, 252, 49, 117, 185, 253, 50, 118, 186, 254, 51, 119, 187, 255, 52, 120, 188, 256, 53, 121, 189, 257, 54, 122, 190, 258, 55, 123, 191, 259, 56, 124, 192, 260, 57, 125, 193, 261, 58, 126, 194, 262, 59, 127, 195, 263, 60, 128, 196, 264, 61, 129, 197, 265, 62, 130, 198, 266, 63, 131, 199, 267, 64, 132, 200, 268, 65, 133, 201, 269, 66, 134, 202, 270, 67, 135, 203, 271};
    private static final int[] permutation_f_336 = new int[]{0, 84, 168, 252, 1, 85, 169, 253, 2, 86, 170, 254, 3, 87, 171, 255, 4, 88, 172, 256, 5, 89, 173, 257, 6, 90, 174, 258, 7, 91, 175, 259, 8, 92, 176, 260, 9, 93, 177, 261, 10, 94, 178, 262, 11, 95, 179, 263, 12, 96, 180, 264, 13, 97, 181, 265, 14, 98, 182, 266, 15, 99, 183, 267, 16, 100, 184, 268, 17, 101, 185, 269, 18, 102, 186, 270, 19, 103, 187, 271, 20, 104, 188, 272, 21, 105, 189, 273, 22, 106, 190, 274, 23, 107, 191, 275, 24, 108, 192, 276, 25, 109, 193, 277, 26, 110, 194, 278, 27, 111, 195, 279, 28, 112, 196, 280, 29, 113, 197, 281, 30, 114, 198, 282, 31, 115, 199, 283, 32, 116, 200, 284, 33, 117, 201, 285, 34, 118, 202, 286, 35, 119, 203, 287, 36, 120, 204, 288, 37, 121, 205, 289, 38, 122, 206, 290, 39, 123, 207, 291, 40, 124, 208, 292, 41, 125, 209, 293, 42, 126, 210, 294, 43, 127, 211, 295, 44, 128, 212, 296, 45, 129, 213, 297, 46, 130, 214, 298, 47, 131, 215, 299, 48, 132, 216, 300, 49, 133, 217, 301, 50, 134, 218, 302, 51, 135, 219, 303, 52, 136, 220, 304, 53, 137, 221, 305, 54, 138, 222, 306, 55, 139, 223, 307, 56, 140, 224, 308, 57, 141, 225, 309, 58, 142, 226, 310, 59, 143, 227, 311, 60, 144, 228, 312, 61, 145, 229, 313, 62, 146, 230, 314, 63, 147, 231, 315, 64, 148, 232, 316, 65, 149, 233, 317, 66, 150, 234, 318, 67, 151, 235, 319, 68, 152, 236, 320, 69, 153, 237, 321, 70, 154, 238, 322, 71, 155, 239, 323, 72, 156, 240, 324, 73, 157, 241, 325, 74, 158, 242, 326, 75, 159, 243, 327, 76, 160, 244, 328, 77, 161, 245, 329, 78, 162, 246, 330, 79, 163, 247, 331, 80, 164, 248, 332, 81, 165, 249, 333, 82, 166, 250, 334, 83, 167, 251, 335};

    private static final int[] permutation_f_384 = new int[]{0, 96, 192, 288, 1, 97, 193, 289, 2, 98, 194, 290, 3, 99, 195, 291, 4, 100, 196, 292, 5, 101, 197, 293, 6, 102, 198, 294, 7, 103, 199, 295, 8, 104, 200, 296, 9, 105, 201, 297, 10, 106, 202, 298, 11, 107, 203, 299, 12, 108, 204, 300, 13, 109, 205, 301, 14, 110, 206, 302, 15, 111, 207, 303, 16, 112, 208, 304, 17, 113, 209, 305, 18, 114, 210, 306, 19, 115, 211, 307, 20, 116, 212, 308, 21, 117, 213, 309, 22, 118, 214, 310, 23, 119, 215, 311, 24, 120, 216, 312, 25, 121, 217, 313, 26, 122, 218, 314, 27, 123, 219, 315, 28, 124, 220, 316, 29, 125, 221, 317, 30, 126, 222, 318, 31, 127, 223, 319, 32, 128, 224, 320, 33, 129, 225, 321, 34, 130, 226, 322, 35, 131, 227, 323, 36, 132, 228, 324, 37, 133, 229, 325, 38, 134, 230, 326, 39, 135, 231, 327, 40, 136, 232, 328, 41, 137, 233, 329, 42, 138, 234, 330, 43, 139, 235, 331, 44, 140, 236, 332, 45, 141, 237, 333, 46, 142, 238, 334, 47, 143, 239, 335, 48, 144, 240, 336, 49, 145, 241, 337, 50, 146, 242, 338, 51, 147, 243, 339, 52, 148, 244, 340, 53, 149, 245, 341, 54, 150, 246, 342, 55, 151, 247, 343, 56, 152, 248, 344, 57, 153, 249, 345, 58, 154, 250, 346, 59, 155, 251, 347, 60, 156, 252, 348, 61, 157, 253, 349, 62, 158, 254, 350, 63, 159, 255, 351, 64, 160, 256, 352, 65, 161, 257, 353, 66, 162, 258, 354, 67, 163, 259, 355, 68, 164, 260, 356, 69, 165, 261, 357, 70, 166, 262, 358, 71, 167, 263, 359, 72, 168, 264, 360, 73, 169, 265, 361, 74, 170, 266, 362, 75, 171, 267, 363, 76, 172, 268, 364, 77, 173, 269, 365, 78, 174, 270, 366, 79, 175, 271, 367, 80, 176, 272, 368, 81, 177, 273, 369, 82, 178, 274, 370, 83, 179, 275, 371, 84, 180, 276, 372, 85, 181, 277, 373, 86, 182, 278, 374, 87, 183, 279, 375, 88, 184, 280, 376, 89, 185, 281, 377, 90, 186, 282, 378, 91, 187, 283, 379, 92, 188, 284, 380, 93, 189, 285, 381, 94, 190, 286, 382, 95, 191, 287, 383};
    public MitmFactory(final GRBModel model) {
    this.model = model;
  }


    public void addfixed_in(GRBVar[][][] Af,GRBVar[][][] Ab,int Block, int Rate) throws GRBException {
      for (int i = 0; i < Block; i++)
	    if (i < Rate){
            model.addConstr(Af[0][i][0], GRB.EQUAL, 1, "");
            model.addConstr(Af[0][i][1], GRB.EQUAL, 1, "");

            model.addConstr(Af[0][i][0], GRB.EQUAL, Ab[0][i][0], "");
            model.addConstr(Af[0][i][1], GRB.EQUAL, Ab[0][i][1], "");
          }
          else {
            model.addConstr(linExprOf(Af[0][i][0],Af[0][i][1]), GRB.GREATER_EQUAL, 1, "");

            model.addConstr(Af[0][i][0], GRB.EQUAL, Ab[0][i][0], "");
            model.addConstr(Af[0][i][1], GRB.EQUAL, Ab[0][i][1], "");
          }
	}

    public void addfixed_in_inv(GRBVar[][][] A,int Block, int Rate) throws GRBException {
        for (int i = 0; i < Block; i++)
            if (i < Rate){
                model.addConstr(linExprOf(A[0][i][0],A[0][i][1]), GRB.GREATER_EQUAL, 1, "");
            }
            else {
                model.addConstr(A[0][i][0], GRB.EQUAL, 1, "");
                model.addConstr(A[0][i][1], GRB.EQUAL, 1, "");
            }
    }

    public void addSbox(GRBVar[][][] A, GRBVar[][][]SB, GRBVar[][][] DC,int Block) throws GRBException {

        int Rounds = SB.length;
        GRBVar[][][] AllOne = new GRBVar[Rounds][Block/4][2];

        for (int round = 0; round < Rounds; round++)
            for (int i = 0; i < (Block/4); i++)
                        for (int l = 0; l < 2; l++) {
                            AllOne[round][i][l] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "AllOnef_"+round+"_"+i+"_"+l);
                            Determine_AllOne(AllOne[round][i][l], A[round][i*4][l], A[round][i*4+1][l], A[round][i*4+2][l], A[round][i*4+3][l]);
                        }
/*
        GRBVar[][] Allgray = new GRBVar[Rounds][Block/4];
        for (int round = 0; round < Rounds; round++)
            for (int i = 0; i < (Block/4); i++) {
                Allgray[round][i] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "Allgrayf_"+round+"_"+i);

                Determine_AllOne(Allgray[round][i], A[round][i*4][0], A[round][i*4][1], A[round][i*4+1][0], A[round][i*4+1][1], A[round][i*4+2][0], A[round][i*4+2][1], A[round][i*4+3][0], A[round][i*4+3][1]);
            }

 */

        double[] t1 = {1, -1, 1};
        double[] t2 = {1, 1, 1, 1, 1, 1, 1, 1, -1};
        double[] t3 = {1, -1, -1};
        for (int round = 0; round < Rounds; round++) {
            for (int i = 0; i < (Block / 4); i++) {
                for (int k = 0; k < 4; k++) {
                    //red
                    model.addConstr(SB[round][i * 4 + k][0], GRB.GREATER_EQUAL, AllOne[round][i][0], "");

                    model.addConstr(linExprOf(t1, DC[round][i * 4 + k][0], SB[round][i * 4 + k][0], AllOne[round][i][0]), GRB.EQUAL, 0, "");
                    //model.addConstr(linExprOf(t1, DC[round][i*4+1][0], SB[round][i*4+1][0], AllOne[round][i][0]), GRB.EQUAL, 0, "");
                    //model.addConstr(linExprOf(t1, DC[round][i*4+2][0], SB[round][i*4+2][0], AllOne[round][i][0]), GRB.EQUAL, 0, "");
                    //model.addConstr(linExprOf(t1, DC[round][i*4+3][0], SB[round][i*4+3][0], AllOne[round][i][0]), GRB.EQUAL, 0, "");


                    //blue
                    model.addConstr(SB[round][i * 4 + k][1], GRB.GREATER_EQUAL, AllOne[round][i][1], "");

                    model.addConstr(linExprOf(t1, DC[round][i * 4 + k][1], SB[round][i * 4 + k][1], AllOne[round][i][1]), GRB.EQUAL, 0, "");
                    //model.addConstr(linExprOf(t1, DC[round][i*4+1][1], SB[round][i*4+1][1], AllOne[round][i][1]), GRB.EQUAL, 0, "");
                    //model.addConstr(linExprOf(t1, DC[round][i*4+2][1], SB[round][i*4+2][1], AllOne[round][i][1]), GRB.EQUAL, 0, "");
                    //model.addConstr(linExprOf(t1, DC[round][i*4+3][1], SB[round][i*4+3][1], AllOne[round][i][1]), GRB.EQUAL, 0, "");

                    //red and blue exist, output white
                    model.addConstr(linExprOf(t3, SB[round][i * 4 + k][0], AllOne[round][i][0], AllOne[round][i][1]), GRB.LESS_EQUAL, 0, "");
                    model.addConstr(linExprOf(t3, SB[round][i * 4 + k][1], AllOne[round][i][0], AllOne[round][i][1]), GRB.LESS_EQUAL, 0, "");
                }
                //will lead to: consume = input num of red
                //model.addConstr(linExprOf(DC[round][i * 4][0], DC[round][i * 4+1][0], DC[round][i * 4+2][0], DC[round][i * 4+3][0], A[round][i * 4][0], A[round][i * 4+1][0], A[round][i * 4+2][0], A[round][i * 4+3][0]), GRB.LESS_EQUAL, 4, "");
                //model.addConstr(linExprOf(DC[round][i * 4][1], DC[round][i * 4+1][1], DC[round][i * 4+2][1], DC[round][i * 4+3][1], A[round][i * 4][1], A[round][i * 4+1][1], A[round][i * 4+2][1], A[round][i * 4+3][1]), GRB.LESS_EQUAL, 4, "");

                //DC - numred - allgray <= -1
                model.addConstr(linExprOf(t2,DC[round][i * 4][0], DC[round][i * 4+1][0], DC[round][i * 4+2][0], DC[round][i * 4+3][0], A[round][i * 4][0], A[round][i * 4+1][0], A[round][i * 4+2][0], A[round][i * 4+3][0],AllOne[round][i][0]), GRB.LESS_EQUAL, 3, "");
                model.addConstr(linExprOf(t2,DC[round][i * 4][1], DC[round][i * 4+1][1], DC[round][i * 4+2][1], DC[round][i * 4+3][1], A[round][i * 4][1], A[round][i * 4+1][1], A[round][i * 4+2][1], A[round][i * 4+3][1],AllOne[round][i][1]), GRB.LESS_EQUAL, 3, "");
            }
        }
    }

    public void addSbox_inv(GRBVar[][][] A, GRBVar[][][]SB, GRBVar[][][] DC,int Block) throws GRBException {

        int Rounds = SB.length;
        GRBVar[][][] AllOne = new GRBVar[Rounds][Block/4][2];

        for (int round = 0; round < Rounds; round++)
            for (int i = 0; i < (Block/4); i++)
                for (int l = 0; l < 2; l++) {
                    AllOne[round][i][l] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "AllOneb_"+round+"_"+i+"_"+l);
                    Determine_AllOne(AllOne[round][i][l], SB[round][i*4][l], SB[round][i*4+1][l], SB[round][i*4+2][l], SB[round][i*4+3][l]);
                }
        /*
        GRBVar[][] Allgray = new GRBVar[Rounds][Block/4];
        for (int round = 0; round < Rounds; round++)
            for (int i = 0; i < (Block/4); i++) {
                Allgray[round][i] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "Allgrayb_"+round+"_"+i);

                Determine_AllOne(Allgray[round][i], SB[round][i*4][0], SB[round][i*4][1], SB[round][i*4+1][0], SB[round][i*4+1][1], SB[round][i*4+2][0], SB[round][i*4+2][1], SB[round][i*4+3][0], SB[round][i*4+3][1]);
            }

         */

        double[] t1 = {1, -1, 1};
        double[] t2 = {1, 1, 1, 1, 1, 1, 1, 1, -1};
        double[] t3 = {1, -1, -1};
        for (int round = 0; round < Rounds; round++) {
            for (int i = 0; i < (Block / 4); i++) {
                for (int k = 0; k < 4; k++) {
                    //red
                    model.addConstr(A[round+1][i * 4 + k][0], GRB.GREATER_EQUAL, AllOne[round][i][0], "");

                    model.addConstr(linExprOf(t1, DC[round][i * 4 + k][0], A[round+1][i * 4 + k][0], AllOne[round][i][0]), GRB.EQUAL, 0, "");

                    //blue
                    model.addConstr(A[round+1][i * 4 + k][1], GRB.GREATER_EQUAL, AllOne[round][i][1], "");

                    model.addConstr(linExprOf(t1, DC[round][i * 4 + k][1], A[round+1][i * 4 + k][1], AllOne[round][i][1]), GRB.EQUAL, 0, "");

                    //red and blue exist, output white
                    model.addConstr(linExprOf(t3, A[round+1][i * 4 + k][0], AllOne[round][i][0], AllOne[round][i][1]), GRB.LESS_EQUAL, 0, "");
                    model.addConstr(linExprOf(t3, A[round+1][i * 4 + k][1], AllOne[round][i][0], AllOne[round][i][1]), GRB.LESS_EQUAL, 0, "");
                }
                //will lead to: consume = input num of red
                //model.addConstr(linExprOf(DC[round][i * 4][0], DC[round][i * 4+1][0], DC[round][i * 4+2][0], DC[round][i * 4+3][0], SB[round][i * 4][0], SB[round][i * 4+1][0], SB[round][i * 4+2][0], SB[round][i * 4+3][0]), GRB.LESS_EQUAL, 4, "");
                //model.addConstr(linExprOf(DC[round][i * 4][1], DC[round][i * 4+1][1], DC[round][i * 4+2][1], DC[round][i * 4+3][0], SB[round][i * 4][1], SB[round][i * 4+1][1], SB[round][i * 4+2][1], SB[round][i * 4+3][1]), GRB.LESS_EQUAL, 4, "");

                //DC - numred - allgray <= -1
                model.addConstr(linExprOf(t2,DC[round][i * 4][0], DC[round][i * 4+1][0], DC[round][i * 4+2][0], DC[round][i * 4+3][0], SB[round][i * 4][0], SB[round][i * 4+1][0], SB[round][i * 4+2][0], SB[round][i * 4+3][0],AllOne[round][i][0]), GRB.LESS_EQUAL, 3, "");
                model.addConstr(linExprOf(t2,DC[round][i * 4][1], DC[round][i * 4+1][1], DC[round][i * 4+2][1], DC[round][i * 4+3][1], SB[round][i * 4][1], SB[round][i * 4+1][1], SB[round][i * 4+2][1], SB[round][i * 4+3][1],AllOne[round][i][1]), GRB.LESS_EQUAL, 3, "");
            }
        }
    }

    public void addPermutation(GRBVar[][][] A, GRBVar[][][]SB, int Block) throws GRBException {
        int Rounds = SB.length;

        if (Block == 88) {
            for (int round = 0; round < Rounds; round++) {
                for (int i = 0; i < Block; i++) {
                    for (int k = 0; k < 2; k++) {
                        model.addConstr(SB[round][i][k], GRB.EQUAL, A[round+1][permutation_f_88[i]][k], "");
                    }
                }
            }
        }

        if (Block == 176) {
            for (int round = 0; round < Rounds; round++) {
                for (int i = 0; i < Block; i++) {
                    for (int k = 0; k < 2; k++) {
                        model.addConstr(SB[round][i][k], GRB.EQUAL, A[round+1][permutation_f_176[i]][k], "");
                    }
                }
            }
        }

        if (Block == 240) {
            for (int round = 0; round < Rounds; round++) {
                for (int i = 0; i < Block; i++) {
                    for (int k = 0; k < 2; k++) {
                        model.addConstr(SB[round][i][k], GRB.EQUAL, A[round+1][permutation_f_240[i]][k], "");
                    }
                }
            }
        }

        if(Block == 272){
            for (int round = 0; round < Rounds; round++) {
                for (int i = 0; i < Block; i++) {
                    for (int k = 0; k < 2; k++) {
                        model.addConstr(SB[round][i][k], GRB.EQUAL, A[round+1][permutation_f_272[i]][k], "");
                    }
                }
            }
        }

        if (Block == 336) {
            for (int round = 0; round < Rounds; round++) {
                for (int i = 0; i < Block; i++) {
                    for (int k = 0; k < 2; k++) {
                        model.addConstr(SB[round][i][k], GRB.EQUAL, A[round+1][permutation_f_336[i]][k], "");
                    }
                }
            }
        }

        if (Block == 384) {
            for (int round = 0; round < Rounds; round++) {
                for (int i = 0; i < Block; i++) {
                    for (int k = 0; k < 2; k++) {
                        model.addConstr(SB[round][i][k], GRB.EQUAL, A[round+1][permutation_f_384[i]][k], "");
                    }
                }
            }
        }
    }

    public void addPermutation_inv(GRBVar[][][] A, GRBVar[][][]SB, int Block) throws GRBException {
        int Rounds = SB.length;

        if (Block == 88) {
            for (int round = 0; round < Rounds; round++) {
                for (int i = 0; i < Block; i++) {
                    for (int k = 0; k < 2; k++) {
                        model.addConstr(SB[round][i][k], GRB.EQUAL, A[round][permutation_f_88[i]][k], "");
                    }
                }
            }
        }

        if (Block == 176) {
            for (int round = 0; round < Rounds; round++) {
                for (int i = 0; i < Block; i++) {
                    for (int k = 0; k < 2; k++) {
                        model.addConstr(SB[round][i][k], GRB.EQUAL, A[round][permutation_f_176[i]][k], "");
                    }
                }
            }
        }

        if (Block == 240) {
            for (int round = 0; round < Rounds; round++) {
                for (int i = 0; i < Block; i++) {
                    for (int k = 0; k < 2; k++) {
                        model.addConstr(SB[round][i][k], GRB.EQUAL, A[round][permutation_f_240[i]][k], "");
                    }
                }
            }
        }

        if(Block == 272){
            for (int round = 0; round < Rounds; round++) {
                for (int i = 0; i < Block; i++) {
                    for (int k = 0; k < 2; k++) {
                        model.addConstr(SB[round][i][k], GRB.EQUAL, A[round][permutation_f_272[i]][k], "");
                    }
                }
            }
        }

        if (Block == 336) {
            for (int round = 0; round < Rounds; round++) {
                for (int i = 0; i < Block; i++) {
                    for (int k = 0; k < 2; k++) {
                        model.addConstr(SB[round][i][k], GRB.EQUAL, A[round][permutation_f_336[i]][k], "");
                    }
                }
            }
        }

        if (Block == 384) {
            for (int round = 0; round < Rounds; round++) {
                for (int i = 0; i < Block; i++) {
                    for (int k = 0; k < 2; k++) {
                        model.addConstr(SB[round][i][k], GRB.EQUAL, A[round][permutation_f_384[i]][k], "");
                    }
                }
            }
        }
    }

    public void addMatch(GRBVar[][][] Af,GRBVar[] dom,int Rate,int Block) throws GRBException {
        int Rounds = Af.length-1;

        for (int i = 0; i < Rate; i++) {
            model.addConstr(dom[i], GRB.EQUAL, 0, "");
        }

        for (int i = Rate; i < Block; i++) {
            Determine_ExistOne(dom[i], Af[Rounds][i][0], Af[Rounds][i][1]);
        }
        
    }

    public void add_FB_Match(GRBVar[][][] Af, GRBVar[][][] Ab, GRBVar[] dom_f,GRBVar[] dom_b,int Rate,int Block) throws GRBException {
        int Rounds = Af.length-1;

        for (int i = 0; i < Rate; i++) {
            Determine_ExistOne(dom_f[i], Af[Rounds][i][0], Af[Rounds][i][1]);
            Determine_ExistOne(dom_b[i], Ab[Rounds][i][0], Ab[Rounds][i][1]);
        }

        for (int i = Rate; i < Block; i++) {
            model.addConstr(dom_f[i], GRB.EQUAL, 0, "");
            model.addConstr(dom_b[i], GRB.EQUAL, 0, "");
        }

    }

    public void add_FB_Match_new(GRBVar[][][] Af, GRBVar[][][] SBb, GRBVar[] dom_f,GRBVar[] dom_b,int Rate,int Block) throws GRBException {
        //forward
        int Rounds = Af.length-1;
        GRBVar[][] NoWhite_f = new GRBVar[Rate][4];
        for (int i = 0; i < Rate; i++)
            for (int l = 0; l < 4; l++) {
                NoWhite_f[i][l] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "NoWhite_S_f_"+i+"_"+l);
            }

        for (int i = 0; i < Rate; i++) {
            for (int j = 0; j < 4; j++) {
                Determine_ExistOne(NoWhite_f[i][j],Af[Rounds][4*i+j][0],Af[Rounds][4*i+j][1]);
            }
            Determine_AllOne(dom_f[i],NoWhite_f[i][0],NoWhite_f[i][1],NoWhite_f[i][2],NoWhite_f[i][3]);
        }

        //backward
        GRBVar[][] NoWhite_b = new GRBVar[Rate][4];
        for (int i = 0; i < Rate; i++)
            for (int l = 0; l < 4; l++) {
                NoWhite_b[i][l] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "NoWhite_S_b_"+i+"_"+l);
            }
        Rounds = SBb.length-2;
        for (int i = 0; i < Rate; i++) {
            int tmp = (i * Block / 4) % (Block - 1);
            int BitIndex = (tmp * Block / 4) % (Block - 1);
            int SboxIndex = BitIndex/4;
            for (int j = 0; j < 4; j++) {
                Determine_ExistOne(NoWhite_b[i][j],SBb[Rounds][4*SboxIndex+j][0],SBb[Rounds][4*SboxIndex+j][1]);
            }
            Determine_AllOne(dom_b[i],NoWhite_b[i][0],NoWhite_b[i][1],NoWhite_b[i][2],NoWhite_b[i][3]);
        }

        for (int i = Rate; i < Block; i++) {
            model.addConstr(dom_f[i], GRB.EQUAL, 0, "");
            model.addConstr(dom_b[i], GRB.EQUAL, 0, "");
        }

    }


    public void add_FB_Match_new_88(GRBVar[][][] Af, GRBVar[][][] SBb, GRBVar[] dom_f,GRBVar[] dom_b,int Rate,int Block) throws GRBException {
        //forward
        int Rounds = Af.length-1;
        GRBVar[][] NoWhite_f = new GRBVar[Rate][4];
        for (int i = 0; i < Rate; i++)
            for (int l = 0; l < 4; l++) {
                NoWhite_f[i][l] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "NoWhite_S_f_"+i+"_"+l);
            }

        for (int i = 0; i < Rate; i++) {
            for (int j = 0; j < 4; j++) {
                Determine_ExistOne(NoWhite_f[i][j],Af[Rounds][4*i+j][0],Af[Rounds][4*i+j][1]);
            }
            Determine_AllOne(dom_f[i],NoWhite_f[i][0],NoWhite_f[i][1],NoWhite_f[i][2],NoWhite_f[i][3]);
        }

        //backward
        GRBVar[][] NoWhite_b = new GRBVar[Rate][4];
        GRBVar[] TwoWhite_b = new GRBVar[Rate];
        for (int i = 0; i < Rate; i++) {
            TwoWhite_b[i] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "TwoWhite_S_b_"+i);
            for (int l = 0; l < 4; l++) {
                NoWhite_b[i][l] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "NoWhite_S_b_" + i + "_" + l);
            }
        }
        Rounds = SBb.length-1;
        for (int i = 0; i < Rate/2; i++) {
            int BitIndex = (i * Block / 4) % (Block - 1);
            //int BitIndex = (tmp * Block / 4) % (Block - 1);
            int SboxIndex = BitIndex/4;
            for (int j = 0; j < 4; j++) {
                Determine_ExistOne(NoWhite_b[i][j],SBb[Rounds][4*SboxIndex+j][0],SBb[Rounds][4*SboxIndex+j][1]);
            }
            Determine_twoinfour(TwoWhite_b[i],NoWhite_b[i][0],NoWhite_b[i][1],NoWhite_b[i][2],NoWhite_b[i][3]);
            double[] t1 = {1, 1};
            model.addConstr(linExprOf(t1, dom_b[i], TwoWhite_b[i]), GRB.EQUAL, 1, "");
        }

        for (int i = Rate/2; i < Rate; i++) {
            int BitIndex = (i * Block / 4) % (Block - 1);
            //int BitIndex = (tmp * Block / 4) % (Block - 1);
            int SboxIndex = BitIndex/4;
            for (int j = 0; j < 4; j++) {
                Determine_ExistOne(NoWhite_b[i][j],SBb[Rounds][4*SboxIndex+j][0],SBb[Rounds][4*SboxIndex+j][1]);
            }
            Determine_AllOne(dom_b[i],NoWhite_b[i][0],NoWhite_b[i][1],NoWhite_b[i][2],NoWhite_b[i][3]);
        }

        for (int i = Rate; i < Block; i++) {
            model.addConstr(dom_f[i], GRB.EQUAL, 0, "");
            model.addConstr(dom_b[i], GRB.EQUAL, 0, "");
        }

    }

    public void addAtoSB(GRBVar[][][][][] A, GRBVar[][][][][] SB, int r) throws GRBException {
	//SB
	double[][] t = {{0, 0, 0, 0, -1, 1, 0},
			{0, 1, 0, 0, 0, -1, 0},
			{0, 0, 0, 0, 0, 1, -1},
			{0, 0, 1, 0, 0, -1, 0},
			{-1, -1, -1, 0, 1, 0, 0},
			{1, 0, 0, 0, -1, 0, 0},
			{0, -1, -1, -1, 0, 0, 1},
			{0, 0, 0, 1, 0, 0, -1},
			{0, 0, 0, 0, 1, -1, 1}};
	double[] c = {0, 0, 0, 0, -2, 0, -2, 0, 0};
	//for (int r = 0; r < Rounds; r++)
            for (int i = 0; i < 16; i++)
          	for (int j = 0; j < 4; j++) {
		    for (int q = 0; q < 9; q++) {
			model.addConstr(linExprOf(t[q], A[r][(i%4+j)%4+(i/4)*4][j][0][0], A[r][(i%4+j)%4+(i/4)*4][j][0][1], A[r][(i%4+j)%4+(i/4)*4][j][1][0], A[r][(i%4+j)%4+(i/4)*4][j][1][1], SB[r][i][j][0][0], SB[r][i][j][0][1], SB[r][i][j][1][1]), GRB.GREATER_EQUAL, c[q], "");	
		    }	
		    model.addConstr(SB[r][i][j][0][1], GRB.EQUAL, SB[r][i][j][1][0], "");	
	}
    }

    public void addSBtoA(GRBVar[][][][][] A, GRBVar[][][][][] SB, int r) throws GRBException {
	
	double[][] t = {{0, 0, 0, 0, -1, 1, 0},
			{0, 1, 0, 0, 0, -1, 0},
			{0, 0, 0, 0, 0, 1, -1},
			{0, 0, 1, 0, 0, -1, 0},
			{-1, -1, -1, 0, 1, 0, 0},
			{1, 0, 0, 0, -1, 0, 0},
			{0, -1, -1, -1, 0, 0, 1},
			{0, 0, 0, 1, 0, 0, -1},
			{0, 0, 0, 0, 1, -1, 1}};
	double[] c = {0, 0, 0, 0, -2, 0, -2, 0, 0};
	//for (int r = 0; r < Rounds; r++)
            for (int i = 0; i < 16; i++)
          	for (int j = 0; j < 4; j++) {
		    for (int q = 0; q < 9; q++) {
			model.addConstr(linExprOf(t[q], SB[r][i][j][0][0], SB[r][i][j][0][1], SB[r][i][j][1][0], SB[r][i][j][1][1], A[r+1][(i%4+j)%4+(i/4)*4][j][0][0], A[r+1][(i%4+j)%4+(i/4)*4][j][0][1], A[r+1][(i%4+j)%4+(i/4)*4][j][1][1]), GRB.GREATER_EQUAL, c[q], "");	
		    }	
		    model.addConstr(A[r+1][(i%4+j)%4+(i/4)*4][j][0][1], GRB.EQUAL, A[r+1][(i%4+j)%4+(i/4)*4][j][1][0], "");	
	}
    }

    public void Determine_consume(GRBVar AllOne, GRBVar Out, GRBVar DC) throws GRBException {
        model.addConstr(DC, GRB.LESS_EQUAL, Out, "");
        double[][] t = {{1.0, -1.0, 1.0},{1.0, 1.0, 1.0}};
        model.addConstr(linExprOf(t[0], DC, Out, AllOne), GRB.GREATER_EQUAL, 0, "");
        model.addConstr(linExprOf(t[1], DC, Out, AllOne), GRB.LESS_EQUAL, 2, "");
    }

    public void XOR_red(GRBVar mainVar, GRBVar ... vars) throws GRBException {
      GRBLinExpr expr = new GRBLinExpr();
      expr.addTerm(1.0, mainVar);
        for (int i = 0; i < vars.length; i++) {
          expr.addTerm(1.0, vars[i]);
	        model.addConstr(linExprOf(mainVar,vars[i]), GRB.LESS_EQUAL, 1, "");
        }
        model.addConstr(expr, GRB.GREATER_EQUAL, 1, "");
    }

    public void Determine_twoinfour(GRBVar mainVar, GRBVar ... vars) throws GRBException {
        GRBLinExpr expr1 = new GRBLinExpr();
        GRBLinExpr expr2 = new GRBLinExpr();
        expr1.addTerm(3.0, mainVar);
        expr2.addTerm(-2.0, mainVar);
        for (int i = 0; i < vars.length; i++) {
            expr1.addTerm(1.0, vars[i]);
            expr2.addTerm(-1.0, vars[i]);
        }
        model.addConstr(expr1, GRB.GREATER_EQUAL, 3, "");
        model.addConstr(expr2, GRB.GREATER_EQUAL, -4, "");
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

    public void Determine_Existzero(GRBVar mainVar, GRBVar ... vars) throws GRBException {
      GRBLinExpr expr = new GRBLinExpr();
      expr.addTerm(1.0, mainVar);
      for (int i = 0; i < vars.length; i++) {
          expr.addTerm(1.0, vars[i]);
          model.addConstr(linExprOf(mainVar,vars[i]), GRB.GREATER_EQUAL, 1, "");
      }
      model.addConstr(expr, GRB.LESS_EQUAL, vars.length, "");
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
