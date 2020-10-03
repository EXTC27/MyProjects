module modulo_k_counter #(parameter k=4'd10)
(input wire reset, clk, en,
output wire rollover,
output reg[3:0] counter);

assign rollover = counter==(k-4'd1);
wire[3:0] next_counter;
assign next_counter = rollover ? 4'd0 : (counter+4'd1);

always@ (posedge clk, negedge reset)
if(~reset) counter<=4'd0;
else if(en) counter<=next_counter;

endmodule
