module remote(input wire w,clk,rst, output reg [2:0]state_output);

parameter A=5'b00000,B=5'b00001,C=5'b00010,D=5'b00011,E=5'b00100,F=5'b00101,G=5'b00110,
H=5'b00111,I=5'b01000,J=5'b01001,K=5'b01010,L=5'b01011,M=5'b01100,N=5'b01101,O=5'b01110,
P=5'b01111,Q=5'b10000,R=5'b10001,S=5'b10010,T=5'b10011,U=5'b10100,V=5'b10101,W=5'b10110;

reg[4:0]state;
reg[4:0]next_state;
reg [12:0]counter0; //to make 6.1036kHz (163.84usec) clock
reg [7:0]counter1;  //(to skip address signal)
reg[27:0]shiftreg;  //28bit shift register
wire modclock; //modclock is 6.1035kHz, 163.84 usecond clock
wire modclock2;//modclock2 detects the end of address signal                    
wire z1,z2,z3,z4;//detect logic 1, logic 0, leading pulse and space



//163.84usec clock, 3.4 clock cycle can be inside the 562.25 us
always @(posedge clk, negedge rst) //163.84*3.4 = 562.25 usec 
if(~rst) counter0<=13'b0;      //13bit means 163.84 usec
else counter0<=counter0+13'b1;//'~|counter0' becomes 163.84 usecond clock

assign modclock=~|counter0;//modclock is 6.1035kHz, 163.84 usecond clock



//sample the IRDA_RXD signal "at least 3 times" to detect the low or high
always@(posedge modclock, negedge rst)
if(~rst) shiftreg<=28'b0000000000000000000010101010;
else shiftreg<={shiftreg[26:0],w};  //w becomes IRDA_RXD signal

//z1 detects 000 of w at the begining of signal and outputs 1
assign z1=/*(~shiftreg[2])*/&(~shiftreg[1])&(~shiftreg[0]);

//z2 detects 111 of w at the begining of signal and outputs 1
assign z2=/*(shiftreg[2])*/&(shiftreg[1])&(shiftreg[0]); 

//z3 detects 111111111 of w and outputs 1(for detect logic 1)
assign z3=&shiftreg[0]&shiftreg[1]&shiftreg[2]&shiftreg[3]&shiftreg[4]&shiftreg[5]
&shiftreg[6]/*&shiftreg[7]&shiftreg[8]*/;

//z4 detects 26times of 1 and outputs 1(to detect the space signal)
assign z4=(&shiftreg[0]&shiftreg[1]&shiftreg[2]&shiftreg[3]&shiftreg[4]&shiftreg[5]&shiftreg[6]
&shiftreg[7]&shiftreg[8]&shiftreg[9]&shiftreg[10]&shiftreg[11]&shiftreg[12]&shiftreg[13]&shiftreg[14]
&shiftreg[15]&shiftreg[16]&shiftreg[17]&shiftreg[18]&shiftreg[19]&shiftreg[20]&shiftreg[21]&shiftreg[22]
&shiftreg[23]&shiftreg[24]&shiftreg[25])
|(&(~shiftreg));//or all the shiftregister becomes 0s(to detect the leading pulse)



