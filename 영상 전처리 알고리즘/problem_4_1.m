%img = double(imread('lena_grey.bmp'));
%img = double(imread('pepper_grey.bmp'));
img = double(imread('babbon_grey.bmp'));
[height, width] = size(img);
k=10;
img_1d = zeros(1,height*width);
for j=1:height
    for i=1:width
        img_1d(1,i+((j-1)*width)) = img(j,i); % 1차원 배열로 바꾼다
    end
end
X = img_1d; 

init_Z = zeros(1,k);
for k_temp=1:k 
    init_Z(1,k_temp) = X(1,fix(length(X)/k)*(k_temp-1)+1);
end

Z = init_Z;

K = zeros(1,length(X));
residue = zeros(1,k);
breakpoint = 0; % 중단점
while(breakpoint<20)
K_com = zeros(1,length(K));
for l=1:length(X)
    residue = zeros(1,k);
    for k_temp=1:k
        residue(1,k_temp) = abs(Z(1,k_temp) - X(1,l));
    end
    
    residue_min = min(residue);
    k_temp=1;
    while(1)        
        if residue_min == residue(1,k_temp)
            K_com(l) = k_temp;
            break;
        end
        k_temp = k_temp+1;
    end
end

Z_temp = zeros(1,k);
Z_mean_temp = zeros(1,k);
    
for k_temp=1:k
    num=0;
    for l=1:length(X)
        if K_com(1,l) == k_temp;
            Z_temp(1,k_temp) = Z_temp(1,k_temp)+X(1,l);
            num = num+1;
        end            
    end        
    Z_mean_temp(1,k_temp) = round(Z_temp(1,k_temp)/num);
end
            
if Z_mean_temp ~= Z
    Z = Z_mean_temp;
end
    breakpoint = breakpoint+1;
end

K_1d = zeros(1, length(K));
for k_temp=1:k
    for l=1:length(X)
        if K_com(1,l) == k_temp
            K_1d(1,l) = Z(1,k_temp);
        end
    end
end

k_img = zeros(height,width,1);
for j=1:height
    for i=1:width
        k_img(j,i,1) = K_1d(1,i+((j-1)*width));
    end
end
        
figure(1)
imshow(k_img/max(max(k_img)));