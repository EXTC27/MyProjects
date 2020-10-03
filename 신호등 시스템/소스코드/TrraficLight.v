module traffic_light(rst, clk, T, S, next_S, La, Lb);
input wire [1:0] T;            // 입력T 선언 (T[0]는 , T[1]는 로 지정)
input wire rst, clk;           // reset, clock 선언
output wire [1:0] La, Lb;     // 출력 , 선언
output wire [3:0] S, next_S;
 // 이전상태와 다음상태 선언
 // S=0001일 때 상태는 , next_S=0001일 때 
 // S=0010일 때 상태는 , next_S=0010일 때 
 // S=0100일 때 상태는 , next_S=0100일 때 
 // S=1000일 때 상태는 , next_S=1000일 때 

reg [1:0] la, lb;              
reg [3:0] next_s, s;           
 // 조건문안에 임시로 저장해줄 함수 선언

always@(posedge clk or posedge rst)  // clock이나 reset이 상승edge일 때 작동
begin
 if(rst)                   // reset이 입력되면 
  begin
   s = next_s;           // 이전상태는 reset을 입력하기 전 상태
   next_s = 4'b0001;    // 다음상태는 초기상태 (0001)로 돌아간다.
   la= 2'b00;            // 는 green
   lb= 2'b10;            // 는 red
  end
 else if(T[0]==0 && next_s==4'b0001)  // 와 상관없이 가 0이고 상태가 이면
  begin
   s = next_s;             // 이전상태는 (0001)로 바뀌고
   next_s = next_s<<1;    // 다음상태는 (0010)로 바뀐다. shift 사용
   la= 2'b01;              // 는 yellow
   lb= 2'b10;              // 는 red
  end

 else if(next_s==4'b0010)    // ,와 상관없이 상태가 이면
  begin
   s = next_s;              // 이전상태는 (0010)로 바뀌고
   next_s = next_s<<1;     // 다음상태는 (0100)로 바뀐다.
   la= 2'b10;               // 는 red
   lb= 2'b00;               // 는 green
  end

 else if(t[1]==0 && next_s==4'b0100)  // 와 상관없이 가 0이고 상태가 이면
  begin
   s = next_s;               // 이전상태는 (0100)로 바뀌고
   next_s = next_s<<1;      // 다음상태는 (1000)로 바뀐다.
   la= 2'b10;                // 는 red
   lb= 2'b01;                // 는 yellow
  end

else if(next_s==4'b1000)   // ,와 상관없이 상태가 이면
  begin
   s = next_s;             // 이전상태는 (1000)로 바뀌고
   next_s = next_s>>3;    // 다음상태는 (0001)로 바뀐다.
   la= 2'b00;              // 는 green
   lb= 2'b10;              // 는 red
  end
end

assign S=s;               // reg s는 output S로 연결
assign next_S=next_s;    // reg next_s 는 output next_S로 연결
assign La=la;             // reg la는 output La로 연결
assign Lb=lb;             // reg lb는 output Lb로 연결

endmodule