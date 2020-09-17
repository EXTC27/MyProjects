import pandas as pd
import pymysql
import requests as rq
import datetime

db=pymysql.connect("localhost","ssafy","ssafy","forchannel",charset="utf8")
cursor = db.cursor()
CSV_PATH = 'C:/Users/multicampus/Desktop/crawling/sisahe.csv'
csv_test = pd.read_csv(CSV_PATH)
sub=pd.DataFrame(csv_test, columns=['title','contents','sdate','stime','edate','etime','place','attendants'])
# sub=csv_test.data[['전시회명','전시장소','전시시작기간','전시끝기간','전시설명']]
tmp = [tuple(list(x)) for x in sub.values]
data=[]
for i in range(len(tmp)):
    s_date=datetime.datetime(int(tmp[i][2].split('-')[0]),int(tmp[i][2].split('-')[1]),int(tmp[i][2].split('-')[2]),int(tmp[i][3].split(':')[0]),int(tmp[i][3].split(':')[1]),0,0)
    e_date=datetime.datetime(int(tmp[i][4].split('-')[0]),int(tmp[i][4].split('-')[1]),int(tmp[i][4].split('-')[2]),int(tmp[i][5].split(':')[0]),int(tmp[i][5].split(':')[1]),0,0)
    data.append((tmp[i][0],tmp[i][1],s_date,e_date,tmp[i][6],tmp[i][7],30,datetime.datetime.now(),datetime.datetime.now()))
print(data)
# query="""insert into schedules(title,contents,sdate,edate,place,attendants,ch_no,created_date,modified_date) values (%s,%s,%s,%s,%s,%s,%s,%s,%s)"""
# cursor.executemany(query, data)
# db.commit()