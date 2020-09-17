function output = newQuantization(input,distance,sample)

samp_dist=zeros(1,distance); %�Ÿ����� ���ü� ����
for i=1:distance
    for ii=1:sample
        if i==input(ii)
            samp_dist(i)=samp_dist(i)+1;
        end
    end
end

sort_samp_dist=sort(samp_dist,'descend'); %���帹�� ���ü��� ���� �Ÿ��� ������ ����
if sort_samp_dist(1)~=samp_dist(1) %���帹�� ������ �Ÿ����� 0�� �ƴѰ��
    max_samp_num=sort_samp_dist(1);
else  %���帹�� ������ �Ÿ����� 0�ΰ��
    max_samp_num=sort_samp_dist(2);
end

md=zeros(1,sample);
x=1;
for i=1:distance
    if samp_dist(i)==max_samp_num %���帹�� ������ ���� �ִ� �Ÿ����� ã��
       md(x)=i; %ã���� md�� ����
       x=x+1;
    end
end

Q=zeros(1,sample);
neomd=zeros(1,sample);
    if sum(sign(md))==1 %% level�� 1�� ���
        Q(sample)=md(1); %�� ����ȭ
    elseif sum(sign(md))==2 %% level�� 2�� ���
        if md(1)==input(sample) %% ���߿� �ֽŰ����� ã��
                neomd(1)=md(1);
                neomd(2)=md(2);
        elseif md(2)==input(sample)
                neomd(1)=md(2);
                neomd(2)=md(1);
        end
        Q(sample)=neomd(1); %���߿� �ֽŰ����� ����ȭ
    elseif sum(sign(md))>=3  %% level�� 3,�̻��� ���
        if md(1)==input(sample)%% ���߿� �ֽŰ����� ã��
            neomd(1)=md(1);
        elseif md(2)==input(sample)
            neomd(1)=md(2);
        elseif md(3)==input(sample)
            neomd(1)=md(3);
        end
        Q(sample)=neomd(1); 
    end
    o=0;
    for i=1:length(Q)
        if Q(i)~=0
            o=o+1;
        end
    end
    out=zeros(1,o);
    oo=1;
     for i=1:length(Q)
        if Q(i)~=0
            out(oo)=Q(i);
            oo=oo+1;
        end
     end
    for i=1:length(out)
    if abs(out(i)-out(1))>3
        out(i)=out(1);
    end
    end
    output=out;
end
