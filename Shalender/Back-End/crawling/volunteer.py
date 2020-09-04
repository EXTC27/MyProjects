from selenium import webdriver
from urllib.request import urlopen
from bs4 import BeautifulSoup
import pymysql
import sys
import datetime

db=pymysql.connect("localhost","ssafy","ssafy","forchannel",charset="utf8")
cursor = db.cursor()
title=[]
content=[]
sdate=[]
edate=[]
driver = webdriver.Chrome('C:/Users/multicampus/Downloads/chromedriver_win32/chromedriver')
driver.implicitly_wait(6)
# 서울 6110000# 부산 6260000 # 대구 6270000 # 인천 6280000
# 광주 6290000# 대전 6300000
# 울산 6310000# 세종 5690000
# 경기 6410000# 강원 6420000
# 충북 6430000# 충남 6440000
# 전북 6450000# 전남 6460000
# 경북 6470000# 경남 6480000# 제주 6500000
for i in range(1,17):
    driver.get('https://www.1365.go.kr/vols/P9210/partcptn/timeCptn.do?searchHopeArea1=6300000&cPage='+str(i))
    html = driver.page_source
    soup = BeautifulSoup(html, 'html.parser')
    # db=pymysql.connect("localhost","ssafy","ssafy","crawling",charset="utf8")
    # cursor = db.cursor()
    data_result=soup.findAll('dl',{'class':'txts'})
    # print(data_result)
    for datas in data_result:
        if datas.find('dd',{'class':'board_data'}).findAll('dl')[2].text.replace(' ', '').replace('\n', '').replace('\t','').replace('봉사기간:', '').split('~')[0].split('-')[1]!=datas.find('dd',{'class':'board_data'}).findAll('dl')[2].text.replace(' ', '').replace('\n', '').replace('\t','').replace('봉사기간:', '').split('~')[1].split('-')[1]:
            continue
        #title부분
        title.append(datas.find('dt',{'class':'tit_board_list'}).text.replace('\n', '').replace('\t','')[:-6][:30])
        content.append(datas.find('dd',{'class':'text'}).text.replace('\n', '').replace('\t','')[:200])
        #sdate/edate
        sdate.append(datas.find('dd',{'class':'board_data'}).findAll('dl')[2].text.replace(' ', '').replace('\n', '').replace('\t','').replace('봉사기간:', '').split('~')[0])
        # edate.append()
        edate.append(datas.find('dd',{'class':'board_data'}).findAll('dl')[2].text.replace(' ', '').replace('\n', '').replace('\t','').replace('봉사기간:', '').split('~')[1])


data=[]
for i in range(len(title)):
    data.append((title[i],sdate[i],edate[i],content[i],37,datetime.datetime.now(),datetime.datetime.now()))
print(data)
query="""insert into schedules(title,sdate,edate,contents, ch_no, created_date, modified_date) values (%s,%s,%s,%s,%s,%s,%s)"""
cursor.executemany(query, tuple(data))
db.commit()