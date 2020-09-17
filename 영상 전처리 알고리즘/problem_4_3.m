%img = double(imread('lena.bmp'));
%img = double(imread('pepper.bmp'));
img = double(imread('babbon.bmp'));
[height, width, channel] = size(img);
k=10;
R_img = img(:,:,1);
G_img = img(:,:,2);
B_img = img(:,:,3);
R_img_1d = zeros(1,height*width);
G_img_1d = zeros(1,height*width);
B_img_1d = zeros(1,height*width);
for j=1:height  % 1차원 배열로 바꾼다
    for i=1:width
        R_img_1d(1,i+((j-1)*width)) = R_img(j,i);
        G_img_1d(1,i+((j-1)*width)) = G_img(j,i);
        B_img_1d(1,i+((j-1)*width)) = B_img(j,i);
    end
end
X = [R_img_1d; G_img_1d; B_img_1d];

init_Z = zeros(channel,k);
for k_temp=1:k 
    init_Z(:,k_temp) = X(:,fix(length(X)/k)*(k_temp-1)+1);
end

Z = init_Z;


K = zeros(channel,length(X));
residue = zeros(channel,k);
breakpoint = 0;
while(breakpoint<20)
K_com = zeros(1,length(K));
for l=1:length(X)
    residue = zeros(channel,k);
    for c=1:channel
        for k_temp=1:k
            residue(c,k_temp) = abs(Z(c,k_temp) - X(c,l));
        end
    end
    residue_sum = sum(residue);
    residue_min = min(residue_sum);
    k_temp=1;
    while(1)        
        if residue_min == residue_sum(1,k_temp)
            K_com(l) = k_temp;
            break;
        end
        k_temp = k_temp+1;
    end
end

Z_temp = zeros(channel,k);
Z_mean_temp = zeros(channel,k);
    
    for k_temp=1:k
        num=0;
        for l=1:length(X)
            if K_com(1,l) == k_temp;
                Z_temp(:,k_temp) = Z_temp(:,k_temp)+X(:,l);
                num = num+1;
            end            
        end        
        Z_mean_temp(:,k_temp) = round(Z_temp(:,k_temp)/num);
    end
    

        
if Z_mean_temp ~= Z
    Z = Z_mean_temp;
end
    breakpoint = breakpoint+1;
end

K_com_2d = zeros(height,width);
for j=1:height
    for i=1:width
        K_com_2d(j,i) = K_com(1,i+((j-1)*width));
    end
end

distance = 3;
for j=1+fix(distance/2):height-(distance/2)
    for i=1+fix(distance/2):width-fix(distance/2)
        area = zeros(distance,distance);
        area(1:distance,1:distance) = K_com_2d(j-fix(distance/2):j+fix(distance/2),i-fix(distance/2):i+fix(distance/2)); 
        count_diff = 0;
        for y=1:distance
            for x=1:distance
                if area(y,x) ~= K_com_2d(j,i)
                    count_diff = count_diff+1;
                end
            end
        end
        if count_diff >= ((distance^2)-1)/2
            count = zeros(1,k);
            for y=1:distance
                for x=1:distance
                    for k_temp=1:k
                        if area(y,x) == k_temp;
                           count(1,k_temp) = count(1,k_temp)+1;
                        end
                    end
                end
            end
            max_count = max(count);
            for k_temp=1:k
                if count(1,k_temp) == max_count
                    K_com_2d(j,i) = k_temp;
                end
            end
        end
    end    
end
        
K_1d = zeros(channel, length(K));
for j=1:height
    for i=1:width
        K_1d(1,i+((j-1)*width)) = K_com_2d(j,i);
    end
end

for k_temp=1:k
for l=1:length(X)
    if K_1d(1,l) == k_temp
        K_1d(:,l) = Z(:,k_temp);
    end
end
end

k_img = zeros(height,width,channel);

for c=1:channel
    for j=1:height
        for i=1:width
            k_img(j,i,c) = K_1d(c,i+((j-1)*width));
        end
    end
end
        
figure(1)
imshow(k_img/255);

