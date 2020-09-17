import pandas as pd
import pymysql
import requests as rq
import datetime

db=pymysql.connect("localhost","ssafy","ssafy","forchannel",charset="utf8")
cursor = db.cursor()
CSV_PATH = 'C:/Users/multicampus/Desktop/crawling/jobkorea.csv'
csv_test = pd.read_csv(CSV_PATH)
sub=pd.DataFrame(csv_test, columns=['title','contents','sdate','edate'])
# sub=csv_test.data[['전시회명','전시장소','전시시작기간','전시끝기간','전시설명']]
data = [tuple(list(x)+[29,datetime.datetime.now(),datetime.datetime.now()]) for x in sub.values]
print(data)
query="""insert into schedules(title,contents,sdate,edate,ch_no,created_date,modified_date) values (%s,%s,%s,%s,%s,%s,%s)"""
cursor.executemany(query, data)
db.commit()