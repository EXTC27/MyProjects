# import sys
# import io

# sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding = 'utf-8')
# sys.stderr = io.TextIOWrapper(sys.stderr.detach(), encoding = 'utf-8')
from selenium import webdriver
from urllib.request import urlopen
from bs4 import BeautifulSoup
import pymysql
import sys
import datetime

driver = webdriver.Chrome('C:/Users/multicampus/Downloads/chromedriver_win32/chromedriver')
driver.implicitly_wait(3)
driver.get('https://movie.naver.com/movie/running/current.nhn?view=list&tab=normal&order=reserve')
html = driver.page_source
soup = BeautifulSoup(html, 'html.parser')
db=pymysql.connect("localhost","ssafy","ssafy","forchannel",charset="utf8")
cursor = db.cursor()
title=[]
day=[]
contents=[]
data_result=soup.find('div',{'id': 'content'}).find('div',{'class':'lst_wrap'}).find('ul',{'class':'lst_detail_t1'}).findAll('li')
for li_data in data_result:
    title.append(li_data.find('dt',{'class':'tit'}).a.text)
    contents.append(li_data.find('span',{'class':'link_txt'}).text.replace('\n', '').replace('\t',''))
    day.append(li_data.find('dl',{'class':'info_txt1'}).find('dd').text.replace('\n', '').replace('\t','').split("|")[-1].split(" ")[0])

data=[]
for i in range(len(day)):
    day[i]=datetime.datetime(int(day[i].split('.')[0]),int(day[i].split('.')[1]),int(day[i].split('.')[2]),0,0,0,0)
    data.append((title[i],day[i],day[i],contents[i],31,datetime.datetime.now(),datetime.datetime.now()))
print(data)
query="""insert into schedules(title,sdate,edate,contents, ch_no, created_date, modified_date) values (%s,%s,%s,%s,%s,%s,%s)"""
cursor.executemany(query, tuple(data))
db.commit()