from selenium import webdriver
from urllib.request import urlopen
from bs4 import BeautifulSoup
#import pandas as pd
import pymysql
import datetime

driver = webdriver.Chrome('C:/Users/multicampus/Downloads/chromedriver_win32/chromedriver')
driver.implicitly_wait(3)
driver.get('https://sports.news.naver.com/kfootball/schedule/index.nhn?year=2020&month=05&category=acl')
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
# content=[]
data_result=soup.find('tbody',{'id':'_monthlyScheduleList'}).findAll('tr')
for tr_data in data_result:
    if tr_data.find('em')!=None:
        day_tmp=tr_data.find('em')
    if tr_data.find('td',{'class':'time_place'})==None:
        continue
    day.append(str(day_tmp).replace('<em>', '').replace('</em>',''))
    time.append(str(tr_data.find('span',{'class':'time'})).replace('<span class="time">', '').replace('</span>',''))
    place.append(str(tr_data.find('span',{'class':'place'})).replace('<span class="place">', '').replace('</span>',''))
    left_team.append(str(tr_data.find('span',{'class':'team_left'}).find('span',{'class':'name'})).replace('<span class="name">', '').replace('</span>',''))
    right_team.append(str(tr_data.find('span',{'class':'team_right'}).find('span',{'class':'name'})).replace('<span class="name">', '').replace('</span>',''))
    # content.append(tr_data.find('td',{'class':'game_content'}).find('span').text)
dt=[]
for i in range(len(day)):
    dt.append(datetime.datetime(2020,5,int(day[i].split('.')[1]),int(time[i].split(':')[0]),int(time[i].split(':')[1]),0,0))
#값 넣을때 년/월을 중요하게 할 것!
data=[]
for i in range(len(day)):
    data.append((dt[i],dt[i]+datetime.timedelta(hours=2),left_team[i]+" vs "+right_team[i],place[i],"AFC 챔피언스리그",18,datetime.datetime.now(),datetime.datetime.now()))
print(data)
# query="""insert into schedules(sdate,edate,title,place,contents,ch_no,created_date,modified_date) values (%s,%s,%s,%s,%s,%s,%s,%s)"""
# cursor.executemany(query, tuple(data))
# db.commit()