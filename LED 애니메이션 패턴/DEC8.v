module DEC8(input wire[4:0]i,output reg[6:0]o);
always @*
case(i)
5'd0: o=7'b1111111;
5'd1: o=7'b1111111;
5'd2: o=7'b1111111;
5'd3: o=7'b1111111;
5'd4: o=7'b1111111;
5'd5: o=7'b1111111;
5'd6: o=7'b1111111;
5'd7: o=7'b1111111;
5'd8: o=7'b1111111;
5'd9: o=7'b1111111;
5'd10:o=7'b1111111;
5'd11:o=7'b1111111;
5'd12:o=7'b1110111;
5'd13:o=7'b1110011;
5'd14:o=7'b1110001;
5'd15:o=7'b1110001;
5'd16:o=7'b1110001;
5'd17:o=7'b1110001;
5'd18:o=7'b1110001;
5'd19:o=7'b1110001;
5'd20:o=7'b1110001;
5'd21:o=7'b1110001;
5'd22:o=7'b1110001;
5'd23:o=7'b1110001;
5'd24:o=7'b1111001;
5'd25:o=7'b1111101;
5'd26:o=7'b1111111;
5'd27:o=7'b1111111;
5'd28:o=7'b1111111;
5'd29:o=7'b1111111;
5'd30:o=7'b1111111;
5'd31:o=7'b1111111;
endcase 
endmodule
