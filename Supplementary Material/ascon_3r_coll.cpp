//
//  main.cpp
//  2r experiments on Ascon-XoF
//  
//  Created on 2022/9/3.
//

#include <iostream>
#include <cstdlib>
#include <ctime>
#include <map>
#include <cmath>
#include <vector>

using namespace std;

typedef unsigned char UINT8;
typedef unsigned long int UINT32;
typedef unsigned long long int UINT64;


#define random(x) (rand())%x;
#define nrRounds 2
#define nrLanes 5

#define ROL64(a, offset) ((offset != 0) ? ((((UINT64)a) << offset) ^ (((UINT64)a) >> (64-offset))) : a)
#define ROR64(a, offset) ((offset != 0) ? ((((UINT64)a) >> offset) ^ (((UINT64)a) << (64-offset))) : a)
#define TAKE_BIT(x, pos) (((x) >> (pos)) & 0x1)

unsigned int indexm[6]={9,22,31,41,54,63};
unsigned int indexb[24]={1,4,7,10,11,14,19,21,23,26,29,30,  33,36,39,42,43,46,51,53,55,58,61,62};
unsigned int indexr[36]={0,2,3,5,6,9,12,13,15,16,18,20,22,24,25,27,28,31, 32,34,35,37,38,41,44,45,47,48,50,52,54,56,57,59,60,63};

unsigned int cond0x[8]={5,7,12,21,23,27,30,31};
//unsigned int cond0y[8]={1,1,1,1,1,1,1,1};
unsigned int cond1x[5]={4,6,10,19,26};
//unsigned int cond1y[5]={1,1,1,1,1};
unsigned int condplusx[3]={7,14,21};

void PermutationOnWords(UINT64 *state);
void pL(UINT64 *A);
void pS(UINT64 *A);


void PermutationOnWords(UINT64 state[], int round)
{
    unsigned int i;
    for(i=0; i<round; i++) {
        pS(state);
        pL(state);       
    }
}


void pL(UINT64 *A)
{
    A[0] = A[0] ^ ROR64(A[0], 19) ^ ROR64(A[0], 28);
    A[1] = A[1] ^ ROR64(A[1], 61) ^ ROR64(A[1], 39);
    A[2] = A[2] ^ ROR64(A[2], 1) ^ ROR64(A[2], 6);
    A[3] = A[3] ^ ROR64(A[3], 10) ^ ROR64(A[3], 17);
    A[4] = A[4] ^ ROR64(A[4], 7) ^ ROR64(A[4], 41);
}


void pS(UINT64 *A)
{
    unsigned int x, y;
    UINT64 C[5];

    C[0] = (A[4]&A[1]) ^ (A[3]) ^ (A[2]&A[1]) ^ (A[2]) ^ (A[1]&A[0]) ^ (A[1]) ^ (A[0]);
    C[1] = (A[4]) ^ (A[3]&A[2]) ^ (A[3]&A[1]) ^ (A[3]) ^ (A[2]&A[1]) ^ (A[2]) ^ (A[1]) ^ (A[0]);
    C[2] = (A[4]&A[3]) ^ (A[4]) ^ (A[2]) ^ (A[1]) ^ (0xFFFFFFFFFFFFFFFF);
    C[3] = (A[4]&A[0]) ^ (A[4]) ^ (A[3]&A[0]) ^ (A[3]) ^ (A[2])^ (A[1]) ^ (A[0]);
    C[4] = (A[4]&A[1]) ^ (A[4]) ^ (A[3]) ^ (A[1]&A[0])^ (A[1]);
    for(x=0; x<5; x++)
        A[x] = C[x];  
}


void displaystate(UINT64 *state)
{
    unsigned int i;
    for(i=0;i<5;i++)
    {
        printf("%016llx ",(state[i]));
        printf("\n");
    }
    printf("\n");
}

UINT64 generateMP(UINT64 *state)
{
    UINT64 MP=0;
    for (int i=0; i<6; i++) {
        MP = (MP << 1) ^ TAKE_BIT(state[0], (63-indexm[i]));
        MP = (MP << 1) ^ TAKE_BIT(state[1], (63-indexm[i])) ^ TAKE_BIT(state[2], (63-indexm[i]));
        MP = (MP << 1) ^ TAKE_BIT(state[3], (63-indexm[i]));
        MP = (MP << 1) ^ TAKE_BIT(state[4], (63-indexm[i]));
    }
    return MP;
}

UINT64 generateConsume(UINT64 *state)
{
    UINT64 Consume=0;
    Consume = (Consume << 1) ^ TAKE_BIT(state[1], (63-3));
    Consume = (Consume << 1) ^ TAKE_BIT(state[3], (63-3));
    Consume = (Consume << 1) ^ TAKE_BIT(state[1], (63-13));
    Consume = (Consume << 1) ^ TAKE_BIT(state[0], (63-14));
    Consume = (Consume << 1) ^ TAKE_BIT(state[1], (63-15));
    Consume = (Consume << 1) ^ TAKE_BIT(state[0], (63-21));

    Consume = (Consume << 1) ^ TAKE_BIT(state[1], (63-35));
    Consume = (Consume << 1) ^ TAKE_BIT(state[3], (63-35));
    Consume = (Consume << 1) ^ TAKE_BIT(state[1], (63-45));
    Consume = (Consume << 1) ^ TAKE_BIT(state[0], (63-46));
    Consume = (Consume << 1) ^ TAKE_BIT(state[1], (63-47));
    Consume = (Consume << 1) ^ TAKE_BIT(state[0], (63-53));
    return Consume;
}

