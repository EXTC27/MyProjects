import pandas as pd
import pymysql
import requests as rq
import datetime

db=pymysql.connect("localhost","ssafy","ssafy","forchannel",charset="utf8")
cursor = db.cursor()
CSV_PATH = 'C:/Users/multicampus/Desktop/crawling/festival_jr.csv'
csv_test = pd.read_csv(CSV_PATH)
sub=pd.DataFrame(csv_test, columns=['축제명','개최장소','축제시작일자','축제종료일자','축제내용'])
# sub=csv_test.data[['전시회명','전시장소','전시시작기간','전시끝기간','전시설명']]
sub['축제명']=sub['축제명'].str[:100]
sub['개최장소']=sub['개최장소'].str[:50]
sub['축제내용']=sub['축제내용'].str[:300]
sub=sub.dropna(axis=0)
data = [tuple(list(x)+[27,datetime.datetime.now(),datetime.datetime.now()]) for x in sub.values]
print(data)
query="""insert into schedules(title,place,sdate,edate,contents,ch_no,created_date,modified_date) values (%s,%s,%s,%s,%s,%s,%s,%s)"""
cursor.executemany(query, data)
db.commit()