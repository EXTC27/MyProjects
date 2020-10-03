module varyingclock(input wire clk,reset,
input wire[31:0] n, output wire clock);
    //clock = Xsec clock,   clk = 50MHz clock
wire [31:0] next_counter0;
reg [31:0]counter0;  
assign next_counter0 = (counter0==n) ? 32'd0 : counter0 + 32'b1; 
always @(posedge clk, negedge reset) 
if(~reset) counter0 = 32'd0;
else counter0 = next_counter0;

assign clock=(counter0== n);    

endmodule
