import pandas as pd
import pymysql
import requests as rq
import datetime

db=pymysql.connect("localhost","ssafy","ssafy","forchannel",charset="utf8")
cursor = db.cursor()
CSV_PATH = 'C:/Users/multicampus/Desktop/crawling/art_info.csv'
csv_test = pd.read_csv(CSV_PATH)
sub=pd.DataFrame(csv_test, columns=['전시회명','전시장소','전시시작기간','전시끝기간','문의전화'])
# sub=csv_test.data[['전시회명','전시장소','전시시작기간','전시끝기간','전시설명']]
sub=sub.dropna(axis=0)
sub['전시회명']=sub['전시회명'].str[:30]
sub['전시장소']=sub['전시장소'].str[:50]
data = [tuple(list(x)+[20,datetime.datetime.now(),datetime.datetime.now()]) for x in sub.values]
print(data)
query="""insert into schedules(title,place,sdate,edate,contents,ch_no,created_date,modified_date) values (%s,%s,%s,%s,%s,%s,%s,%s)"""
cursor.executemany(query, tuple(data))
db.commit()