`timescale 10ns/1ps 
module test_bench; 
reg clk;

reg reset;
reg sw1,sw2,sw3,IRDA;
reg up,down;
wire [9:0] led;
wire [6:0]hex0,hex1,hex2,hex3,hex4,hex5;

term U1(
.clk(clk),.reset(reset),.up(up),.down(down),
.sw1(sw1),.sw2(sw2),.sw3(sw3),.led(led),.IRDA(IRDA),
.hex0(hex0),.hex1(hex1),.hex2(hex2),.hex3(hex3),.hex4(hex4),.hex5(hex5));

initial 
begin clk = 0; 
forever #1 clk = ~clk; 
end 

initial
begin//reset
		sw1=1;sw2=0;sw3=0;reset=1;up=1;down=1;IRDA=1;
#(10)	sw1=1;sw2=0;sw3=0;reset=0;up=1;down=1;IRDA=1;
#(10)	sw1=1;sw2=0;sw3=0;reset=1;up=1;down=1;IRDA=1;

//speed up
#(10) sw1=1;sw2=0;sw3=0;reset=1;up=0;down=1;IRDA=1;
#(10) sw1=1;sw2=0;sw3=0;reset=1;up=1;down=1;IRDA=1;
#(10) sw1=1;sw2=0;sw3=0;reset=1;up=0;down=1;IRDA=1;
#(10) sw1=1;sw2=0;sw3=0;reset=1;up=1;down=1;IRDA=1;
#(10) sw1=1;sw2=0;sw3=0;reset=1;up=0;down=1;IRDA=1;
#(10) sw1=1;sw2=0;sw3=0;reset=1;up=1;down=1;IRDA=1;
#(10) sw1=1;sw2=0;sw3=0;reset=1;up=0;down=1;IRDA=1;
#(10) sw1=1;sw2=0;sw3=0;reset=1;up=1;down=1;IRDA=1;
#(10) sw1=1;sw2=0;sw3=0;reset=1;up=0;down=1;IRDA=1;
#(10) sw1=1;sw2=0;sw3=0;reset=1;up=1;down=1;IRDA=1;
#(10) sw1=1;sw2=0;sw3=0;reset=1;up=0;down=1;IRDA=1;
#(10) sw1=1;sw2=0;sw3=0;reset=1;up=1;down=1;IRDA=1;

//speed down
#(100000) sw1=1;sw2=0;sw3=0;reset=1;up=1;down=0;IRDA=1;
#(10) sw1=1;sw2=0;sw3=0;reset=1;up=1;down=1;IRDA=1;
#(10) sw1=1;sw2=0;sw3=0;reset=1;up=1;down=0;IRDA=1;	
#(10) sw1=1;sw2=0;sw3=0;reset=1;up=1;down=1;IRDA=1;

#(500000) $finish;
end
endmodule
