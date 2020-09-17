function output = newQuantization(input,distance,sample)

samp_dist=zeros(1,distance); %거리값당 샘플수 가산
for i=1:distance
    for ii=1:sample
        if i==input(ii)
            samp_dist(i)=samp_dist(i)+1;
        end
    end
end

sort_samp_dist=sort(samp_dist,'descend'); %가장많은 샘플수를 지닌 거리값 순으로 정렬
if sort_samp_dist(1)~=samp_dist(1) %가장많은 샘플의 거리값이 0이 아닌경우
    max_samp_num=sort_samp_dist(1);
else  %가장많은 샘플의 거리값이 0인경우
    max_samp_num=sort_samp_dist(2);
end

md=zeros(1,sample);
x=1;
for i=1:distance
    if samp_dist(i)==max_samp_num %가장많은 샘플을 갖고 있는 거리값을 찾자
       md(x)=i; %찾으면 md에 저장
       x=x+1;
    end
end

Q=zeros(1,sample);
neomd=zeros(1,sample);
    if sum(sign(md))==1 %% level이 1인 경우
        Q(sample)=md(1); %걍 양자화
    elseif sum(sign(md))==2 %% level이 2인 경우
        if md(1)==input(sample) %% 둘중에 최신값부터 찾자
                neomd(1)=md(1);
                neomd(2)=md(2);
        elseif md(2)==input(sample)
                neomd(1)=md(2);
                neomd(2)=md(1);
        end
        Q(sample)=neomd(1); %둘중에 최신값으로 양자화
    elseif sum(sign(md))>=3  %% level이 3,이상인 경우
        if md(1)==input(sample)%% 셋중에 최신값부터 찾자
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
