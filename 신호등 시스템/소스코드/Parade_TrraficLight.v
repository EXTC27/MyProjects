module parade_mode(rst, clk, P, R, S_mode, M); //Mode FSM의 module
input wire rst,clk;      // clock, reset
input wire P,R;         // 입력 P, R
output wire S_mode,M; // 상태 출력 S_mode와 출력 M
reg next_s;

always@(posedge clk or posedge rst)  // clock이나 reset이 상승edge일 때 작동
begin

if(rst)
 next_s <= 1'b0;  // reset이 입력되면 상태는 S0으로 초기화
 
else if(P==0 && next_s==1'b0)   
 next_s <= 1'b0;
// 현재 상태가 S0이고 R의 값과 상관없이 P가 0이면 다음상태는 S0으로 유지

else if(P==1 && next_s==1'b0)
 next_s <= 1'b1;
// 현재 상태가 S0이고 R의 값과 상관없이 P가 1이면 다음상태는 S1으로 변환

else if(R==0 && next_s==1'b1)
 next_s <= 1'b1;
// 현재 상태가 S1이고 P의 값과 상관없이 R가 0이면 다음상태는 S1으로 유지

else if(R==1 && next_s==1'b1)
 next_s <= 1'b0;
// 현재 상태가 S1이고 P의 값과 상관없이 R가 1이면 다음상태는 S0으로 변환

end

assign S_mode = next_s;   // 조건문안의 상태함수 next_s는 S_mode이다.
assign M = S_mode;       // S_mode는 출력 M과 같다.

endmodule

///////////////////////////////////////////////////////////////////////////////////////////////////////////
module traffic_light(rst, clk, Ta, Tb, M, S_light, La, Lb); //Light FSM의 module
input wire Ta,Tb,M;      // 입력 , , M
input wire rst,clk;        // reset, clock
output wire [2:0]La,Lb;   // 신호등 출력 , 
output wire [1:0]S_light;  // 상태 출력 S_light
reg [2:0]la,lb;
reg [1:0]next_s;

always@(posedge clk or posedge rst) // clock이나 reset이 상승edge일 때 작동
begin

if(rst)              // reset이 입력되면
begin
 next_s = 2'b00;   // 상태는 S0로 초기화
 la= 3'b111;       // 는 green
 lb= 3'b001;       // 는 red
end

else if(Ta==1'b1 && next_s==2'b00) // 현재상태가 S0일 때 가 1이면
begin
 next_s <= 2'b00;   // 다음상태는 S0으로 유지
 la= 3'b111;        // 는 green
 lb= 3'b001;        // 는 red
end

else if(Ta==1'b0 && next_s==2'b00) // 현재상태가 S0일 때 가 0이면
begin               
 next_s <= 2'b01;   // 다음상태는 S1으로 변환
 la= 3'b100;        // 는 yellow
 lb= 3'b001;        // 는 red
end

else if(next_s==2'b01) // 현재상태가 S1이면
begin
 next_s <= 2'b10;  // 다음상태는 S2으로 변환
 la= 3'b001;       // 는 red
 lb= 3'b111;       // 는 green
end

else if((M|Tb==1) && next_s==2'b10) // 현재상태가 S2일 때 (M+)가 1이면
 begin
  next_s <= 2'b10;   // 다음상태는 S2으로 유지
  la= 3'b001;        // 는 red
  lb= 3'b111;        // 는 green
 end

else if((M|Tb==0) && next_s==2'b10) // 현재상태가 S2일 때 (M+)가 0이면
 begin
  next_s <= 2'b11;   // 다음상태는 S3으로 변환
  la= 3'b001;        // 는 red
  lb= 3'b100;        // 는 yellow
 end

else if(next_s==2'b11)  // 현재상태가 S3이면
begin
 next_s <= 2'b00;   // 다음상태는 S0으로 변환
 la= 3'b111;        // 는 green
 lb= 3'b001;        // 는 red
end
end

assign S_light = next_s;  // 조건문안의 상태함수 next_s는 상태출력 S_light
assign La = la;           // 조건문안의 la는 출력 
assign Lb = lb;           // 조건문안의 lb는 출력 

endmodule

///////////////////////////////////////////////////////////////////////////////////////////////////////////
module parade_traffic_light
(clk, rst, Ta, Tb, P, R, La, Lb, States_light_out, State_mode_out, M);
// Mode FSM와 Light FSM를 연결시키는 top module

input clk,rst;   // clock, reset
input P,R,Ta,Tb;   // 입력 P, R, , 
output [2:0]La,Lb;   // 신호등 출력 , 
output [1:0]States_light_out;  // Light FSM의 상태 출력 
output State_mode_out,M;   // Mode FSM의 상태 출력과 출력 M

wire m; //Mode FSM의 출력 M을 Light FSM의 입력으로 넣어주기 위한 선언

assign M=m; // m은 출력 M이다
					  
parade_mode   parade(rst, clk, P, R, State_mode_out, m);
traffic_light light (rst, clk, Ta, Tb, m, States_light_out, La, Lb);
// Mode FSM과 Light FSM을 연동

endmodule