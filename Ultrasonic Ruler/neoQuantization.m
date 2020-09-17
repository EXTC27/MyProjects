function output = neoQuantization(input,distance,qsample)
% 20개중 10개 양자화
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% for i=2:sample %오류값이
%    error=abs(input(i)-input(i-1));
%    if error>=100 && input(i)~=0 && input(i-1)~=0
%        input(i)=input(i-1);
%    end
% end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

samp_dist=zeros(1,distance); %거리값당 샘플수 가산
for i=1:distance
    for ii=1:qsample
        if i==input(ii)
            samp_dist(i)=samp_dist(i)+1;
        end
    end
end

sort_samp_dist=sort(samp_dist,'descend'); %가장많은 샘플수를 지닌 거리값 순으로 정렬
if sort_samp_dist(1)~=samp_dist(1) %가장많은 샘플의 거리값이 0이 아닌경우
    max_samp_num=sort_samp_dist(1);
else                    %가장많은 샘플의 거리값이 0인경우
    max_samp_num=sort_samp_dist(2);
end

md=zeros(1,qsample);
x=1;
for i=1:distance
    if samp_dist(i)==max_samp_num %가장많은 샘플을 갖고 있는 거리값을 찾자
       md(x)=i; %찾으면 md에 저장
       x=x+1;
    end
end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 여기가 중요해 레벨 나누는 구간
%hsample=sample/2; %샘플의 절반 1차 양자화는 10개만
Q=zeros(1,qsample);
neomd=zeros(1,qsample);
    if sum(sign(md))==1 %% level이 1인 경우
        Q(qsample)=md(1); %걍 양자화
    elseif sum(sign(md))==2 %% level이 2인 경우
        if md(1)==input(qsample) %% 둘중에 최신값부터 찾자
                neomd(1)=md(1);
                neomd(2)=md(2);
        elseif md(2)==input(qsample)
                neomd(1)=md(2);
                neomd(2)=md(1);
        end
        if abs(md(1)-md(2)) <= 100%% 오차범위가 +-1이면
            Q(qsample)=neomd(1); %둘중에 최신값으로 양자화
        else %%오차가 +-1이 아니면
            Q(qsample)=neomd(1);
            Q(round(qsample/2))=neomd(2);
        end
    elseif sum(sign(md))>=3  %% level이 3,이상인 경우
        if md(1)==input(qsample)%% 셋중에 최신값부터 찾자
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
        if (abs(neomd(1)-neomd(2)) <= 100) || (abs(neomd(2)-neomd(3)) <= 100) %% 오차범위가 +-1이면
            Q(qsample)=neomd(1); %둘중에 최신값으로 양자화
            if (abs(neomd(2)-neomd(3)) > 1)
                Q(round(qsample/2))=neomd(2);      
            elseif (round(neomd(1)-neomd(2)) > 1)
                Q(round(qsample/2))=neomd(3);      
            end
        else %%오차가 +-1이 아니면
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