int main(int argc, const char * argv[])
{
	clock_t start_time = clock();
    srand((unsigned)time(NULL));
    
    UINT64 InitialState[5]={0};
    UINT64 TempState[5]={0};
    UINT64 MatchNum = 0;
    UINT64 SucceedNum = 0;
    UINT64 outputNum = 0;
    
    UINT64 tmpCount = 0;
    
	UINT64 consuCount = 0;

    
    //Init the state with 0
    for(UINT64 i=0;i<5;i++){
        InitialState[i]=0;
    }

    //Set condition gray bits
    for(UINT64 j=0;j<5;j++){
        InitialState[1] |= (UINT64(1)<<(63-cond1x[j]));
        InitialState[1] |= (UINT64(1)<<(63-cond1x[j]-32));
    }
    for(UINT64 j=0;j<3;j++){
        InitialState[3] |= (UINT64(1)<<(63-condplusx[j]));
        InitialState[3] |= (UINT64(1)<<(63-condplusx[j]-32));
    }

    map<UINT64, vector<UINT64>> TableL1;
    
    map<UINT64, vector<UINT64>> TableLConsume;
    //Generate table L1
    UINT64 Statered = 0;
    UINT64 Fixred_e = 0;
    UINT64 FM3 = 0;
    //cout << "Start Build L1!\n";
    for(UINT64 j=0;j<(UINT64(1)<<36);j++){
        for(UINT64 k=0;k<5;k++){
                TempState[k]=InitialState[k];
        }
    
        //Set blue=0
        for(UINT64 k=0;k<24;k++){
            TempState[0] &= ROL64(~UINT64(1),(63-indexb[k]));
        }

        //Traverse the red bits
        for(UINT64 k=0;k<36;k++) {
            UINT64 temp1=(j>>(35-k))&1;
            if(temp1){
                TempState[0] |= (UINT64(1)<<(63-indexr[k]));
            }
            else {
                TempState[0] &= ROL64(~UINT64(1),(63-indexr[k]));
            }                   
        }
        Statered = TempState[0];
        	
        PermutationOnWords(TempState, 1);
        UINT64 tmpConsume=generateConsume(TempState);
	
	
        if(tmpConsume == 0){
            //tmpCount+=1;
            PermutationOnWords(TempState, 1);
            UINT64 tmpMatch = generateMP(TempState);
            if(TableL1.find(tmpMatch) != TableL1.end())
            {
                vector<UINT64> ttmp =TableL1[tmpMatch];
                ttmp.push_back(Statered);
                TableL1[tmpMatch] = ttmp;
            } else {
                vector<UINT64> ttmp;
                ttmp.push_back(Statered);
                TableL1[tmpMatch] = ttmp;
            }
            Fixred_e =  Statered;
            FM3 = tmpMatch;
        }
         
    }
 
    cout << "Finish Build L1!\n";
    //cout << "Size of L1 is " << tmpCount <<endl;
    //cout << "consume count is " << consuCount <<endl;
    
    //Fix Red to e, traverse the blue bits
    for(UINT64 j=0; j<(UINT64(1)<<24); j++){
        UINT64 Stateblue = 0;
        for(UINT64 k=0; k<5; k++){
            TempState[k]=InitialState[k];
        }
        //Set red=Fixred_e
        TempState[0] = Fixred_e;
        
        for(UINT64 k=0;k<24;k++)
        {
            UINT64 temp1=(j>>(23-k))&1;
            if(temp1){
                TempState[0] |= (UINT64(1)<<(63-indexb[k]));
                Stateblue |= (UINT64(1)<<(63-indexb[k]));
            }
            else{
                TempState[0] &= ROL64(~UINT64(1),(63-indexb[k]));
                Stateblue &= ROL64(~UINT64(1),(63-indexb[k]));
            }                   
        }

        PermutationOnWords(TempState, 2);
    
        UINT64 Matchfblue = 0;
        //Compute fb
        Matchfblue = generateMP(TempState) ^ FM3;

        UINT64 tmpM = 0;
        for(UINT64 k=0; k<6; k++){
            tmpM |= (UINT64(3)<<(4*k+2));
        }
      
        Matchfblue = Matchfblue ^ tmpM;

        if(TableL1.find(Matchfblue) != TableL1.end()) {
            vector<UINT64> ttmp = TableL1[Matchfblue];
            MatchNum += ttmp.size();

            //verify
            for (UINT64 k=0; k<ttmp.size(); k++) {
                UINT64 Message = ttmp[k]^Stateblue;
                TempState[0] = Message;
                for(int i=1; i<5; i++){
                    TempState[i]=InitialState[i];
                }
                
                PermutationOnWords(TempState, 2);  
                UINT64 hash = generateMP(TempState);
                if (hash==tmpM) {
                    SucceedNum += 1;
                    //if((outputNum<500)&(j>(UINT64(1)<<18))){
                    //outputNum+=1;
                    //printf("%016llx \n",(Message)); 
                //}
                }   
            }

        }
    }
    clock_t end_time = clock();

    cout << "The run time is: " <<(double)(end_time - start_time) / CLOCKS_PER_SEC << "s" << endl;

    cout << "In total, 2^" << log(double(MatchNum))/log(2.0) << " preimage are found!" << endl;
    //cout << "In total, 2^" << log(double(SucceedNum))/log(2.0) << " success are found!" << endl;
    return 0;

}


