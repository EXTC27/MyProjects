from selenium import webdriver
from urllib.request import urlopen
from bs4 import BeautifulSoup
import pymysql
import sys
import datetime

driver = webdriver.Chrome('C:/Users/multicampus/Downloads/chromedriver_win32/chromedriver')
driver.implicitly_wait(3)
driver.get('https://movie.naver.com/movie/running/premovie.nhn?&order=open')
html = driver.page_source
soup = BeautifulSoup(html, 'html.parser')
db=pymysql.connect("localhost","ssafy","ssafy","forchannel",charset="utf8")
cursor = db.cursor()
title=[]
contents=[]
day=[]
tmp_data=[]
data_result=soup.findAll('ul',{'class','lst_detail_t1'})
for li_tmp in data_result:
    data_li=li_tmp.findAll('li')
    for l in data_li:
        tmp_data.append(l)
for li_data in tmp_data:
    if len(li_data.find('dl',{'class':'info_txt1'}).find('dd').text.replace('\n', '').replace('\t','').split("|")[-1].split(" ")[0].split("."))==2:
        continue
    title.append(li_data.find('dt',{'class':'tit'}).find('a').text)
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