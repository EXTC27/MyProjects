function output = neoQuantization(input,distance,qsample)
% 20���� 10�� ����ȭ
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% for i=2:sample %��������
%    error=abs(input(i)-input(i-1));
%    if error>=100 && input(i)~=0 && input(i-1)~=0
%        input(i)=input(i-1);
%    end
% end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

samp_dist=zeros(1,distance); %�Ÿ����� ���ü� ����
for i=1:distance
    for ii=1:qsample
        if i==input(ii)
            samp_dist(i)=samp_dist(i)+1;
        end
    end
end

sort_samp_dist=sort(samp_dist,'descend'); %���帹�� ���ü��� ���� �Ÿ��� ������ ����
if sort_samp_dist(1)~=samp_dist(1) %���帹�� ������ �Ÿ����� 0�� �ƴѰ��
    max_samp_num=sort_samp_dist(1);
else                    %���帹�� ������ �Ÿ����� 0�ΰ��
    max_samp_num=sort_samp_dist(2);
end

md=zeros(1,qsample);
x=1;
for i=1:distance
    if samp_dist(i)==max_samp_num %���帹�� ������ ���� �ִ� �Ÿ����� ã��
       md(x)=i; %ã���� md�� ����
       x=x+1;
    end
end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ���Ⱑ �߿��� ���� ������ ����
%hsample=sample/2; %������ ���� 1�� ����ȭ�� 10����
Q=zeros(1,qsample);
neomd=zeros(1,qsample);
    if sum(sign(md))==1 %% level�� 1�� ���
        Q(qsample)=md(1); %�� ����ȭ
    elseif sum(sign(md))==2 %% level�� 2�� ���
        if md(1)==input(qsample) %% ���߿� �ֽŰ����� ã��
                neomd(1)=md(1);
                neomd(2)=md(2);
        elseif md(2)==input(qsample)
                neomd(1)=md(2);
                neomd(2)=md(1);
        end
        if abs(md(1)-md(2)) <= 100%% ���������� +-1�̸�
            Q(qsample)=neomd(1); %���߿� �ֽŰ����� ����ȭ
        else %%������ +-1�� �ƴϸ�
            Q(qsample)=neomd(1);
            Q(round(qsample/2))=neomd(2);
        end
    elseif sum(sign(md))>=3  %% level�� 3,�̻��� ���
        if md(1)==input(qsample)%% ���߿� �ֽŰ����� ã��
            neomd(1)=md(1);
            for i=(qsample-1):-1:1
                if md(2)==input(i) && input(i)~=input(qsample) %%
                    neomd(2)=md(2); neomd(3)=md(3); break
                elseif md(3)==input(i) && input(i)~=input(qsample)
                    neomd(2)=md(3); neomd(3)=md(2); break
                end
            end
            
        elseif md(2)==input(qsample)
            neomd(1)=md(2);
            for i=(qsample-1):-1:1
                if md(1)==input(i) && input(i)~=input(qsample) %%
                    neomd(2)=md(1); neomd(3)=md(3); break
                elseif md(3)==input(i) && input(i)~=input(qsample)
                    neomd(2)=md(3); neomd(3)=md(1); break
                end
            end
        elseif md(3)==input(qsample)
            neomd(1)=md(3);
            for i=(qsample-1):-1:1
                if md(1)==input(i) && input(i)~=input(qsample) %%
                    neomd(2)=md(1); neomd(3)=md(2); break
                elseif md(2)==input(i) && input(i)~=input(qsample)
                    neomd(2)=md(2); neomd(3)=md(1); break
                end
            end
        end
        if (abs(neomd(1)-neomd(2)) <= 100) || (abs(neomd(2)-neomd(3)) <= 100) %% ���������� +-1�̸�
            Q(qsample)=neomd(1); %���߿� �ֽŰ����� ����ȭ
            if (abs(neomd(2)-neomd(3)) > 1)
                Q(round(qsample/2))=neomd(2);      
            elseif (round(neomd(1)-neomd(2)) > 1)
                Q(round(qsample/2))=neomd(3);      
            end
        else %%������ +-1�� �ƴϸ�
            Q(qsample)=neomd(1);
            Q(round(qsample/2))=neomd(2);
            Q(1)=neomd(3);
           
        end   
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
