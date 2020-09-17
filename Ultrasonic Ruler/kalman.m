function output = kalman(A,array)
for i = 1 :array
     if(A(i) == 0 || A(i) >= 250)
         A(i) = 0;
         array = array -1;
     end
 end
  
     B  = sort(A) ;  
  
 for i = 1: array
     if( (B(i) > B(1)+5) || (B(i) < B(1)-5) ) %% ÆòÅºÈ­
         B(i) =250;
         array = array-1;
     end
         C = sort(B);
 
 end 

 for i= 1:array
 output = SimpleKalman(C(1,i)); 
 end

