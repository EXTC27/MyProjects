#include <stdio.h>
#include <stdlib.h>
#include "system.h"
#include "altera_avalon_uart_regs.h"
#include "time.h"
#include <math.h>

volatile int * GPIO_UART_ptr = (int *) UART_0_BASE; // UART address
void delay_t(int count);
int getTxLength(char *string);
double Calculate(int dis_cnt);
void make_put_line(char* c, int index);
char Start_line[161];

int main()
{
   volatile int *Ultra = ((volatile int *)ULTRA_SONIC_0_BASE);
   int dis_cnt =0;
   int i;
   double distance =0;
   char DATA;
   while(1)
     {
       DATA = IORD_ALTERA_AVALON_UART_RXDATA(GPIO_UART_ptr);
       if(DATA == '<')
       {
         for(i = 0; i<160; i++)
         {
           dis_cnt =*Ultra;
           distance = Calculate (dis_cnt);

           Start_line[i++] = (char) ((((int)distance)/100)%10)+48;
           Start_line[i++] = (char) ((((int)distance)/10)%10)+48;
           Start_line[i++] = (char) (((int)distance)%10)+48;
           Start_line[i] = ' ';
           delay_t(10);
         }
         Start_line[i] = 13;
         make_put_line(Start_line, 161);
         DATA = 0;
       }
     }
}
double Calculate(int dis_cnt)
{
 double distance = dis_cnt*0.000001*331.6/2;
 return distance;

}
void delay_t(int count)
{
volatile int * Timer_ptr= (int *) INTERVAL_TIMER_BASE;
 unsigned int counter= 100000*count;
 *(Timer_ptr + 0x2) = (counter&0xFFFF);
 *(Timer_ptr + 0x3) = ((counter>>16)&0xFFFF);
 *(Timer_ptr + 0x1)= 0x5;
 int Status = *(Timer_ptr);

 while(Status==2)
 {
  Status = *Timer_ptr;
 }
 *(Timer_ptr + 0x1)=0x0;
}

int getTxLength(char * string)
{
 int index = 0;

 while((string[index])!=0) index++;
 return index;
}

void make_put_line(char* c, int index)
{
 int Count=0;
 char* text_string = c;
 while(Count < index)
 {
  IOWR_ALTERA_AVALON_UART_TXDATA(GPIO_UART_ptr, text_string[Count]);
  delay_t(10);
  Count++;
 }
}