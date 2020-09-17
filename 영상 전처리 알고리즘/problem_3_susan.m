img = double(imread('lena_grey.bmp'));
%img = double(imread('pepper_grey.bmp'));
%img = double(imread('babbon_grey.bmp'));
[height,width] = size(img);

susan = NaN(7,'double');
susan(1,3:5) = 1;
susan(2,2:6) = 1;
susan(3,:) = 1;
susan(4,:) = 1;
susan(5,:) = 1;
susan(6,2:6) = 1;
susan(7,3:5) = 1;
usan = zeros(7,7);
S = double(zeros(height,width));
for j=4:height-3
    for i=4:width-3
        b = img(j-3:j+3,i-3:i+3);
        usan = b .* susan;
        for y=-3:3
            for x=-3:3
                if abs(usan(y+4,x+4)-b(4,4)) < 20
                    usan(y+4,x+4) = 1;
                else usan(y+4,x+4) = 0;
                end
            end
        end
        usanarea = sum(sum(usan,'omitnan'));
        if usanarea < 31
            S(j,i) = (0.75*49 - usanarea)*4; % 특징 가능성 값
        else S(j,i) = 0;
        end
    end
end

imshow(S/max(max(S)));        