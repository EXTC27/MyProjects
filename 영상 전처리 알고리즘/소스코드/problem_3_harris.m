%img = double(imread('lena_grey.bmp'));
%img = double(imread('pepper_grey.bmp'));
img = double(imread('babbon_grey.bmp'));
[height,width] = size(img);

G = Gaussian_mask(1);
mask = [-1 0 1];
dy = zeros(height,width);
Y = zeros(1,3);
dx = zeros(height,width);
X = zeros(1,3);
% dy
for j=2:height-1 
    for i=1:width
        for n=-1:1
            Y(n+2) = img(j+n,i)*mask(1,n+2);           
        end
        dy(j,i) = sum(Y);       
    end
end
% dx
for j=1:height
    for i=2:width-1
        for n=-1:1
            X(n+2) = img(j,i+n)*mask(1,n+2);
        end
        dx(j,i) = sum(X);
    end
end

sqrdy = dy.^2; 
dydx = dy.*dx; 
sqrdx = dx.^2;
temp1 = zeros(3,3);
temp2 = zeros(3,3);
temp3 = zeros(3,3);
A = zeros(2,2);
C = zeros(height,width);
S = img;
k = 0.04;
for j=2:height-1
    for i=2:width-1
        for m=-1:1
            for n=-1:1
                temp1(m+2,n+2) = G(m+2,n+2)*sqrdy(j+m,i+n);
                temp2(m+2,n+2) = G(m+2,n+2)*dydx(j+m,i+n);
                temp3(m+2,n+2) = G(m+2,n+2)*sqrdx(j+m,i+n);
            end
        end       
        A = [sum(sum(temp1)) sum(sum(temp2)) ; sum(sum(temp2)) sum(sum(temp3))]; % 2�� ���Ʈ ���
        S(j,i) = [j i]*A*[j i]'; % �ڳ�
        C(j,i) = det(A)-(k*(trace(A)^2)); % Ư�� ���ɼ� ��
    end
end

C = abs(C);
max_C = max(C);
sort_max_C = sort(max_C,'descend');
threshold = sort_max_C(128);
for j=1:height % Ư¡�� ����
    for i=1:width
        if C(j,i)>=threshold
            C(j,i) = 1;
        else
            C(j,i) = 0;
        end
    end
end

% figure(1) % ���� ����
% imshow(img/255); 
% figure(2) % �ڳ� ����
% imshow(S/max(max(S))); 
figure(3) % 2�� ���Ʈ ����� �̿��� Ư¡�� ����
imshow(C);
