module term(
input wire clk,reset,up,down,   //clk is 50MHz clock
input wire sw1,sw2,sw3,IRDA,
output reg [6:0] hex0,hex1,hex2,hex3,hex4,hex5,
output reg [9:0] led);

parameter A=4'd0,B=4'd1,C=4'd2,D=4'd3,E=4'd4,F=4'd5,G=4'd6,H=4'd7;
reg clock; //modified clock
reg[18:0] counter0; //'~|counter0' becomes 0.01second clock 
reg[4:0] counter_1;//counter for animation 1
reg[4:0] counter_2;//counter for animation 2 
reg[6:0] counter_3;//counter for animation 3
reg[3:0] state;  //speed state
reg[3:0] next_state;
reg[2:0] reg_remote_output; //registered remocon signal
wire[2:0] remote_output;//analyzed remocon signal
wire[9:0] t1led,t2led,t3led;
wire[6:0] t1hex0,t1hex1,t1hex2,t1hex3,t1hex4,t1hex5;
wire[6:0] t2hex0,t2hex1,t2hex2,t2hex3,t2hex4,t2hex5;
wire[6:0] t3hex0,t3hex1,t3hex2,t3hex3,t3hex4,t3hex5;
wire[3:0] counter1,counter2,counter3,counter4,counter5,counter6,counter7,
			 counter8; //counters for modulo_k_counter
wire rollover0,rollover1,rollover2,rollover3,rollover4,rollover5,rollover6,rollover7;
//rollovers for each modulo_k_counter

//remote module analyzes IRDA signal and tells the pressed button
remote G0(.w(IRDA),.clk(clk),.rst(reset),.state_output(remote_output));

