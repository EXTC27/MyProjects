img = double(imread('brain.bmp'));
[height,width] = size(img);
img_hist = zeros(1,256); 

% 히스토그램
for j=1:height 
    for i=1:width
        for n=1:256
            if n-1 == img(j,i)
                 img_hist(n) = img_hist(n) + 1;
            end
        end
    end
end

figure(1) % brain 영상 히스토그램 화면
stem(img_hist,'r.'); grid on; 


nom_hist = img_hist/(height*width); % 히스토그램 정규화
cdf_hist = nom_hist; % cdf
for i=2:256
    cdf_hist(i) = sum(nom_hist(1:i));
end

figure(2) % cdf 화면
stem(cdf_hist,'r.'); grid on;
axis([0 255 0 1]);

eq_hist = zeros(1,256); 
for j=1:height % 평활화 된 히스토그램
    for i=1:width
        for n=1:256
            if n-1 == eq_hist_img(j,i)
                 eq_hist(n) = eq_hist(n) + 1;
            end
        end
    end
end

figure(3) % 평활화 된 히스토그램
stem(eq_hist,'r.'); grid on;

mapping_point = round(cdf_hist*255); % 매핑점
eq_hist_img = img;
for j=1:height % 평활화
    for i=1:width
        eq_hist_img(j,i) = mapping_point(eq_hist_img(j,i)+1);
    end
end
 
figure(4) % 원본 영상과 개선된 영상
subplot(1,2,1); 
imshow(img/255);
subplot(1,2,2); 
imshow(eq_hist_img/255);

figure(5) % 기존 hist 함수와 비교
subplot(2,2,1); 
hist(img(:),[0:255]);
subplot(2,2,2); 
stem(img_hist,'r.');
axis([-100 300 0 6*(10^4)]);
subplot(2,2,3); 
hist(eq_hist_img(:),[0:255])
subplot(2,2,4); 
stem(eq_hist,'r.'); 
axis([-100 300 0 6*(10^4)]);

