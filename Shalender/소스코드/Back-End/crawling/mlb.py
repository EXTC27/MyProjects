from selenium import webdriver
from urllib.request import urlopen
from bs4 import BeautifulSoup
#import pandas as pd
import pymysql
import datetime

driver = webdriver.Chrome('C:/Users/multicampus/Downloads/chromedriver_win32/chromedriver')
driver.implicitly_wait(3)
driver.get('https://sports.news.naver.com/wbaseball/schedule/index.nhn?year=2020&month=09&category=mlb')
html = driver.page_source
soup = BeautifulSoup(html, 'html.parser')
db=pymysql.connect("localhost","ssafy","ssafy","forchannel",charset="utf8")
cursor = db.cursor()
#print(soup)
day=[]
time=[]
left_team=[]
right_team=[]
place=[]
data_result=soup.find('tbody',{'id':'_monthlyScheduleList'}).findAll('tr')
for tr_data in data_result:
    if tr_data.find('em')!=None:
        day_tmp=tr_data.find('em').text
    if tr_data.find('td',{'class':'time_place'})==None:
        continue
    day.append(str(day_tmp))
    time.append(tr_data.find('span',{'class':'time'}).text)
    place.append(tr_data.find('span',{'class':'place'}).text)
    left_team.append(tr_data.find('span',{'class':'team_left'}).text.replace('\n',''))
    right_team.append(tr_data.find('span',{'class':'team_right'}).text.replace('\n',''))
dt=[]

for i in range(len(day)):
    dt.append(datetime.datetime(2020,9,int(day[i].split('.')[1]),int(time[i].split(':')[0]),int(time[i].split(':')[1]),0,0))
#값 넣을때 년/월을 중요하게 할 것!
data=[]
for i in range(len(day)):
    data.append((dt[i],dt[i]+datetime.timedelta(hours=4),left_team[i]+" vs "+right_team[i],place[i],"메이저 리그",16,datetime.datetime.now(),datetime.datetime.now()))
print(data)
query="""insert into schedules(sdate,edate,title,place,contents,ch_no,created_date,modified_date) values (%s,%s,%s,%s,%s,%s,%s,%s)"""
cursor.executemany(query, tuple(data))
db.commit()