//counter1(to skip address signal)
always@(posedge modclock, negedge rst) //modclock is 163.84 usecond clock
if(~rst) counter1<=8'b0;
else if(state_output==3'b110) counter1<=counter1+1'b1;
else if(state_output==3'b111) counter1<=8'b0;
//111 is state_output of the first state of command signal
//110 is state_output of the last state of space signal

assign modclock2=(counter1>=8'd167);//when address signal over, outputs 1



//FSM
//register logic
always@(posedge clk,negedge rst)  
if(~rst) state<=A;
else state<=next_state;
//output logic
always@*
case(state)
A:state_output = 3'b000;
B:state_output = 3'b000;
C:state_output = 3'b000;
D:state_output = 3'b000; //first state of command signal
E:state_output = 3'b111;
F:state_output = 3'b000;
G:state_output = 3'b000;
H:state_output = 3'b000;
I:state_output = 3'b001; //speed down state
J:state_output = 3'b001; //speed down state
K:state_output = 3'b010; //speed up state
L:state_output = 3'b010; //speed up state
M:state_output = 3'b000;
N:state_output = 3'b000;
O:state_output = 3'b000;
P:state_output = 3'b000;
Q:state_output = 3'b011; //animation 1 state
R:state_output = 3'b011; //animation 1 state
S:state_output = 3'b100; //animation 2 state
T:state_output = 3'b100; //animation 2 state
U:state_output = 3'b101; //animation 3 state
V:state_output = 3'b101; //animation 3 state
W:state_output = 3'b110; //last state of space
default: state_output = 3'b000;
endcase
//next_state logic
always@*
casex({state,z1,z2,z3,z4,modclock2})
{A,1'b0,1'b0,1'b0,1'bx,1'bx}:next_state=A; //00000
{A,1'b0,1'b0,1'b1,1'bx,1'bx}:next_state=A;
{A,1'b0,1'b1,1'b0,1'bx,1'bx}:next_state=A;
{A,1'b0,1'b1,1'b1,1'bx,1'bx}:next_state=A;
{A,1'b1,1'b0,1'b0,1'b1,1'bx}:next_state=B; 
{A,1'b1,1'b0,1'b0,1'b0,1'bx}:next_state=A; 
{A,1'b1,1'b0,1'b1,1'bx,1'bx}:next_state=A;
{A,1'b1,1'b1,1'b0,1'bx,1'bx}:next_state=A;
{A,1'b1,1'b1,1'b1,1'bx,1'bx}:next_state=A;
{B,1'b0,1'b0,1'b0,1'bx,1'bx}:next_state=B; //00001
{B,1'b0,1'b0,1'b1,1'bx,1'bx}:next_state=B;
{B,1'b0,1'b1,1'b0,1'bx,1'bx}:next_state=C; 
{B,1'b0,1'b1,1'b1,1'bx,1'bx}:next_state=B;
{B,1'b1,1'b0,1'b0,1'bx,1'bx}:next_state=B;
{B,1'b1,1'b0,1'b1,1'bx,1'bx}:next_state=B;
{B,1'b1,1'b1,1'b0,1'bx,1'bx}:next_state=B;
{B,1'b1,1'b1,1'b1,1'bx,1'bx}:next_state=B;
{C,1'b0,1'b0,1'b0,1'bx,1'bx}:next_state=C; //00010
{C,1'b0,1'b0,1'b1,1'bx,1'bx}:next_state=C;
{C,1'b0,1'b1,1'b0,1'bx,1'bx}:next_state=C; 
{C,1'b0,1'b1,1'b1,1'b1,1'bx}:next_state=W;
{C,1'b0,1'b1,1'b1,1'b0,1'bx}:next_state=C;
{C,1'b1,1'b0,1'b0,1'bx,1'bx}:next_state=C;
{C,1'b1,1'b0,1'b1,1'bx,1'bx}:next_state=C;
{C,1'b1,1'b1,1'b0,1'bx,1'bx}:next_state=C;
{C,1'b1,1'b1,1'b1,1'bx,1'bx}:next_state=C;
{W,1'b0,1'b0,1'b0,1'bx,1'bx}:next_state=W; //10110
{W,1'b0,1'b0,1'b1,1'bx,1'bx}:next_state=W;
{W,1'b0,1'b1,1'b0,1'bx,1'bx}:next_state=W; 
{W,1'b0,1'b1,1'b1,1'bx,1'bx}:next_state=W;
{W,1'b1,1'b0,1'b0,1'bx,1'b1}:next_state=D;
{W,1'b1,1'b0,1'b0,1'bx,1'b0}:next_state=W;
{W,1'b1,1'b0,1'b1,1'bx,1'bx}:next_state=W;
{W,1'b1,1'b1,1'b0,1'bx,1'bx}:next_state=W;
{W,1'b1,1'b1,1'b1,1'bx,1'bx}:next_state=W;
{D,1'b0,1'b0,1'b0,1'bx,1'bx}:next_state=D; //00011
{D,1'b0,1'b0,1'b1,1'bx,1'bx}:next_state=D;
{D,1'b0,1'b1,1'b0,1'bx,1'bx}:next_state=E; 
{D,1'b0,1'b1,1'b1,1'bx,1'bx}:next_state=D;
{D,1'b1,1'b0,1'b0,1'bx,1'bx}:next_state=D;
{D,1'b1,1'b0,1'b1,1'bx,1'bx}:next_state=D;
{D,1'b1,1'b1,1'b0,1'bx,1'bx}:next_state=D;
{D,1'b1,1'b1,1'b1,1'bx,1'bx}:next_state=D;
{E,1'b0,1'b0,1'b0,1'bx,1'bx}:next_state=E; //00100
{E,1'b0,1'b0,1'b1,1'bx,1'bx}:next_state=E;
{E,1'b0,1'b1,1'b0,1'bx,1'bx}:next_state=E; 
{E,1'b0,1'b1,1'b1,1'bx,1'bx}:next_state=F;
{E,1'b1,1'b0,1'b0,1'bx,1'bx}:next_state=M;
{E,1'b1,1'b0,1'b1,1'bx,1'bx}:next_state=E;
{E,1'b1,1'b1,1'b0,1'bx,1'bx}:next_state=E;
{E,1'b1,1'b1,1'b1,1'bx,1'bx}:next_state=E;
{F,1'b0,1'b0,1'b0,1'bx,1'bx}:next_state=F; //00101
{F,1'b0,1'b0,1'b1,1'bx,1'bx}:next_state=F;
{F,1'b0,1'b1,1'b0,1'bx,1'bx}:next_state=F; 
{F,1'b0,1'b1,1'b1,1'bx,1'bx}:next_state=F;
{F,1'b1,1'b0,1'b0,1'bx,1'bx}:next_state=G;
{F,1'b1,1'b0,1'b1,1'bx,1'bx}:next_state=F;
{F,1'b1,1'b1,1'b0,1'bx,1'bx}:next_state=F;
{F,1'b1,1'b1,1'b1,1'bx,1'bx}:next_state=F;
{G,1'b0,1'b0,1'b0,1'bx,1'bx}:next_state=G; //00110
{G,1'b0,1'b0,1'b1,1'bx,1'bx}:next_state=G;
{G,1'b0,1'b1,1'b0,1'bx,1'bx}:next_state=H; 
{G,1'b0,1'b1,1'b1,1'bx,1'bx}:next_state=G;
{G,1'b1,1'b0,1'b0,1'bx,1'bx}:next_state=G;
{G,1'b1,1'b0,1'b1,1'bx,1'bx}:next_state=G;
{G,1'b1,1'b1,1'b0,1'bx,1'bx}:next_state=G;
{G,1'b1,1'b1,1'b1,1'bx,1'bx}:next_state=G;
{H,1'b0,1'b0,1'b0,1'bx,1'bx}:next_state=H; //00111
{H,1'b0,1'b0,1'b1,1'bx,1'bx}:next_state=H;
{H,1'b0,1'b1,1'b0,1'bx,1'bx}:next_state=H; 
{H,1'b0,1'b1,1'b1,1'bx,1'bx}:next_state=I;
{H,1'b1,1'b0,1'b0,1'bx,1'bx}:next_state=K;
{H,1'b1,1'b0,1'b1,1'bx,1'bx}:next_state=H;
{H,1'b1,1'b1,1'b0,1'bx,1'bx}:next_state=H;
{H,1'b1,1'b1,1'b1,1'bx,1'bx}:next_state=H;
{I,1'b0,1'b0,1'b0,1'bx,1'bx}:next_state=I; //01000
{I,1'b0,1'b0,1'b1,1'bx,1'bx}:next_state=I;
{I,1'b0,1'b1,1'b0,1'bx,1'bx}:next_state=I; 
{I,1'b0,1'b1,1'b1,1'bx,1'bx}:next_state=I;
{I,1'b1,1'b0,1'b0,1'bx,1'bx}:next_state=J;
{I,1'b1,1'b0,1'b1,1'bx,1'bx}:next_state=I;
{I,1'b1,1'b1,1'b0,1'bx,1'bx}:next_state=I;
{I,1'b1,1'b1,1'b1,1'bx,1'bx}:next_state=I;
{I,1'bx,1'bx,1'bx,1'bx,1'bx}:next_state=A;
{J,1'bx,1'bx,1'bx,1'bx,1'bx}:next_state=A; //01001

{K,1'b0,1'b0,1'b0,1'bx,1'bx}:next_state=K; //01010
{K,1'b0,1'b0,1'b1,1'bx,1'bx}:next_state=K;
{K,1'b0,1'b1,1'b0,1'bx,1'bx}:next_state=L; 
{K,1'b0,1'b1,1'b1,1'bx,1'bx}:next_state=K;
{K,1'b1,1'b0,1'b0,1'bx,1'bx}:next_state=K;
{K,1'b1,1'b0,1'b1,1'bx,1'bx}:next_state=K;
{K,1'b1,1'b1,1'b0,1'bx,1'bx}:next_state=K;
{K,1'b1,1'b1,1'b1,1'bx,1'bx}:next_state=K;
{L,1'bx,1'bx,1'bx,1'bx,1'bx}:next_state=A; //01011
{M,1'b0,1'b0,1'b0,1'bx,1'bx}:next_state=M; //01100
{M,1'b0,1'b0,1'b1,1'bx,1'bx}:next_state=M;
{M,1'b0,1'b1,1'b0,1'bx,1'bx}:next_state=N; 
{M,1'b0,1'b1,1'b1,1'bx,1'bx}:next_state=M;
{M,1'b1,1'b0,1'b0,1'bx,1'bx}:next_state=M;
{M,1'b1,1'b0,1'b1,1'bx,1'bx}:next_state=M;
{M,1'b1,1'b1,1'b0,1'bx,1'bx}:next_state=M;
{M,1'b1,1'b1,1'b1,1'bx,1'bx}:next_state=M;
{N,1'b0,1'b0,1'b0,1'bx,1'bx}:next_state=N; //01101
{N,1'b0,1'b0,1'b1,1'bx,1'bx}:next_state=N;
{N,1'b0,1'b1,1'b0,1'bx,1'bx}:next_state=N; 
{N,1'b0,1'b1,1'b1,1'bx,1'bx}:next_state=U;
{N,1'b1,1'b0,1'b0,1'bx,1'bx}:next_state=O;
{N,1'b1,1'b0,1'b1,1'bx,1'bx}:next_state=N;
{N,1'b1,1'b1,1'b0,1'bx,1'bx}:next_state=N;
{N,1'b1,1'b1,1'b1,1'bx,1'bx}:next_state=N;
{O,1'b0,1'b0,1'b0,1'bx,1'bx}:next_state=O; //01110
{O,1'b0,1'b0,1'b1,1'bx,1'bx}:next_state=O;
{O,1'b0,1'b1,1'b0,1'bx,1'bx}:next_state=P; 
{O,1'b0,1'b1,1'b1,1'bx,1'bx}:next_state=O;
{O,1'b1,1'b0,1'b0,1'bx,1'bx}:next_state=O;
{O,1'b1,1'b0,1'b1,1'bx,1'bx}:next_state=O;
{O,1'b1,1'b1,1'b0,1'bx,1'bx}:next_state=O;
{O,1'b1,1'b1,1'b1,1'bx,1'bx}:next_state=O;
{P,1'b0,1'b0,1'b0,1'bx,1'bx}:next_state=P; //01111
{P,1'b0,1'b0,1'b1,1'bx,1'bx}:next_state=P;
{P,1'b0,1'b1,1'b0,1'bx,1'bx}:next_state=P; 
{P,1'b0,1'b1,1'b1,1'bx,1'bx}:next_state=Q;
{P,1'b1,1'b0,1'b0,1'bx,1'bx}:next_state=S;
{P,1'b1,1'b0,1'b1,1'bx,1'bx}:next_state=P;
{P,1'b1,1'b1,1'b0,1'bx,1'bx}:next_state=P;
{P,1'b1,1'b1,1'b1,1'bx,1'bx}:next_state=P;
{Q,1'b0,1'b0,1'b0,1'bx,1'bx}:next_state=Q; //10000
{Q,1'b0,1'b0,1'b1,1'bx,1'bx}:next_state=Q;
{Q,1'b0,1'b1,1'b0,1'bx,1'bx}:next_state=Q; 
{Q,1'b0,1'b1,1'b1,1'bx,1'bx}:next_state=Q;
{Q,1'b1,1'b0,1'b0,1'bx,1'bx}:next_state=R;
{Q,1'b1,1'b0,1'b1,1'bx,1'bx}:next_state=Q;
{Q,1'b1,1'b1,1'b0,1'bx,1'bx}:next_state=Q;
{Q,1'b1,1'b1,1'b1,1'bx,1'bx}:next_state=Q;
{R,1'bx,1'bx,1'bx,1'bx,1'bx}:next_state=A; //10001
{S,1'b0,1'b0,1'b0,1'bx,1'bx}:next_state=S; //10010
{S,1'b0,1'b0,1'b1,1'bx,1'bx}:next_state=S;
{S,1'b0,1'b1,1'b0,1'bx,1'bx}:next_state=T; 
{S,1'b0,1'b1,1'b1,1'bx,1'bx}:next_state=S;
{S,1'b1,1'b0,1'b0,1'bx,1'bx}:next_state=S;
{S,1'b1,1'b0,1'b1,1'bx,1'bx}:next_state=S;
{S,1'b1,1'b1,1'b0,1'bx,1'bx}:next_state=S;
{S,1'b1,1'b1,1'b1,1'bx,1'bx}:next_state=S;
{T,1'bx,1'bx,1'bx,1'bx,1'bx}:next_state=A; //10011
{U,1'b0,1'b0,1'b0,1'bx,1'bx}:next_state=U; //10100
{U,1'b0,1'b0,1'b1,1'bx,1'bx}:next_state=U;
{U,1'b0,1'b1,1'b0,1'bx,1'bx}:next_state=U; 
{U,1'b0,1'b1,1'b1,1'bx,1'bx}:next_state=U;
{U,1'b1,1'b0,1'b0,1'bx,1'bx}:next_state=V;
{U,1'b1,1'b0,1'b1,1'bx,1'bx}:next_state=U;
{U,1'b1,1'b1,1'b0,1'bx,1'bx}:next_state=U;
{U,1'b1,1'b1,1'b1,1'bx,1'bx}:next_state=U;
{V,1'bx,1'bx,1'bx,1'bx,1'bx}:next_state=A; //10101
default:next_state=A;     
endcase

endmodule
