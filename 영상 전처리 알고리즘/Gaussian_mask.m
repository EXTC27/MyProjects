function output = Gaussian_mask(sigma)

f = zeros(3,3);
for y= -1:1
    for x= -1:1
        f(y+2,x+2) = exp(-(y^2+x^2)/(2*(sigma^2)))/(2*pi*(sigma^2));
    end
end
output = f./sum(sum(f));