always @(posedge clk, negedge reset)
if(~reset) reg_remote_output<=3'b011;
else if((remote_output==3'b011)|(remote_output==3'b100)
|(remote_output==3'b101)) reg_remote_output<=remote_output;

always @(posedge clk, negedge reset)
if(~reset) counter0<=19'b0;//19bit
else counter0<=counter0+19'b1;//'~|counter0' becomes 0.01second clock

//each rollover of modulo_k_counters becomes ...
//0.02s A
modulo_k_counter #(.k(4'd2)) G1
(.reset(reset),.clk(clk), .rollover(rollover0),.counter(counter1),
.en(~|counter0) );
//0.04s B
modulo_k_counter #(.k(4'd2)) G2
(.reset(reset),.clk(clk),.rollover(rollover1),.counter(counter2),
.en((~|counter0)&rollover0));
//0.08s C
modulo_k_counter #(.k(4'd2)) G3
(.reset(reset),.clk(clk),.rollover(rollover2),.counter(counter3),
.en((~|counter0)&rollover0&rollover1));
//0.16s D
modulo_k_counter #(.k(4'd2)) G4
(.reset(reset),.clk(clk),.rollover(rollover3),.counter(counter4),
.en((~|counter0)&rollover0&rollover1&rollover2));
//0.32s E
modulo_k_counter #(.k(4'd2)) G5
(.reset(reset),.clk(clk),.rollover(rollover4),.counter(counter5),
.en((~|counter0)&rollover0&rollover1&rollover2&rollover3));
//0.64s F
modulo_k_counter #(.k(4'd2)) G6
(.reset(reset),.clk(clk),.rollover(rollover5),.counter(counter6),
.en((~|counter0)&rollover0&rollover1&rollover2&rollover3&rollover4));
//1.28s G
modulo_k_counter #(.k(4'd2)) G7
(.reset(reset),.clk(clk),.rollover(rollover6),.counter(counter7),
.en((~|counter0)&rollover0&rollover1&rollover2&rollover3&rollover4&rollover5));
//2.56s H
modulo_k_counter #(.k(4'd2)) G8
(.reset(reset),.clk(clk),.rollover(rollover7),.counter(counter8),
.en((~|counter0)&rollover0&rollover1&rollover2&rollover3&rollover4&rollover5&rollover6));


//clock FSM
//register logic
always@(posedge rollover4,negedge reset)  //rollover4 means 0.32s clock
if(~reset) state<=F;
else state<=next_state;
//output logic
always@*
case(state)
A:clock = rollover0;
B:clock = rollover1;
C:clock = rollover2;
D:clock = rollover3;
E:clock = rollover4;
F:clock = rollover5;
G:clock = rollover6;
H:clock = rollover7;
default: clock = rollover5;
endcase
//next_state logic
always@*
case({state,up,down,remote_output}) //001 speed down, 010 speed up
{A,1'b0,1'b0,3'b000}:next_state=A;
{A,1'b0,1'b0,3'b001}:next_state=B; //speed down
{A,1'b0,1'b0,3'b010}:next_state=A; //speed up
{A,1'b0,1'b0,3'b011}:next_state=A;
{A,1'b0,1'b0,3'b100}:next_state=A;
{A,1'b0,1'b0,3'b101}:next_state=A;
{A,1'b0,1'b0,3'b110}:next_state=A;
{A,1'b0,1'b0,3'b111}:next_state=A;
{A,1'b0,1'b1,3'b000}:next_state=A; //speed up
{A,1'b0,1'b1,3'b001}:next_state=A;
{A,1'b0,1'b1,3'b010}:next_state=A;
{A,1'b0,1'b1,3'b011}:next_state=A;
{A,1'b0,1'b1,3'b100}:next_state=A;
{A,1'b0,1'b1,3'b101}:next_state=A;
{A,1'b0,1'b1,3'b110}:next_state=A;
{A,1'b0,1'b1,3'b111}:next_state=A;
{A,1'b1,1'b0,3'b000}:next_state=B; //speed down
{A,1'b1,1'b0,3'b001}:next_state=B;
{A,1'b1,1'b0,3'b010}:next_state=B;
{A,1'b1,1'b0,3'b011}:next_state=B;
{A,1'b1,1'b0,3'b100}:next_state=B;
{A,1'b1,1'b0,3'b101}:next_state=B;
{A,1'b1,1'b0,3'b110}:next_state=B;
{A,1'b1,1'b0,3'b111}:next_state=B;
{A,1'b1,1'b1,3'b000}:next_state=A;
{A,1'b1,1'b1,3'b001}:next_state=B; //speed down
{A,1'b1,1'b1,3'b010}:next_state=A; //speed up
{A,1'b1,1'b1,3'b011}:next_state=A;
{A,1'b1,1'b1,3'b100}:next_state=A;
{A,1'b1,1'b1,3'b101}:next_state=A;
{A,1'b1,1'b1,3'b110}:next_state=A;
{A,1'b1,1'b1,3'b111}:next_state=A;

{B,1'b0,1'b0,3'b000}:next_state=B;
{B,1'b0,1'b0,3'b001}:next_state=C; //speed down
{B,1'b0,1'b0,3'b010}:next_state=A; //speed up
{B,1'b0,1'b0,3'b011}:next_state=B;
{B,1'b0,1'b0,3'b100}:next_state=B;
{B,1'b0,1'b0,3'b101}:next_state=B;
{B,1'b0,1'b0,3'b110}:next_state=B;
{B,1'b0,1'b0,3'b111}:next_state=B;
{B,1'b0,1'b1,3'b000}:next_state=A; //speed up
{B,1'b0,1'b1,3'b001}:next_state=A;
{B,1'b0,1'b1,3'b010}:next_state=A;
{B,1'b0,1'b1,3'b011}:next_state=A;
{B,1'b0,1'b1,3'b100}:next_state=A;
{B,1'b0,1'b1,3'b101}:next_state=A;
{B,1'b0,1'b1,3'b110}:next_state=A;
{B,1'b0,1'b1,3'b111}:next_state=A;
{B,1'b1,1'b0,3'b000}:next_state=C; //speed down
{B,1'b1,1'b0,3'b001}:next_state=C;
{B,1'b1,1'b0,3'b010}:next_state=C;
{B,1'b1,1'b0,3'b011}:next_state=C;
{B,1'b1,1'b0,3'b100}:next_state=C;
{B,1'b1,1'b0,3'b101}:next_state=C;
{B,1'b1,1'b0,3'b110}:next_state=C;
{B,1'b1,1'b0,3'b111}:next_state=C;
{B,1'b1,1'b1,3'b000}:next_state=B;
{B,1'b1,1'b1,3'b001}:next_state=C; //speed down
{B,1'b1,1'b1,3'b010}:next_state=A; //speed up
{B,1'b1,1'b1,3'b011}:next_state=A;
{B,1'b1,1'b1,3'b100}:next_state=A;
{B,1'b1,1'b1,3'b101}:next_state=A;
{B,1'b1,1'b1,3'b110}:next_state=B;
{B,1'b1,1'b1,3'b111}:next_state=B;

{C,1'b0,1'b0,3'b000}:next_state=C;
{C,1'b0,1'b0,3'b001}:next_state=D; //speed down
{C,1'b0,1'b0,3'b010}:next_state=B; //speed up
{C,1'b0,1'b0,3'b011}:next_state=C;
{C,1'b0,1'b0,3'b100}:next_state=C;
{C,1'b0,1'b0,3'b101}:next_state=C;
{C,1'b0,1'b0,3'b110}:next_state=C;
{C,1'b0,1'b0,3'b111}:next_state=C;
{C,1'b0,1'b1,3'b000}:next_state=B; //speed up
{C,1'b0,1'b1,3'b001}:next_state=B;
{C,1'b0,1'b1,3'b010}:next_state=B;
{C,1'b0,1'b1,3'b011}:next_state=B;
{C,1'b0,1'b1,3'b100}:next_state=B;
{C,1'b0,1'b1,3'b101}:next_state=B;
{C,1'b0,1'b1,3'b110}:next_state=B;
{C,1'b0,1'b1,3'b111}:next_state=B;
{C,1'b1,1'b0,3'b000}:next_state=D; //speed down
{C,1'b1,1'b0,3'b001}:next_state=D;
{C,1'b1,1'b0,3'b010}:next_state=D;
{C,1'b1,1'b0,3'b011}:next_state=D;
{C,1'b1,1'b0,3'b100}:next_state=D;
{C,1'b1,1'b0,3'b101}:next_state=D;
{C,1'b1,1'b0,3'b110}:next_state=D;
{C,1'b1,1'b0,3'b111}:next_state=D;
{C,1'b1,1'b1,3'b000}:next_state=C;
{C,1'b1,1'b1,3'b001}:next_state=D; //speed down
{C,1'b1,1'b1,3'b010}:next_state=B; //speed up
{C,1'b1,1'b1,3'b011}:next_state=B;
{C,1'b1,1'b1,3'b100}:next_state=B;
{C,1'b1,1'b1,3'b101}:next_state=B;
{C,1'b1,1'b1,3'b110}:next_state=C;
{C,1'b1,1'b1,3'b111}:next_state=C;

{D,1'b0,1'b0,3'b000}:next_state=D;
{D,1'b0,1'b0,3'b001}:next_state=E; //speed down
{D,1'b0,1'b0,3'b010}:next_state=C; //speed up
{D,1'b0,1'b0,3'b011}:next_state=D;
{D,1'b0,1'b0,3'b100}:next_state=D;
{D,1'b0,1'b0,3'b101}:next_state=D;
{D,1'b0,1'b0,3'b110}:next_state=D;
{D,1'b0,1'b0,3'b111}:next_state=D;
{D,1'b0,1'b1,3'b000}:next_state=C; //speed up
{D,1'b0,1'b1,3'b001}:next_state=C;
{D,1'b0,1'b1,3'b010}:next_state=C;
{D,1'b0,1'b1,3'b011}:next_state=C;
{D,1'b0,1'b1,3'b100}:next_state=C;
{D,1'b0,1'b1,3'b101}:next_state=C;
{D,1'b0,1'b1,3'b110}:next_state=C;
{D,1'b0,1'b1,3'b111}:next_state=C;
{D,1'b1,1'b0,3'b000}:next_state=E; //speed down
{D,1'b1,1'b0,3'b001}:next_state=E;
{D,1'b1,1'b0,3'b010}:next_state=E;
{D,1'b1,1'b0,3'b011}:next_state=E;
{D,1'b1,1'b0,3'b100}:next_state=E;
{D,1'b1,1'b0,3'b101}:next_state=E;
{D,1'b1,1'b0,3'b110}:next_state=E;
{D,1'b1,1'b0,3'b111}:next_state=E;
{D,1'b1,1'b1,3'b000}:next_state=D;
{D,1'b1,1'b1,3'b001}:next_state=E; //speed down
{D,1'b1,1'b1,3'b010}:next_state=C; //speed up
{D,1'b1,1'b1,3'b011}:next_state=C;
{D,1'b1,1'b1,3'b100}:next_state=C;
{D,1'b1,1'b1,3'b101}:next_state=C;
{D,1'b1,1'b1,3'b110}:next_state=D;
{D,1'b1,1'b1,3'b111}:next_state=D;

{E,1'b0,1'b0,3'b000}:next_state=E;
{E,1'b0,1'b0,3'b001}:next_state=F; //speed down
{E,1'b0,1'b0,3'b010}:next_state=D; //speed up
{E,1'b0,1'b0,3'b011}:next_state=E;
{E,1'b0,1'b0,3'b100}:next_state=E;
{E,1'b0,1'b0,3'b101}:next_state=E;
{E,1'b0,1'b0,3'b110}:next_state=E;
{E,1'b0,1'b0,3'b111}:next_state=E;
{E,1'b0,1'b1,3'b000}:next_state=D; //speed up
{E,1'b0,1'b1,3'b001}:next_state=D;
{E,1'b0,1'b1,3'b010}:next_state=D;
{E,1'b0,1'b1,3'b011}:next_state=D;
{E,1'b0,1'b1,3'b100}:next_state=D;
{E,1'b0,1'b1,3'b101}:next_state=D;
{E,1'b0,1'b1,3'b110}:next_state=D;
{E,1'b0,1'b1,3'b111}:next_state=D;
{E,1'b1,1'b0,3'b000}:next_state=F; //speed down
{E,1'b1,1'b0,3'b001}:next_state=F;
{E,1'b1,1'b0,3'b010}:next_state=F;
{E,1'b1,1'b0,3'b011}:next_state=F;
{E,1'b1,1'b0,3'b100}:next_state=F;
{E,1'b1,1'b0,3'b101}:next_state=F;
{E,1'b1,1'b0,3'b110}:next_state=F;
{E,1'b1,1'b0,3'b111}:next_state=F;
{E,1'b1,1'b1,3'b000}:next_state=E;
{E,1'b1,1'b1,3'b001}:next_state=F; //speed down
{E,1'b1,1'b1,3'b010}:next_state=D; //speed up
{E,1'b1,1'b1,3'b011}:next_state=D;
{E,1'b1,1'b1,3'b100}:next_state=D;
{E,1'b1,1'b1,3'b101}:next_state=D;
{E,1'b1,1'b1,3'b110}:next_state=E;
{E,1'b1,1'b1,3'b111}:next_state=E;

{F,1'b0,1'b0,3'b000}:next_state=F;
{F,1'b0,1'b0,3'b001}:next_state=G; //speed down
{F,1'b0,1'b0,3'b010}:next_state=E; //speed up
{F,1'b0,1'b0,3'b011}:next_state=F;
{F,1'b0,1'b0,3'b100}:next_state=F;
{F,1'b0,1'b0,3'b101}:next_state=F;
{F,1'b0,1'b0,3'b110}:next_state=F;
{F,1'b0,1'b0,3'b111}:next_state=F;
{F,1'b0,1'b1,3'b000}:next_state=E; //speed up
{F,1'b0,1'b1,3'b001}:next_state=E;
{F,1'b0,1'b1,3'b010}:next_state=E;
{F,1'b0,1'b1,3'b011}:next_state=E;
{F,1'b0,1'b1,3'b100}:next_state=E;
{F,1'b0,1'b1,3'b101}:next_state=E;
{F,1'b0,1'b1,3'b110}:next_state=E;
{F,1'b0,1'b1,3'b111}:next_state=E;
{F,1'b1,1'b0,3'b000}:next_state=G; //speed down
{F,1'b1,1'b0,3'b001}:next_state=G;
{F,1'b1,1'b0,3'b010}:next_state=G;
{F,1'b1,1'b0,3'b011}:next_state=G;
{F,1'b1,1'b0,3'b100}:next_state=G;
{F,1'b1,1'b0,3'b101}:next_state=G;
{F,1'b1,1'b0,3'b110}:next_state=G;
{F,1'b1,1'b0,3'b111}:next_state=G;
{F,1'b1,1'b1,3'b000}:next_state=F;
{F,1'b1,1'b1,3'b001}:next_state=G; //speed down
{F,1'b1,1'b1,3'b010}:next_state=E; //speed up
{F,1'b1,1'b1,3'b011}:next_state=E;
{F,1'b1,1'b1,3'b100}:next_state=E;
{F,1'b1,1'b1,3'b101}:next_state=E;
{F,1'b1,1'b1,3'b110}:next_state=F;
{F,1'b1,1'b1,3'b111}:next_state=F;

{G,1'b0,1'b0,3'b000}:next_state=G;
{G,1'b0,1'b0,3'b001}:next_state=H; //speed down
{G,1'b0,1'b0,3'b010}:next_state=F; //speed up
{G,1'b0,1'b0,3'b011}:next_state=G;
{G,1'b0,1'b0,3'b100}:next_state=G;
{G,1'b0,1'b0,3'b101}:next_state=G;
{G,1'b0,1'b0,3'b110}:next_state=G;
{G,1'b0,1'b0,3'b111}:next_state=G;
{G,1'b0,1'b1,3'b000}:next_state=F; //speed up
{G,1'b0,1'b1,3'b001}:next_state=F;
{G,1'b0,1'b1,3'b010}:next_state=F;
{G,1'b0,1'b1,3'b011}:next_state=F;
{G,1'b0,1'b1,3'b100}:next_state=F;
{G,1'b0,1'b1,3'b101}:next_state=F;
{G,1'b0,1'b1,3'b110}:next_state=F;
{G,1'b0,1'b1,3'b111}:next_state=F;
{G,1'b1,1'b0,3'b000}:next_state=H; //speed down
{G,1'b1,1'b0,3'b001}:next_state=H;
{G,1'b1,1'b0,3'b010}:next_state=H;
{G,1'b1,1'b0,3'b011}:next_state=H;
{G,1'b1,1'b0,3'b100}:next_state=H;
{G,1'b1,1'b0,3'b101}:next_state=H;
{G,1'b1,1'b0,3'b110}:next_state=H;
{G,1'b1,1'b0,3'b111}:next_state=H;
{G,1'b1,1'b1,3'b000}:next_state=G;
{G,1'b1,1'b1,3'b001}:next_state=H; //speed down
{G,1'b1,1'b1,3'b010}:next_state=F; //speed up
{G,1'b1,1'b1,3'b011}:next_state=F;
{G,1'b1,1'b1,3'b100}:next_state=F;
{G,1'b1,1'b1,3'b101}:next_state=F;
{G,1'b1,1'b1,3'b110}:next_state=G;
{G,1'b1,1'b1,3'b111}:next_state=G;

{H,1'b0,1'b0,3'b000}:next_state=H;
{H,1'b0,1'b0,3'b001}:next_state=H; //speed down
{H,1'b0,1'b0,3'b010}:next_state=G; //speed up
{H,1'b0,1'b0,3'b011}:next_state=H;
{H,1'b0,1'b0,3'b100}:next_state=H;
{H,1'b0,1'b0,3'b101}:next_state=H;
{H,1'b0,1'b0,3'b110}:next_state=H;
{H,1'b0,1'b0,3'b111}:next_state=H;
{H,1'b0,1'b1,3'b000}:next_state=G; //speed up
{H,1'b0,1'b1,3'b001}:next_state=G;
{H,1'b0,1'b1,3'b010}:next_state=G;
{H,1'b0,1'b1,3'b011}:next_state=G;
{H,1'b0,1'b1,3'b100}:next_state=G;
{H,1'b0,1'b1,3'b101}:next_state=G;
{H,1'b0,1'b1,3'b110}:next_state=G;
{H,1'b0,1'b1,3'b111}:next_state=G;
{H,1'b1,1'b0,3'b000}:next_state=H; //speed down
{H,1'b1,1'b0,3'b001}:next_state=H;
{H,1'b1,1'b0,3'b010}:next_state=H;
{H,1'b1,1'b0,3'b011}:next_state=H;
{H,1'b1,1'b0,3'b100}:next_state=H;
{H,1'b1,1'b0,3'b101}:next_state=H;
{H,1'b1,1'b0,3'b110}:next_state=H;
{H,1'b1,1'b0,3'b111}:next_state=H;
{H,1'b1,1'b1,3'b000}:next_state=H;
{H,1'b1,1'b1,3'b001}:next_state=H; //speed down
{H,1'b1,1'b1,3'b010}:next_state=G; //speed up
{H,1'b1,1'b1,3'b011}:next_state=G;
{H,1'b1,1'b1,3'b100}:next_state=G;
{H,1'b1,1'b1,3'b101}:next_state=G;
{H,1'b1,1'b1,3'b110}:next_state=H;
{H,1'b1,1'b1,3'b111}:next_state=H;
default:next_state=F;     //Default goes to F state (0.64s)
endcase


//counter_1 for animation 1
always @(posedge clock, negedge reset) //synchronized by modified 'clock' signal
if(~reset) counter_1<=5'b0;
else if(~sw1) counter_1<=5'b0;
else counter_1<=counter_1+5'b1;

//counter_2 for animation 2
always @(posedge clock, negedge reset) //clocked by modified 'clock' clock
if(~reset) counter_2<=5'b0;
else if(~sw2) counter_2<=5'b0;
else counter_2<=counter_2+5'b1;

//counter_3 for animation 3
always @(posedge clock, negedge reset) //clocked by modified 'clock' clock
if(~reset) counter_3<=7'b0;
else if(~sw3) counter_3<=7'b0;
else counter_3<=counter_3+7'b1;

//animation 1 hex
DEC0 U0(.i(counter_1),.o(t1hex0));
DEC1 U1(.i(counter_1),.o(t1hex1));
DEC2 U2(.i(counter_1),.o(t1hex2));
DEC3 U3(.i(counter_1),.o(t1hex3));
DEC4 U4(.i(counter_1),.o(t1hex4));
DEC5 U5(.i(counter_1),.o(t1hex5));

//animation 2 hex
DEC6 U6(.i(counter_2),.o(t2hex0));
DEC7 U7(.i(counter_2),.o(t2hex1));
DEC8 U8(.i(counter_2),.o(t2hex2));
DEC9 U9(.i(counter_2),.o(t2hex3));
DEC10 U10(.i(counter_2),.o(t2hex4));
DEC11 U11(.i(counter_2),.o(t2hex5));

//animation 3 hex
DEC12 U12(.i(counter_3),.o(t3hex0));
DEC13 U13(.i(counter_3),.o(t3hex1));
DEC14 U14(.i(counter_3),.o(t3hex2));
DEC15 U15(.i(counter_3),.o(t3hex3));
DEC16 U16(.i(counter_3),.o(t3hex4));
DEC17 U17(.i(counter_3),.o(t3hex5));

//LED animation 1, 2, 3
LED_DEC0 U18(.i(counter_1),.o(t1led));
LED_DEC1 U19(.i(counter_2),.o(t2led));
LED_DEC2 U20(.i(counter_3),.o(t3led));

//select animation to HEXs 
always@*
casex({sw1,sw2,sw3,reg_remote_output}) //011,100,101
{1'b0,1'b0,1'b0,3'bxxx}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111};
{1'b0,1'b0,1'b1,3'bxxx}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{t3hex0,t3hex1,t3hex2,t3hex3,t3hex4,t3hex5};
{1'b0,1'b1,1'b0,3'bxxx}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{t2hex0,t2hex1,t2hex2,t2hex3,t2hex4,t2hex5};
{1'b0,1'b1,1'b1,3'b000}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111};
{1'b0,1'b1,1'b1,3'b001}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111};
{1'b0,1'b1,1'b1,3'b010}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111};
{1'b0,1'b1,1'b1,3'b011}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{t1hex0,t1hex1,t1hex2,t1hex3,t1hex4,t1hex5};
{1'b0,1'b1,1'b1,3'b100}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{t2hex0,t2hex1,t2hex2,t2hex3,t2hex4,t2hex5};
{1'b0,1'b1,1'b1,3'b101}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{t3hex0,t3hex1,t3hex2,t3hex3,t3hex4,t3hex5};							
{1'b0,1'b1,1'b1,3'b110}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111};
{1'b0,1'b1,1'b1,3'b111}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111};
{1'b1,1'b0,1'b0,3'bxxx}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{t1hex0,t1hex1,t1hex2,t1hex3,t1hex4,t1hex5};						
{1'b1,1'b0,1'b1,3'b000}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111};
{1'b1,1'b0,1'b1,3'b001}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111};
{1'b1,1'b0,1'b1,3'b010}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111};
{1'b1,1'b0,1'b1,3'b011}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{t1hex0,t1hex1,t1hex2,t1hex3,t1hex4,t1hex5};
{1'b1,1'b0,1'b1,3'b100}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{t2hex0,t2hex1,t2hex2,t2hex3,t2hex4,t2hex5};
{1'b1,1'b0,1'b1,3'b101}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{t3hex0,t3hex1,t3hex2,t3hex3,t3hex4,t3hex5};							
{1'b1,1'b0,1'b1,3'b110}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111};
{1'b1,1'b0,1'b1,3'b111}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111};

{1'b1,1'b1,1'b0,3'b000}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111};
{1'b1,1'b1,1'b0,3'b001}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111};
{1'b1,1'b1,1'b0,3'b010}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111};
{1'b1,1'b1,1'b0,3'b011}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{t1hex0,t1hex1,t1hex2,t1hex3,t1hex4,t1hex5};
{1'b1,1'b1,1'b0,3'b100}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{t2hex0,t2hex1,t2hex2,t2hex3,t2hex4,t2hex5};
{1'b1,1'b1,1'b0,3'b101}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{t3hex0,t3hex1,t3hex2,t3hex3,t3hex4,t3hex5};							
{1'b1,1'b1,1'b0,3'b110}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111};
{1'b1,1'b1,1'b0,3'b111}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111};				
{1'b1,1'b1,1'b1,3'b000}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111};
{1'b1,1'b1,1'b1,3'b001}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111};
{1'b1,1'b1,1'b1,3'b010}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111};
{1'b1,1'b1,1'b1,3'b011}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{t1hex0,t1hex1,t1hex2,t1hex3,t1hex4,t1hex5};
{1'b1,1'b1,1'b1,3'b100}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{t2hex0,t2hex1,t2hex2,t2hex3,t2hex4,t2hex5};
{1'b1,1'b1,1'b1,3'b101}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{t3hex0,t3hex1,t3hex2,t3hex3,t3hex4,t3hex5};							
{1'b1,1'b1,1'b1,3'b110}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111};
{1'b1,1'b1,1'b1,3'b111}:{hex0,hex1,hex2,hex3,hex4,hex5}=
								{7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111};				
default:{hex0,hex1,hex2,hex3,hex4,hex5}=
			{7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111,7'b1111111};
endcase
//select animation to LEDs 
always@*
casex({sw1,sw2,sw3,reg_remote_output})
{1'b0,1'b0,1'b0,3'bxxx}:led = 10'd0;
{1'b0,1'b0,1'b1,3'bxxx}:led = t3led;
{1'b0,1'b1,1'b0,3'bxxx}:led = t2led;
{1'b0,1'b1,1'b1,3'b000}:led = 10'd0;
{1'b0,1'b1,1'b1,3'b001}:led = 10'd0;
{1'b0,1'b1,1'b1,3'b010}:led = 10'd0;
{1'b0,1'b1,1'b1,3'b011}:led = t1led;
{1'b0,1'b1,1'b1,3'b100}:led = t2led;
{1'b0,1'b1,1'b1,3'b101}:led = t3led;					
{1'b0,1'b1,1'b1,3'b110}:led = 10'd0;
{1'b0,1'b1,1'b1,3'b111}:led = 10'd0;
{1'b1,1'b0,1'b0,3'bxxx}:led = t1led;			
{1'b1,1'b0,1'b1,3'b000}:led = 10'd0;
{1'b1,1'b0,1'b1,3'b001}:led = 10'd0;
{1'b1,1'b0,1'b1,3'b010}:led = 10'd0;
{1'b1,1'b0,1'b1,3'b011}:led = t1led;
{1'b1,1'b0,1'b1,3'b100}:led = t2led;
{1'b1,1'b0,1'b1,3'b101}:led = t3led;			
{1'b1,1'b0,1'b1,3'b110}:led = 10'd0;
{1'b1,1'b0,1'b1,3'b111}:led = 10'd0;
{1'b1,1'b1,1'b0,3'b000}:led = 10'd0;
{1'b1,1'b1,1'b0,3'b001}:led = 10'd0;
{1'b1,1'b1,1'b0,3'b010}:led = 10'd0;
{1'b1,1'b1,1'b0,3'b011}:led = t1led;
{1'b1,1'b1,1'b0,3'b100}:led = t2led;
{1'b1,1'b1,1'b0,3'b101}:led = t3led;					
{1'b1,1'b1,1'b0,3'b110}:led = 10'd0;
{1'b1,1'b1,1'b0,3'b111}:led = 10'd0;						
{1'b1,1'b1,1'b1,3'b000}:led = 10'd0;
{1'b1,1'b1,1'b1,3'b001}:led = 10'd0;
{1'b1,1'b1,1'b1,3'b010}:led = 10'd0;
{1'b1,1'b1,1'b1,3'b011}:led = t1led;
{1'b1,1'b1,1'b1,3'b100}:led = t2led;
{1'b1,1'b1,1'b1,3'b101}:led = t3led;			
{1'b1,1'b1,1'b1,3'b110}:led = 10'd0;
{1'b1,1'b1,1'b1,3'b111}:led = 10'd0;
default: led = 10'd0;
endcase
endmodule

