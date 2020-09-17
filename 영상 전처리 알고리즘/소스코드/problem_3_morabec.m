img = double(imread('lena_grey.bmp'));
%img = double(imread('pepper_grey.bmp'));
%img = double(imread('babbon_grey.bmp'));'));
[height,width] = size(img);

M = double(zeros(height,width));
mask = NaN(3,'double');
for j=3:height-2
    for i=3:width-2
        b_mid = img(j-1:j+1,i-1:i+1);
        b_up = img(j-2:j,i-1:i+1);
        b_down = img(j:j+2,i-1:i+1);
        b_left = img(j-1:j+1,i-2:i);
        b_right = img(j-1:j+1,i:i+2);
        mask(1,2) = sum(sum((b_mid - b_up).^2));  % 위와 비교
        mask(3,2) = sum(sum((b_mid - b_down).^2));  % 아래와 비교
        mask(2,1) = sum(sum((b_mid - b_left).^2));  % 왼쪽과 비교
        mask(2,3) = sum(sum((b_mid - b_right).^2)); % 오른쪽과 비교       
        M(j,i) = min(min(mask,[],'omitnan')); % NaN값을 빼고 최소값을 구한다. 특징 가능성 값
    end 
end

imshow(M/max(max(M))); %모라벡을 이용한 코너 검출 영상