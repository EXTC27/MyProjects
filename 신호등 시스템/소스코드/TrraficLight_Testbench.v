module traffic_light_tb;
reg [1:0]T;
reg rst,clk;
wire [3:0]S,next_S;
wire [1:0]La,Lb;

traffic_light t(.rst(rst),.clk(clk),.T(T),.S(S),.next_S(next_S),.La(La),.Lb(Lb));
 // traffic_light 모듈과 testbench를 연동시킨다.

initial 
begin
clk = 1;
forever #5 clk=~clk;
end                       // clock을 0.01ns주기로 준다.

initial
begin
rst=1; #3;                // reset을 입력시켜 출력을 초기화
rst=0; T=2'b01; #20;      // 0.02ns동안 =0, =1 입력
rst=0; T=2'b11; #20;      // 0.02ns동안 =1, =1 입력
rst=0; T=2'b10; #30;      // 0.02ns동안 =1, =0 입력
rst=0; T=2'b11; #20;      // 0.02ns동안 =1, =1 입력
rst=0; T=2'b01; #30;      // 0.03ns동안 =0, =1 입력
rst=0; T=2'b11; #20;      // 0.02ns동안 =1, =1 입력
rst=0; T=2'b10; #20;      // 0.02ns동안 =1, =0 입력
rst=0; T=2'b11; #20;      // 0.02ns동안 =1, =1 입력
rst=1; #3;                // reset을 입력시켜 출력 초기화
rst=0;                    // reset해제
#100 $stop;              // 0.1ns 뒤에 시뮬레이션 종료
end 
endmodule