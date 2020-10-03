module traffic_test();
reg Clock, Reset;  // Input Data to Top Module
reg TLight_A, TLight_B, Po_s, Ro_s;
wire [2:0] A_Light_out, B_Light_out;  //Output Data from Top Module
wire [1:0] Traffic_state;
wire Mode_state;
wire Mode_out;

initial begin       //Input data Initialization.
Clock = 1; Reset = 0; TLight_A = 1; TLight_B = 1; Po_s =0; Ro_s = 0;
#35 Reset = 1;    //Reset Clock Input
#45 Reset = 0;
#30 TLight_A = 0; // Light FSM S0 -> S1 -> S2
#75 Po_s = 1;     // Mode S0 -> S1 -> S0
 #25 TLight_B =0; // Light FSM S2 -> S2 -> S3 -> S0
#25 Po_s = 0;
#50 Ro_s =1;
#65 Ro_s =0;
#200 $stop;
end

always begin      //clock controls
Clock = ~Clock;
#25;
end

//Wiring between Top Module and Signal.
parade_traffic_light 
t(.clk(Clock), .rst(Reset), .Ta(TLight_A), .Tb(TLight_B), .P(Po_s), 
.R(Ro_s), .La(A_Light_out), .Lb(B_Light_out), .States_light_out(Traffic_state), 
.State_mode_out(Mode_state), .M(Mode_out));

endmodule