img = double(imread('lena_grey.bmp'));
%img = double(imread('pepper_grey.bmp'));
%img = double(imread('babbon_grey.bmp'));
[height, width] = size(img);
sigma = 10;
sigma_d = 200;
n=35;
awgn = n*randn(height,width); % Gaussian noise 생성
gaussian_noise_img = img+awgn;

uniform_noise = 3*n*rand(height,width); % Uniform noise 생성
uniform_noise_img = img+uniform_noise;

figure(1)
%subplot(1,3,1); % 원본 영상
%imshow(img/255);
subplot(1,2,1); % Gaussian noise
imshow(gaussian_noise_img/max(max(gaussian_noise_img))); 
subplot(1,2,2); % Uniform noise
imshow(uniform_noise_img/max(max(uniform_noise_img))); 

% Box filter 적용
box_filter = ones(3,3);
box_filter = box_filter/sum(sum(box_filter));
box_filter_gauss_noise = gaussian_noise_img; % Gaussian noise에 적용
box_filter_uni_noise = uniform_noise_img; % Uniform noise에 적용
temp1 = zeros(length(box_filter'),length(box_filter));
temp2 = zeros(length(box_filter'),length(box_filter));
for j=2:height-1
    for i=2:width-1
        for m=-1:1
            for n=-1:1
                temp1(m+2,n+2) = box_filter(m+2,n+2)*gaussian_noise_img(j+m,i+n);
                temp2(m+2,n+2) = box_filter(m+2,n+2)*uniform_noise_img(j+m,i+n);
            end
        end
        box_filter_gauss_noise(j,i) = sum(sum(temp1));
        box_filter_uni_noise(j,i) = sum(sum(temp2));
    end
end

figure(2) % Box filter 적용 영상
subplot(1,2,1); % Gaussian noise에 적용
imshow(box_filter_gauss_noise/max(max(box_filter_gauss_noise)));
subplot(1,2,2); % Uniform noise에 적용
imshow(box_filter_uni_noise/max(max(box_filter_uni_noise)));

% Gaussian filter 적용
gauss_filter = Gaussian_mask(sigma);
gauss_filter_gauss_noise = gaussian_noise_img; % Gaussian noise에 적용
gauss_filter_uni_noise = uniform_noise_img; % Uniform noise에 적용
temp1 = zeros(length(gauss_filter'),length(gauss_filter));
temp2 = zeros(length(gauss_filter'),length(gauss_filter));
for j=2:height-1
    for i=2:width-1
        for m=-1:1
            for n=-1:1
                temp1(m+2,n+2) = gauss_filter(m+2,n+2)*gaussian_noise_img(j+m,i+n);
                temp2(m+2,n+2) = gauss_filter(m+2,n+2)*uniform_noise_img(j+m,i+n);
            end
        end
        gauss_filter_gauss_noise(j,i) = sum(sum(temp1));
        gauss_filter_uni_noise(j,i) = sum(sum(temp2));
    end
end

figure(3) % Gaussian filter 적용 영상
subplot(1,2,1); % Gaussian noise에 적용
imshow(gauss_filter_gauss_noise/max(max(gauss_filter_gauss_noise))); 
subplot(1,2,2); % Uniform noise에 적용
imshow(gauss_filter_uni_noise/max(max(gauss_filter_uni_noise)));

bilateral_filter_gauss_noise = gaussian_noise_img; % Gaussian noise에 적용
bilateral_filter_uni_noise = uniform_noise_img; % Uniform noise에 적용
for j=2:height-1
    for i=2:width-1  
        bilateral_filter_gauss_noise(j,i) = Bilateral_filter(gaussian_noise_img,j,i,sigma,sigma_d);
        bilateral_filter_uni_noise(j,i) = Bilateral_filter(uniform_noise_img,j,i,sigma,sigma_d);
    end
end

figure(4) % Bilateral filter 적용 영상
subplot(1,2,1); % Gaussian noise에 적용
imshow(bilateral_filter_gauss_noise/max(max(bilateral_filter_gauss_noise))); 
subplot(1,2,2); % Uniform noise에 적용
imshow(bilateral_filter_uni_noise/max(max(bilateral_filter_uni_noise)));

count1 = zeros(height,width);
count2 = zeros(height,width);
count3 = zeros(height,width);
for j=1:height
    for i=1:width
        count1(j,i) = (gaussian_noise_img(j,i)-box_filter_gauss_noise(j,i))^2;
        count2(j,i) = (gaussian_noise_img(j,i)-gauss_filter_gauss_noise(j,i))^2;
        count3(j,i) = (gaussian_noise_img(j,i)-bilateral_filter_uni_noise(j,i))^2;
    end
end
mse1 = sum(sum(count1))/(height*width);
mse2 = sum(sum(count2))/(height*width);
mse3 = sum(sum(count3))/(height*width);

psnr_box = 10*log10((255^2)/mse1);
psnr_gauss = 10*log10((255^2)/mse2);
psnr_bilateral = 10*log10((255^2)/mse3);