function output = Bilateral_filter(input,j,i,sigma,sigma_d)
input2 = input;
f = zeros(3,3);
f2 = zeros(3,3);
for y=-1:1
    for x=-1:1
        f(y+2,x+2) = input2(j+y,i+x)*exp((-1*(((y^2)+(x^2))/(2*(sigma^2)))) - ((norm(input2(j,i)-input2(j+y,i+x))^2)/(2*(sigma_d^2))));
        f2(y+2,x+2) = exp((-1*(((y^2)+(x^2))/(2*(sigma^2)))) - ((norm(input2(j,i)-input2(j+y,i+x))^2)/(2*(sigma_d^2))));
    end
end
output = sum(sum(f))/sum(sum(f2));

