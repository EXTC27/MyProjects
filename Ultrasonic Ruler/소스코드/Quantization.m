function output = Quantization(input,distance,sample)

output=zeros(1,sample);

 for i=2:sample %오류값이
    error=abs(input(i)-input(i-1));
    if error>=100 && input(i)~=0 && input(i-1)~=0
        input(i)=input(i-1);
    end
 end

samp_dist=zeros(1,distance);

for i=1:distance
    for ii=1:sample
        if i==input(ii)
            samp_dist(i)=samp_dist(i)+1;
        end
    end
end

qd=zeros(1,sample);
j=1;
sort_samp_dist=sort(samp_dist,'descend');
max_samp_num=sort_samp_dist(1);
for i=1:distance
if samp_dist(i)==max_samp_num
qd(j)=i;
j=j+1;           
end
end

Bh=0;
for i=1:sample
    if sum(sign(qd))==1
        Bh=qd(1);
    elseif sum(sign(qd))==2
        for i2=sample:-1:1
            if qd(1)==input(i2) || qd(2)==input(i2)
                qd(1)=input(i2);
                break
            end
        end
        Bh=qd(1);
    elseif sum(sign(qd))==3
        for i3=sample:-1:1
            if qd(1)==input(i3) || qd(2)==input(i3) || qd(3)==input(i3)
                qd(1)=input(i3);
                break
            end
        end
        Bh=qd(1);
    elseif sum(sign(qd))==4
        for i4=sample:-1:1
            if qd(1)==input(i4) || qd(2)==input(i4) || qd(3)==input(i4) || qd(4)==input(i4)
            qd(1)=input(i4);
            break
            end
        end
        Bh=qd(1);
    elseif sum(sign(qd))==5
        for i5=sample:-1:1
            if qd(1)==input(i5) || qd(2)==input(i5) || qd(3)==input(i5) || qd(4)==input(i5) || qd(5)==input(i5)
                qd(1)=input(i5);
                break
            end
        end
        Bh=qd(1);
    end
    output(i)=Bh;
end
end



