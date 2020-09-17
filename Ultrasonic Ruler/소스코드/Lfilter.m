function out = Lfilter(a)
b = sort(a);
c = reshape(b,10,2);
d=transpose(c);
e=d(1,:);
out = e;
